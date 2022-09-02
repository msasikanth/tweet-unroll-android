package dev.sasikanth.twine.common.ui.components

import android.graphics.Path
import androidx.annotation.FloatRange
import androidx.compose.animation.core.Spring
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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.R
import dev.sasikanth.twine.common.ui.theme.TwineTheme
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
  modifier: Modifier = Modifier,
  checked: Boolean,
  enabled: Boolean = true,
  onValueChange: (Boolean) -> Unit,
) {
  val rippleColor = if (checked) {
    TwineTheme.colorScheme.onBrand
  } else {
    TwineTheme.colorScheme.primary
  }

  val trackWidth = SwitchDefaults.TrackWidth
  val trackHeight = SwitchDefaults.TrackHeight

  Box(
    modifier = modifier
      .width(trackWidth)
      .height(trackHeight)
      .padding(vertical = SwitchDefaults.VerticalPadding)
      .clip(SwitchDefaults.TrackShape)
      .toggleable(
        value = checked,
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(color = rippleColor),
        role = Role.Switch,
        onValueChange = onValueChange,
      )
      .testTag("Switch")
  ) {
    val transition = updateTransition(
      targetState = checked,
      label = "Switch"
    )

    val thumbSize = SwitchDefaults.ThumbSize
    val thumbPadding = SwitchDefaults.ThumbPadding

    val density = LocalDensity.current

    val trackWidthPx = with(density) { trackWidth.toPx() }
    val thumbSizePx = with(density) { thumbSize.toPx() }
    val thumbPaddingPx = with(density) { thumbPadding.toPx() }

    val thumbCenterPx = thumbSizePx / 2f

    @Suppress("UnnecessaryVariable")
    val thumbOffsetMin = thumbPaddingPx
    val thumbOffsetMax = (trackWidthPx - thumbSizePx) - thumbPaddingPx

    val revealOffsetMin = thumbPaddingPx + thumbCenterPx
    val revealOffsetMax = (trackWidthPx - thumbCenterPx) - thumbPaddingPx

    val transitionStiffness = 900f
    val thumbOffset by transition.animateFloat(
      label = "ThumbOffset",
      transitionSpec = {
        spring(stiffness = transitionStiffness)
      }
    ) {
      if (it) thumbOffsetMax else thumbOffsetMin
    }

    val revealOffset by transition.animateFloat(
      label = "ThumbOffset",
      transitionSpec = {
        spring(stiffness = transitionStiffness)
      }
    ) {
      if (it) revealOffsetMax else revealOffsetMin
    }

    val revealProgress by transition.animateFloat(
      label = "RevealProgress",
      transitionSpec = {
        spring(stiffness = transitionStiffness)
      }
    ) {
      if (it) 1f else 0f
    }

    val uncheckedTrackColor = uncheckedTrackColor(enabled = enabled)
    val uncheckedThumbColor = uncheckedThumbColor(enabled = enabled)
    val uncheckedIconColor = uncheckedIconColor(enabled = enabled)

    SwitchImpl(
      checked = checked,
      trackColor = uncheckedTrackColor,
      thumbColor = uncheckedThumbColor,
      iconTint = uncheckedIconColor,
      thumbSize = thumbSize,
      thumbOffset = thumbOffset.roundToInt()
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
      SwitchImpl(
        modifier = Modifier.circularClip(revealProgress, revealOffset),
        checked = checked,
        trackColor = TwineTheme.colorScheme.brand,
        thumbColor = TwineTheme.colorScheme.onBrand,
        iconTint = TwineTheme.colorScheme.brand,
        thumbSize = thumbSize,
        thumbOffset = thumbOffset.roundToInt()
      )
    }
  }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun SwitchImpl(
  modifier: Modifier = Modifier,
  checked: Boolean,
  trackColor: Color,
  thumbColor: Color,
  iconTint: Color,
  thumbSize: Dp,
  thumbOffset: Int
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(trackColor)
  ) {
    Box(
      modifier = Modifier
        .align(Alignment.CenterStart)
        .offset { IntOffset(thumbOffset, 0) }
        .requiredSize(thumbSize)
        .background(
          color = thumbColor,
          shape = SwitchDefaults.ThumbShape
        ),
      contentAlignment = Alignment.Center,
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

@Composable
private fun uncheckedTrackColor(enabled: Boolean): Color {
  return if (enabled)
    TwineTheme.colorScheme.surfaceVariant
  else
    TwineTheme.colorScheme.onSurface.copy(
      alpha = TwineTheme.opacity.bgDisabled
    )
}

@Composable
private fun uncheckedThumbColor(enabled: Boolean): Color {
  return if (enabled)
    TwineTheme.colorScheme.onSurfaceVariant
  else
    TwineTheme.colorScheme.onSurface.copy(
      alpha = TwineTheme.opacity.bgDisabled
    )
}

@Composable
private fun uncheckedIconColor(enabled: Boolean): Color {
  return if (enabled)
    TwineTheme.colorScheme.surfaceVariant
  else
    TwineTheme.colorScheme.surface
}

private fun Modifier.circularClip(
  @FloatRange(from = 0.0, to = 1.0) progress: Float,
  offset: Float
) = clip(CircularRevealShape(progress, offset))

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

    return Outline.Generic(Path().apply {
      addCircle(
        offset,
        yCenter,
        radius,
        Path.Direction.CW
      )
    }.asComposePath())
  }
}

internal object SwitchDefaults {
  val TrackWidth = 64.dp
  val TrackHeight = 48.dp

  val VerticalPadding = 4.dp
  val ThumbPadding = 6.dp

  val ThumbSize = 28.dp

  val TrackShape = Shapes.Full
  val ThumbShape = Shapes.Full

  val RevealRadius = 44.dp
}

@Preview
@Composable
private fun SwitchPreview_Unchecked() {
  TwineTheme {
    Switch(
      checked = false,
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
      enabled = false,
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
      checked = true,
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
      enabled = false,
    ) {
      // Handle value changes
    }
  }
}
