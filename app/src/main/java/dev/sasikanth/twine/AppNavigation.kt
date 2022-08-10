package dev.sasikanth.twine

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.sasikanth.twine.home.HomePage
import dev.sasikanth.twine.login.LoginPage

internal sealed class Screen(val route: String) {
  object Login : Screen("login")
  object Home : Screen("home")
}

@Composable
internal fun AppNavigation(
  navController: NavHostController
) {
  NavHost(
    navController = navController,
    startDestination = Screen.Login.route
  ) {
    addLogin(navController)
    addHome(navController)
  }
}

internal fun NavGraphBuilder.addLogin(
  navController: NavHostController
) {
  composable(Screen.Login.route) {
    LoginPage(navigateToHome = {
      navController.navigate(Screen.Home.route) {
        popUpTo(Screen.Login.route) {
          inclusive = true
        }
      }
    })
  }
}

internal fun NavGraphBuilder.addHome(
  navController: NavHostController
) {
  composable(Screen.Home.route) {
    HomePage(
      navigateToSearch = {
        // TODO: Navigate to search
      },
      navigateToSettings = {
        // TODO: Navigate to settings
      }
    )
  }
}
