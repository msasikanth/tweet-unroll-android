package dev.sasikanth.twine

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.sasikanth.twine.data.prefrences.Theme
import dev.sasikanth.twine.home.HomePage
import dev.sasikanth.twine.login.LoginPage
import dev.sasikanth.twine.settings.SettingsPage

internal sealed class Screen(val route: String) {
  object Login : Screen("login")
  object Home : Screen("home")
  object Settings : Screen("settings")
}

@Composable
internal fun AppNavigation(
  navController: NavHostController,
  theme: Theme,
  useDynamicColors: Boolean,
  onThemeChange: (Theme) -> Unit,
  toggleDynamicColors: (Boolean) -> Unit
) {
  NavHost(
    navController = navController,
    startDestination = Screen.Login.route
  ) {
    addLogin(navController)
    addHome(navController)
    addSettings(
      navController = navController,
      theme = theme,
      useDynamicColors = useDynamicColors,
      onThemeChange = onThemeChange,
      toggleDynamicColors = toggleDynamicColors
    )
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
        navController.navigate(Screen.Settings.route)
      }
    )
  }
}

internal fun NavGraphBuilder.addSettings(
  navController: NavHostController,
  theme: Theme,
  useDynamicColors: Boolean,
  onThemeChange: (Theme) -> Unit,
  toggleDynamicColors: (Boolean) -> Unit
) {
  composable(Screen.Settings.route) {
    SettingsPage(
      theme = theme,
      useDynamicColors = useDynamicColors,
      onThemeChange = onThemeChange,
      toggleDynamicColors = toggleDynamicColors,
      navigateBack = {
        navController.popBackStack()
      }
    )
  }
}
