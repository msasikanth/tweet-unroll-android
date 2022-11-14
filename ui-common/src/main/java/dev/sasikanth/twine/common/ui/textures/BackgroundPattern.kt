package dev.sasikanth.twine.common.ui.textures

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation

@Composable
fun BackgroundPattern(
  modifier: Modifier = Modifier,
  animationSpec: InfiniteRepeatableSpec<Float>
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
      animationSpec = animationSpec
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
    BackgroundPattern(
      animationSpec = infiniteRepeatable(
        tween(
          1000,
          easing = LinearEasing
        )
      )
    )
  }
}
