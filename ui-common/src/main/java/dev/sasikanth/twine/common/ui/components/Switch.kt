package dev.sasikanth.twine.common.ui.components

import android.graphics.Path
import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.R
import dev.sasikanth.twine.common.ui.anim.TwineSpring
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation
import kotlin.math.roundToInt

/**
 * Switch can be used to toggle between on and off states.
 *
 * @param checked: whether this switch is checked or not
 * @param enabled: controls the enabled state of this switch. When false, switch will not
 * respond to user input, and visual appearance changes to indicate it is disabled
 * @param onValueChange: Callback to notify state when the switch state changes
 */
@Composable
fun Switch(
  checked: Boolean,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  onValueChange: (Boolean) -> Unit
) {
  val rippleColor = if (checked) {
    TwineTheme.colorScheme.onBrand
  } else {
    TwineTheme.colorScheme.primary
  }

  SwitchContainer(
    modifier = modifier
      .width(SwitchDefaults.TrackWidth)
      .height(SwitchDefaults.TrackHeight)
      .padding(vertical = SwitchDefaults.VerticalPadding)
      .clip(SwitchDefaults.TrackShape)
      .toggleable(
        value = checked,
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(color = rippleColor),
        role = Role.Switch,
        onValueChange = onValueChange
      )
      .testTag("Switch"),
    checked = checked,
    enabled = enabled
  )
}

@Composable
private fun SwitchContainer(
  checked: Boolean,
  enabled: Boolean,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier) {
    val transition = updateTransition(
      targetState = checked,
      label = "Switch"
    )

    val thumbSize = SwitchDefaults.ThumbSize
    val thumbPadding = SwitchDefaults.ThumbPadding

    val density = LocalDensity.current

    val trackWidthPx = with(density) { SwitchDefaults.TrackWidth.toPx() }
    val thumbSizePx = with(density) { thumbSize.toPx() }
    val thumbPaddingPx = with(density) { thumbPadding.toPx() }

    val thumbCenterPx = thumbSizePx / 2f

    @Suppress("UnnecessaryVariable")
    val thumbOffsetMin = thumbPaddingPx
    val thumbOffsetMax = (trackWidthPx - thumbSizePx) - thumbPaddingPx

    val revealOffsetMin = thumbPaddingPx + thumbCenterPx
    val revealOffsetMax = (trackWidthPx - thumbCenterPx) - thumbPaddingPx

    val thumbOffset by transition.animateFloat(
      label = "ThumbOffset",
      transitionSpec = {
        spring(stiffness = TwineSpring.StiffnessMedium)
      }
    ) {
      if (it) thumbOffsetMax else thumbOffsetMin
    }

    val revealOffset by transition.animateFloat(
      label = "ThumbOffset",
      transitionSpec = {
        spring(stiffness = TwineSpring.StiffnessMedium)
      }
    ) {
      if (it) revealOffsetMax else revealOffsetMin
    }

    val revealProgress by transition.animateFloat(
      label = "RevealProgress",
      transitionSpec = {
        spring(stiffness = TwineSpring.StiffnessMedium)
      }
    ) {
      if (it) 1f else 0f
    }

    SwitchOff(
      checked = checked,
      enabled = enabled,
      thumbOffsetProvider = { thumbOffset }
    )

    /**
     * When a switch is enabled and turned on, we animate the checked
     * state using a circular reveal. Colors used to indicate
     * checked switch are different from unchecked state.
     *
     * Since we want the content to have a "clipping" effect
     * as the reveal happens. We are placing the checked switch
     * above the unchecked switch and animate the clip using
     * circular reveal.
     */
    if (enabled) {
      SwitchOn(
        checked = checked,
        thumbOffsetProvider = { thumbOffset },
        revealProgressProvider = { revealProgress },
        revealOffsetProvider = { revealOffset }
      )
    }
  }
}

@Composable
private fun SwitchOff(
  checked: Boolean,
  enabled: Boolean,
  modifier: Modifier = Modifier,
  thumbOffsetProvider: () -> Float
) {
  val switchColors = SwitchDefaults.colors()
  val uncheckedTrackColor by switchColors.uncheckedTrackColor(enabled = enabled)
  val uncheckedThumbColor by switchColors.uncheckedThumbColor(enabled = enabled)
  val uncheckedIconColor by switchColors.uncheckedIconTint(enabled = enabled)

  SwitchImpl(
    modifier = modifier,
    checked = checked,
    trackColor = uncheckedTrackColor,
    thumbColor = uncheckedThumbColor,
    iconTint = uncheckedIconColor,
    thumbOffsetProvider = thumbOffsetProvider
  )
}

@Composable
fun SwitchOn(
  checked: Boolean,
  thumbOffsetProvider: () -> Float,
  revealProgressProvider: () -> Float,
  modifier: Modifier = Modifier,
  revealOffsetProvider: () -> Float
) {
  SwitchImpl(
    modifier = modifier
      .graphicsLayer {
        clip = true
        shape = CircularRevealShape(
          progress = revealProgressProvider(),
          offset = revealOffsetProvider()
        )
      },
    checked = checked,
    trackColor = TwineTheme.colorScheme.brand,
    thumbColor = TwineTheme.colorScheme.onBrand,
    iconTint = TwineTheme.colorScheme.brand,
    thumbOffsetProvider = thumbOffsetProvider
  )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun SwitchImpl(
  checked: Boolean,
  trackColor: Color,
  thumbColor: Color,
  iconTint: Color,
  modifier: Modifier = Modifier,
  thumbOffsetProvider: () -> Float
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(trackColor)
  ) {
    Box(
      modifier = Modifier
        .align(Alignment.CenterStart)
        .offset {
          IntOffset(
            x = thumbOffsetProvider().roundToInt(),
            y = 0
          )
        }
        .requiredSize(SwitchDefaults.ThumbSize)
        .background(
          color = thumbColor,
          shape = SwitchDefaults.ThumbShape
        ),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        modifier = Modifier.requiredSize(16.dp),
        painter = rememberAnimatedVectorPainter(
          animatedImageVector = AnimatedImageVector
            .animatedVectorResource(id = R.drawable.avd_switch),
          atEnd = checked
        ),
        contentDescription = null,
        tint = iconTint
      )
    }
  }
}

private class CircularRevealShape(
  @FloatRange(from = 0.0, to = 1.0) private val progress: Float,
  private val offset: Float
) : Shape {

  override fun createOutline(
    size: Size,
    layoutDirection: LayoutDirection,
    density: Density
  ): Outline {
    val radius = with(density) { SwitchDefaults.RevealRadius.toPx() } * progress

    val yCenter = size.height / 2

    return Outline.Generic(
      Path().apply {
        addCircle(
          offset,
          yCenter,
          radius,
          Path.Direction.CW
        )
      }.asComposePath()
    )
  }
}

private object SwitchDefaults {
  val TrackWidth = 64.dp
  val TrackHeight = 48.dp

  val VerticalPadding = 4.dp
  val ThumbPadding = 6.dp

  val ThumbSize = 28.dp

  val TrackShape = CircleShape
  val ThumbShape = CircleShape

  val RevealRadius = 44.dp

  @Composable
  fun colors() = SwitchColors()
}

private class SwitchColors {

  @Composable
  fun uncheckedTrackColor(enabled: Boolean): State<Color> {
    return rememberUpdatedState(
      if (enabled) {
        TwineTheme.colorScheme.surfaceColorAtElevation(ElevationTokens.Level5)
      } else {
        TwineTheme.colorScheme.onSurface.copy(
          alpha = TwineTheme.opacity.bgDisabled
        )
      }
    )
  }

  @Composable
  fun uncheckedThumbColor(enabled: Boolean): State<Color> {
    return rememberUpdatedState(
      if (enabled) {
        TwineTheme.colorScheme.onSurfaceVariant
      } else {
        TwineTheme.colorScheme.onSurface.copy(
          alpha = TwineTheme.opacity.bgDisabled
        )
      }
    )
  }

  @Composable
  fun uncheckedIconTint(enabled: Boolean): State<Color> {
    return rememberUpdatedState(
      if (enabled) {
        TwineTheme.colorScheme.surfaceVariant
      } else {
        TwineTheme.colorScheme.surface
      }
    )
  }
}

@Preview
@Composable
private fun SwitchPreview_Unchecked() {
  TwineTheme {
    Switch(
      checked = false
    ) {
      // Handle value changes
    }
  }
}

@Preview
@Composable
private fun SwitchPreview_Unchecked_Disabled() {
  TwineTheme {
    Switch(
      checked = false,
      enabled = false
    ) {
      // Handle value changes
    }
  }
}

@Preview
@Composable
private fun SwitchPreview_Checked() {
  TwineTheme {
    Switch(
      checked = true
    ) {
      // Handle value changes
    }
  }
}

@Preview
@Composable
private fun SwitchPreview_Checked_Disabled() {
  TwineTheme {
    Switch(
      checked = true,
      enabled = false
    ) {
      // Handle value changes
    }
  }
}
