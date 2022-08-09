package dev.sasikanth.twine.login

internal data class LoginUiState(
  val isCheckingLoginStatus: Boolean = true,
  val isUserLoggedIn: Boolean = false,
) {

  fun checkingUserLoginStatus(): LoginUiState {
    return copy(isCheckingLoginStatus = true)
  }

  fun userLoggedIn(): LoginUiState {
    return copy(
      isCheckingLoginStatus = false,
      isUserLoggedIn = true
    )
  }

  fun userNotLoggedIn(): LoginUiState {
    return copy(
      isCheckingLoginStatus = false,
      isUserLoggedIn = false
    )
  }
}
