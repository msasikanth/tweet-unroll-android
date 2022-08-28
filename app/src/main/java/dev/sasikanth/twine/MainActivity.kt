package dev.sasikanth.twine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.data.prefrences.Theme.DARK
import dev.sasikanth.twine.data.prefrences.Theme.LIGHT
import dev.sasikanth.twine.data.prefrences.Theme.SYSTEM

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @OptIn(ExperimentalLifecycleComposeApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    val viewModel by viewModels<MainViewModel>()

    setContent {
      val uiState by viewModel.uiState.collectAsStateWithLifecycle()

      val useDarkTheme = when (uiState.theme) {
        LIGHT -> false
        DARK -> true
        SYSTEM -> isSystemInDarkTheme()
      }

      TwineTheme(
        useDynamicColors = uiState.useDynamicColors,
        useDarkTheme = useDarkTheme
      ) {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = TwineTheme.colorScheme.surface
        ) {
          AppNavigation(
            navController = rememberNavController(),
            theme = uiState.theme,
            useDynamicColors = uiState.useDynamicColors,
            onThemeChange = viewModel::setTheme,
            toggleDynamicColors = viewModel::setDynamicColors
          )
        }
      }
    }
  }
}
