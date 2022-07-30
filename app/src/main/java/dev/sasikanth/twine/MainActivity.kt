package dev.sasikanth.twine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import dev.sasikanth.twine.common.ui.theme.TwineTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      TwineTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = TwineTheme.colorScheme.brand
        ) {
          Greeting(
            modifier = Modifier.systemBarsPadding(),
            name = "Android"
          )
        }
      }
    }
  }
}

@Composable
fun Greeting(
  modifier: Modifier = Modifier,
  name: String
) {
  Text(
    modifier = modifier
      .padding(16.dp),
    text = "Hello $name!",
    style = TwineTheme.typography.titleMedium,
    color = TwineTheme.colorScheme.onBrand
  )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  TwineTheme {
    Greeting(name = "Android")
  }
}
