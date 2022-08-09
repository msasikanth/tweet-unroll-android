package dev.sasikanth.twine

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.sasikanth.twine.login.LoginPage

internal sealed class Screen(val route: String) {
  object Login : Screen("login")
}

@Composable
internal fun AppNavigation(
  navController: NavHostController
) {
  NavHost(
    navController = navController,
    startDestination = Screen.Login.route
  ) {
    addLogin()
  }
}

internal fun NavGraphBuilder.addLogin() {
  composable(Screen.Login.route) {
    LoginPage(navigateToHome = {
      // TODO: Navigate to home
    })
  }
}
