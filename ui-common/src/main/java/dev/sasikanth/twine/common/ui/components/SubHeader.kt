package dev.sasikanth.twine.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.theme.TwineTheme

@Composable
fun SubHeader(
  modifier: Modifier = Modifier,
  text: String
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        horizontal = 16.dp,
        vertical = 32.dp
      )
  ) {
    Text(
      text = text,
      style = TwineTheme.typography.titleMedium,
      color = TwineTheme.colorScheme.onSurface
    )
  }
}

@Preview
@Composable
private fun SubHeaderPreview() {
  TwineTheme {
    SubHeader(text = "Title")
  }
}