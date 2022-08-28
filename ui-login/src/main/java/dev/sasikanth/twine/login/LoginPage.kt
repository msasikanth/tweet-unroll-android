package dev.sasikanth.twine.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
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
  Box(
    modifier = modifier.fillMaxSize()
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

    BackgroundPattern()

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

@Composable
private fun BackgroundPattern(
  modifier: Modifier = Modifier
) {
  val backgroundColor = TwineTheme
    .colorScheme
    .surfaceColorAtElevation(ElevationTokens.Level1)

  val backgroundPatternColor = TwineTheme
    .colorScheme
    .surfaceColorAtElevation(ElevationTokens.Level3)

  val lineWidthPx = with(LocalDensity.current) { 2.dp.toPx() }
  val lineToGapRatio = 0.5f
  val lineGapWidthPx = lineWidthPx / lineToGapRatio
  val brushSizePx = lineGapWidthPx + lineWidthPx
  val stripeStart = lineGapWidthPx / brushSizePx


  val infiniteTransition = rememberInfiniteTransition()
  val offset by infiniteTransition
    .animateFloat(
      initialValue = 0f,
      targetValue = brushSizePx,
      animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing))
    )

  Canvas(
    modifier = modifier
      .fillMaxSize()
      .background(backgroundColor),
    onDraw = {
      val brush = Brush.linearGradient(
        stripeStart to Color.Transparent,
        stripeStart to backgroundPatternColor,
        start = Offset(offset, offset),
        end = Offset(offset + brushSizePx, offset + brushSizePx),
        tileMode = TileMode.Repeated,
      )

      drawRect(brush)
    }
  )
}

@Preview
@Composable
private fun BackgroundPatternPreview() {
  TwineTheme {
    BackgroundPattern()
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
