package dev.sasikanth.twine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.sasikanth.twine.common.ui.theme.TwineTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      TwineTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = TwineTheme.colorScheme.surface
        ) {
          AppNavigation(navController = rememberNavController())
        }
      }
    }
  }
}
