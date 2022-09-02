package dev.sasikanth.twine.common.ui.anim

import androidx.compose.animation.core.Spring

internal object TwineSpring {

  const val StiffnessMedium = (Spring.StiffnessMedium + Spring.StiffnessMediumLow) / 2f

  const val DampingRatioHigh = 0.35f
}
