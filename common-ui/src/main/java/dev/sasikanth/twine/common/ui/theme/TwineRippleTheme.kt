package dev.sasikanth.twine.common.ui.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal object TwineRippleTheme : RippleTheme {

  @Composable
  override fun defaultColor(): Color {
    return TwineTheme.colorScheme.primary
  }

  @Composable
  override fun rippleAlpha(): RippleAlpha {
    return RippleAlpha(
      draggedAlpha = TwineTheme.opacity.dragged,
      focusedAlpha = TwineTheme.opacity.focused,
      hoveredAlpha = TwineTheme.opacity.hovered,
      pressedAlpha = TwineTheme.opacity.pressed
    )
  }
}
