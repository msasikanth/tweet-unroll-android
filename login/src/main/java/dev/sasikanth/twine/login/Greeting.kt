package dev.sasikanth.twine.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.theme.TwineTheme


@Composable
internal fun Greeting(
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.wrapContentSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = stringResource(id = R.string.login_greeting_welcome),
      style = TwineTheme.typography.displayMedium,
      color = TwineTheme.colorScheme.onSurface,
    )

    Text(
      text = stringResource(id = R.string.login_greeting_app_name),
      style = TwineTheme.typography.displayMedium,
      color = TwineTheme.colorScheme.primary,
    )
  }
}

@Preview(
  showBackground = true
)
@Composable
private fun GreetingPreview() {
  TwineTheme {
    Greeting(modifier = Modifier.padding(16.dp))
  }
}
