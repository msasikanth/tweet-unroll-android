package dev.sasikanth.twine.auth

import android.content.Context
import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.sasikanth.twine.auth.AuthConstants.PREF_KEY_AUTH_STATE
import dev.sasikanth.twine.auth.TwineAuthState.FAILED_TO_LOGIN
import dev.sasikanth.twine.auth.TwineAuthState.IDLE
import dev.sasikanth.twine.auth.TwineAuthState.LOGGED_IN
import dev.sasikanth.twine.auth.TwineAuthState.LOGGED_OUT
import dev.sasikanth.twine.common.dispatchers.CoroutineDispatchers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.TokenResponse
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface AuthManager {
  val authState: StateFlow<TwineAuthState>

  /**
   * Create an activity result contract for login
   *
   * see also: [TwineLogin]
   */
  fun buildTwineLoginActivityResult(): TwineLogin?

  /**
   * Handle login result obtained from activity result
   */
  suspend fun onLoginResult(result: TwineLogin.Result?)

  /**
   * Clear auth state from preference and notify state
   */
  suspend fun logout()
}

/**
 * Class to manage Twine authentication
 *
 * @param context: Application context
 * @param requestProvider: An OAuth2 authorization request
 * @param clientAuth: Client secret for requesting OAuth2 token
 * @param coroutineDispatchers: [CoroutineDispatchers]
 * @param authPref: Preference where [AuthState] is saved
 */
@OptIn(DelicateCoroutinesApi::class)
@Singleton
class TwineAuthManager @Inject constructor(
  @ApplicationContext context: Context,
  private val requestProvider: Lazy<AuthorizationRequest>,
  private val clientAuth: Lazy<ClientAuthentication>,
  private val coroutineDispatchers: CoroutineDispatchers,
  @Named("auth_pref") private val authPref: DataStore<Preferences>,
) : AuthManager {

  private val authService = AuthorizationService(context)

  private val _authState = MutableStateFlow(IDLE)
  override val authState: StateFlow<TwineAuthState>
    get() = _authState.asStateFlow()

  init {
    // Read the current auth state from preferences and notify the state flow
    GlobalScope.launch(coroutineDispatchers.main) {
      val state = withContext(coroutineDispatchers.io) { readAuthState() }
      _authState.update {
        when {
          state.isAuthorized -> LOGGED_IN
          else -> LOGGED_OUT
        }
      }
    }
  }

  /**
   * Create an activity result contract for login
   *
   * see also: [TwineLogin]
   */
  override fun buildTwineLoginActivityResult() = TwineLogin(intentBuilder = ::buildLoginIntent)

  /**
   * Handle login result obtained from activity result
   */
  override suspend fun onLoginResult(result: TwineLogin.Result?) {
    val (response, ex) = result ?: return

    _authState.update {
      when {
        response != null -> handleAuthorizationResponse(response)
        ex != null -> FAILED_TO_LOGIN
        else -> LOGGED_OUT
      }
    }
  }

  /**
   * Clear auth state from preference and notify state
   */
  override suspend fun logout() {
    clearAuthState()
    _authState.update { LOGGED_OUT }
  }

  /**
   * Function to fetch latest tokens from the auth state, to avoid using expired tokens
   * when making API calls
   *
   * It reads the current auth state from preferences and then pulls new tokens if needed.
   * If the tokens are updated, we update the auth state saved in preferences.
   *
   * @return latest [AuthState]
   *
   * see also: [TwineAuthInterceptor]
   */
  internal suspend fun fetchFreshAccessToken(): String? {
    return withContext(coroutineDispatchers.io) {
      val authState = readAuthState()
      val oldAccessToken = authState.accessToken

      val isAccessTokenUpdated = suspendCoroutine { continuation ->
        authState.performActionWithFreshTokens(authService) { newAccessToken, _, _ ->
          val isAccessTokenUpdated = oldAccessToken != newAccessToken
          continuation.resume(isAccessTokenUpdated)
        }
      }

      if (isAccessTokenUpdated) saveAuthState(authState)

      authState.accessToken
    }
  }

  /**
   * Handle [AuthorizationResponse] returned from login result
   *
   * - Request OAuth2 token
   * - Save auth state in preferences
   * - Notify [_authState]
   */
  private suspend fun handleAuthorizationResponse(response: AuthorizationResponse): TwineAuthState {
    val (tokenResponse, tokenRequestException) = requestToken(response)

    val authState = AuthState(response, null).apply {
      update(tokenResponse, tokenRequestException)
    }
    saveAuthState(authState)

    return if (authState.isAuthorized) {
      LOGGED_IN
    } else {
      LOGGED_OUT
    }
  }

  /**
   * Suspend function for fetching token after OAuth authorization
   */
  private suspend fun requestToken(authorizationResponse: AuthorizationResponse): Pair<TokenResponse?, AuthorizationException?> {
    return withContext(coroutineDispatchers.io) {
      suspendCoroutine { continuation ->
        authService.performTokenRequest(
          authorizationResponse.createTokenExchangeRequest(),
          clientAuth.get()
        ) { tokenResponse, exception ->
          continuation.resume(Pair(tokenResponse, exception))
        }
      }
    }
  }

  /**
   * Read current [AuthState] from the preferences
   */
  private suspend fun readAuthState(): AuthState {
    return withContext(coroutineDispatchers.io) {
      val authStateString = authPref.data.first()[stringPreferencesKey(PREF_KEY_AUTH_STATE)]

      return@withContext if (authStateString.isNullOrBlank()) {
        AuthState()
      } else {
        AuthState.jsonDeserialize(authStateString)
      }
    }
  }

  /**
   * Write [AuthState] to the preferences
   */
  private suspend fun saveAuthState(state: AuthState) {
    authPref.edit { authPref ->
      authPref[stringPreferencesKey(PREF_KEY_AUTH_STATE)] = state.jsonSerializeString()
    }
  }

  /**
   * Clear auth state from preferences
   */
  private suspend fun clearAuthState() {
    withContext(coroutineDispatchers.io) {
      authPref.edit { it.clear() }
    }
  }

  /**
   * Build login intent using [AuthorizationService] & [AuthorizationRequest]
   */
  private fun buildLoginIntent(): Intent {
    return authService.getAuthorizationRequestIntent(requestProvider.get())
  }
}
