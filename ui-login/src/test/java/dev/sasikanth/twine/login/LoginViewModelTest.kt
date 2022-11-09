package dev.sasikanth.twine.login

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.sasikanth.twine.auth.TwineLogin
import dev.sasikanth.twine.common.testing.auth.FakeAuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationException.TYPE_GENERAL_ERROR
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

  private lateinit var viewModel: LoginViewModel

  private val uiState = LoginUiState(
    isCheckingLoginStatus = false,
    isUserLoggedIn = false
  )

  @Before
  fun setup() {
    val testDispatcher = UnconfinedTestDispatcher()
    Dispatchers.setMain(testDispatcher)

    viewModel = LoginViewModel(
      authManager = FakeAuthManager()
    )
  }

  @Test
  fun `when viewmodel is created, then login status should be checked`() = runTest {
    viewModel.uiState.test {
      assertThat(awaitItem()).isEqualTo(uiState.checkingUserLoginStatus())
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `when login activity result is received, then handle login result`() = runTest {
    // given
    val userLoginFailedResult = TwineLogin.Result(
      response = null,
      error = AuthorizationException(
        TYPE_GENERAL_ERROR,
        0,
        "Login page closed",
        null,
        null,
        null
      )
    )

    // when
    viewModel.onLoginResult(userLoginFailedResult)

    // then
    viewModel.uiState.test {
      assertThat(awaitItem()).isEqualTo(uiState.userNotLoggedIn())
      cancelAndIgnoreRemainingEvents()
    }
  }
}
