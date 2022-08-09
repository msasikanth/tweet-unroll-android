package dev.sasikanth.twine.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.sasikanth.twine.common.ui.theme.TwineTheme

@Composable
fun HomePage(
  modifier: Modifier = Modifier
) {
  val backgroundColor = TwineTheme.colorScheme.background

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(backgroundColor)
  ) {
    // Add home page content
    Text(
      modifier = Modifier.align(Alignment.Center),
      text = "Work in progress!"
    )
  }
}
