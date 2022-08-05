package dev.sasikanth.twine.common.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

data class Opacity(
  val hovered: Float = 0.08f,
  val focused: Float = 0.12f,
  val pressed: Float = 0.16f,
  val dragged: Float = 0.24f,
  val bgDisabled: Float = 0.12f,
  val fgDisabled: Float = 0.38f
)

internal val LocalOpacity = staticCompositionLocalOf { Opacity() }
