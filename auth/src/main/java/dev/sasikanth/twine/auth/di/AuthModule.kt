package dev.sasikanth.twine.auth.di

import android.content.Context
import android.util.Base64
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.auth.AuthConstants.AUTH_AUTHORIZE_URI
import dev.sasikanth.twine.auth.AuthConstants.AUTH_PREF_FILE
import dev.sasikanth.twine.auth.AuthConstants.AUTH_REDIRECT_URI
import dev.sasikanth.twine.auth.AuthConstants.AUTH_TOKEN_URI
import dev.sasikanth.twine.auth.AuthConstants.CODE_VERIFIER_CHALLENGE_METHOD
import dev.sasikanth.twine.auth.AuthConstants.MESSAGE_DIGEST_ALGORITHM
import dev.sasikanth.twine.auth.AuthConstants.SCOPE_OFFLINE_ACCESS
import dev.sasikanth.twine.auth.AuthConstants.SCOPE_TWEET_READ
import dev.sasikanth.twine.auth.AuthConstants.SCOPE_TWEET_WRITE
import dev.sasikanth.twine.auth.AuthConstants.SCOPE_USERS_READ
import dev.sasikanth.twine.common.dispatchers.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ResponseTypeValues
import java.security.MessageDigest
import java.security.SecureRandom
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

  @Provides
  @Singleton
  fun providesAuthConfig(): AuthorizationServiceConfiguration {
    return AuthorizationServiceConfiguration(
      AUTH_AUTHORIZE_URI.toUri(),
      AUTH_TOKEN_URI.toUri()
    )
  }

  @Provides
  @Singleton
  fun providesClientAuthentication(
    @Named("auth_client_secret") clientSecret: String
  ): ClientAuthentication {
    return ClientSecretBasic(clientSecret)
  }

  @Provides
  fun providesAuthorizationRequest(
    configuration: AuthorizationServiceConfiguration,
    @Named("auth_client_id") clientId: String
  ): AuthorizationRequest {

    val secureRandom = SecureRandom()
    val bytes = ByteArray(64)
    secureRandom.nextBytes(bytes)

    val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
    val codeVerifier = Base64.encodeToString(bytes, encoding)

    val digest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM)
    val hash = digest.digest(codeVerifier.toByteArray())
    val codeChallenge = Base64.encodeToString(hash, encoding)

    return AuthorizationRequest
      .Builder(
        configuration,
        clientId,
        ResponseTypeValues.CODE,
        AUTH_REDIRECT_URI.toUri()
      )
      .setScopes(
        SCOPE_TWEET_READ,
        SCOPE_TWEET_WRITE,
        SCOPE_USERS_READ,
        SCOPE_OFFLINE_ACCESS
      )
      .setCodeVerifier(codeVerifier, codeChallenge, CODE_VERIFIER_CHALLENGE_METHOD)
      .build()
  }

  @Provides
  @Singleton
  @Named("auth_pref")
  fun providesDatastorePref(
    @ApplicationContext context: Context,
    coroutineDispatchers: CoroutineDispatchers
  ): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
      corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { emptyPreferences() }
      ),
      scope = CoroutineScope(coroutineDispatchers.io + SupervisorJob()),
      produceFile = { context.preferencesDataStoreFile(AUTH_PREF_FILE) }
    )
  }
}
