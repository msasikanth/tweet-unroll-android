package dev.sasikanth.twine.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sasikanth.twine.auth.AuthManager
import dev.sasikanth.twine.auth.TwineAuthState
import dev.sasikanth.twine.auth.TwineAuthState.FAILED_TO_LOGIN
import dev.sasikanth.twine.auth.TwineAuthState.IDLE
import dev.sasikanth.twine.auth.TwineAuthState.LOGGED_IN
import dev.sasikanth.twine.auth.TwineAuthState.LOGGED_OUT
import dev.sasikanth.twine.auth.TwineLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val authManager: AuthManager
) : ViewModel() {

  private val _uiState = MutableStateFlow(
    LoginUiState(
      isCheckingLoginStatus = false,
      isUserLoggedIn = false
    )
  )
  internal val uiState: StateFlow<LoginUiState>
    get() = _uiState

  init {
    viewModelScope.launch {
      authManager
        .authState
        .collect(::handleAuthState)
    }
  }

  fun buildTwineLoginActivityResult() = authManager.buildTwineLoginActivityResult()

  fun onLoginResult(result: TwineLogin.Result?) {
    viewModelScope.launch {
      authManager.onLoginResult(result)
    }
  }

  private fun handleAuthState(authState: TwineAuthState) {
    _uiState.update {
      when (authState) {
        IDLE -> it.checkingUserLoginStatus()
        LOGGED_IN -> it.userLoggedIn()
        LOGGED_OUT,
        FAILED_TO_LOGIN -> it.userNotLoggedIn()
      }
    }
  }
}
