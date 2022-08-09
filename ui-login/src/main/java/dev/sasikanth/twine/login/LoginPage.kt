package dev.sasikanth.twine.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun LoginPage(
  modifier: Modifier = Modifier,
  viewModel: LoginViewModel = hiltViewModel(),
  navigateToHome: () -> Unit,
) {
  val backgroundColor = TwineTheme
    .colorScheme
    .surfaceColorAtElevation(ElevationTokens.Level1)

  val backgroundPatternColor = TwineTheme
    .colorScheme
    .surfaceColorAtElevation(ElevationTokens.Level3)

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(backgroundColor)
      .drawWithCache {
        onDrawBehind {
          backgroundPattern(backgroundPatternColor)
        }
      }
  ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
      contract = viewModel.buildTwineLoginActivityResult()!!,
      onResult = viewModel::onLoginResult
    )

    if (uiState.isUserLoggedIn) {
      LaunchedEffect(Unit) {
        navigateToHome()
      }
    }

    if (!uiState.isCheckingLoginStatus && !uiState.isUserLoggedIn) {
      LoginPageContent {
        launcher.launch(Unit)
      }
    }
  }
}

@Composable
private fun LoginPageContent(
  modifier: Modifier = Modifier,
  onLoginClick: () -> Unit,
) {
  Column(
    modifier = modifier
      .systemBarsPadding()
      .fillMaxSize()
      .padding(40.dp),
    verticalArrangement = Arrangement.SpaceBetween,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Greeting()
    SignInButtonContainer(onLoginClick = onLoginClick)
  }
}

private fun DrawScope.backgroundPattern(
  backgroundPatternColor: Color,
) {
  val lineWidthPx = 2.dp.toPx()
  val lineToGapRatio = 0.5f
  val lineGapWidthPx = lineWidthPx / lineToGapRatio
  val brushSizePx = lineGapWidthPx + lineWidthPx
  val stripeStart = lineGapWidthPx / brushSizePx

  val brush = Brush.linearGradient(
    stripeStart to Color.Transparent,
    stripeStart to backgroundPatternColor,
    start = Offset(0f, 0f),
    end = Offset(brushSizePx, brushSizePx),
    tileMode = TileMode.Repeated,
  )

  drawRect(brush)
}

@Preview
@Composable
private fun BackgroundPatternPreview() {
  TwineTheme {
    val backgroundColor = TwineTheme
      .colorScheme
      .surfaceColorAtElevation(ElevationTokens.Level1)

    val backgroundPatternColor = TwineTheme
      .colorScheme
      .surfaceColorAtElevation(ElevationTokens.Level3)

    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)
        .drawBehind {
          backgroundPattern(backgroundPatternColor)
        }
    )
  }
}

@Preview(
  showBackground = true
)
@Composable
private fun LoginPagePreview() {
  TwineTheme {
    Box {
      LoginPageContent {
        // Handle login button clicks
      }
    }
  }
}
