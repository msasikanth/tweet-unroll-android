package dev.sasikanth.twine.common.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.R
import dev.sasikanth.twine.common.ui.theme.TwineTheme

internal object CheckBoxDefaults {
  val CheckBoxUncheckedSize = 18.dp
  val CheckBoxCheckedBoxSize = 20.dp
  val CheckBoxCheckedCornerSize = 5.dp
  val LabelEndSpacing = 20.dp

  const val BoxColorChangeDuration = 250
  const val BoxSizeChangeDuration = 75
}

/**
 * CheckBox can we used to turn an option on or off, or used for selecting one or more items
 *
 * @param checked: whether this checkbox is checked or unchecked
 * @param enabled: controls the enabled state of this checkbox. When false, checkbox will not
 * respond to user input, and visual appearance changes to indicate it is disabled
 * @param label: Optional label to be show for this checkbox
 * @param onValueChange: Callback to notify state when the checkbox state changes
 */
@Composable
fun CheckBox(
  modifier: Modifier = Modifier,
  checked: Boolean,
  enabled: Boolean = true,
  label: String? = null,
  onValueChange: (Boolean) -> Unit,
) {
  Row(
    modifier = modifier
      .clip(TwineTheme.shapes.medium)
      .toggleable(
        value = checked,
        role = Role.Checkbox,
        enabled = enabled,
        onValueChange = onValueChange,
      )
      .testTag("CheckBox"),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    CheckBoxBackgroundAndIcon(
      checked = checked,
      enabled = enabled
    )

    if (label != null) {
      Label(label = label)
    }
  }
}

@Composable
private fun Label(
  modifier: Modifier = Modifier,
  label: String,
) {
  Row(
    modifier = modifier.testTag("CheckBox:Label")
  ) {
    Text(
      text = label,
      color = TwineTheme.colorScheme.onSurface,
      style = TwineTheme.typography.bodyMedium,
    )

    Spacer(modifier = Modifier.width(CheckBoxDefaults.LabelEndSpacing))
  }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun CheckBoxBackgroundAndIcon(
  modifier: Modifier = Modifier,
  checked: Boolean,
  enabled: Boolean,
) {
  val boxColor by boxColor(
    checked = checked,
    enabled = enabled
  )

  val iconTint = if (enabled) {
    when {
      checked -> TwineTheme.colorScheme.onBrand
      else -> TwineTheme.colorScheme.onSurfaceVariant
    }
  } else {
    TwineTheme.colorScheme.onSurface.copy(alpha = TwineTheme.opacity.fgDisabled)
  }

  val boxShape = if (checked) {
    RoundedCornerShape(CheckBoxDefaults.CheckBoxCheckedCornerSize)
  } else {
    TwineTheme.shapes.extraSmall
  }

  val checkBoxSize by boxSize(checked = checked)

  val animatedIcon = AnimatedImageVector.animatedVectorResource(R.drawable.avd_checkbox)

  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .size(checkBoxSize)
        .clip(boxShape)
        .background(color = boxColor)
    )

    // AVD already has padding for the icon, so try to place this inside the background container
    // creates extra padding and makes the check/unchecked icon look really small. To prevent that
    // we are overlapping the drawable on top of the background.
    Icon(
      painter = rememberAnimatedVectorPainter(animatedIcon, checked),
      contentDescription = null,
      tint = iconTint
    )
  }
}

@Composable
private fun boxColor(
  enabled: Boolean,
  checked: Boolean,
): State<Color> {
  val boxColor = if (enabled) {
    when {
      checked -> TwineTheme.colorScheme.brand
      else -> TwineTheme.colorScheme.primary.copy(alpha = TwineTheme.opacity.pressed)
    }
  } else {
    TwineTheme.colorScheme.onSurface.copy(alpha = TwineTheme.opacity.bgDisabled)
  }

  return animateColorAsState(
    targetValue = boxColor,
    animationSpec = tween(durationMillis = CheckBoxDefaults.BoxColorChangeDuration)
  )
}

@Composable
private fun boxSize(
  checked: Boolean,
): State<Dp> {
  val boxSize = if (checked) {
    CheckBoxDefaults.CheckBoxCheckedBoxSize
  } else {
    CheckBoxDefaults.CheckBoxUncheckedSize
  }

  return animateDpAsState(
    targetValue = boxSize,
    animationSpec = tween(CheckBoxDefaults.BoxSizeChangeDuration)
  )
}

@Preview
@Composable
fun CheckBoxPreview_Unchecked() {
  TwineTheme {
    CheckBox(
      checked = false,
      onValueChange = {
        // Handle value changes
      }
    )
  }
}

@Preview
@Composable
fun CheckBoxPreview_Checked() {
  TwineTheme {
    CheckBox(
      checked = true,
      onValueChange = {
        // Handle value changes
      }
    )
  }
}

@Preview
@Composable
fun CheckBoxWithLabelPreview() {
  TwineTheme {
    CheckBox(
      checked = false,
      label = "Enable?",
      onValueChange = {
        // Handle value changes
      }
    )
  }
}

@Preview
@Composable
fun CheckBoxPreview_Unchecked_Disabled() {
  TwineTheme(useDynamicColors = false) {
    CheckBox(
      checked = false,
      enabled = false,
      onValueChange = {
        // Handle value changes
      }
    )
  }
}

@Preview
@Composable
fun CheckBoxPreview_Checked_Disabled() {
  TwineTheme(useDynamicColors = false) {
    CheckBox(
      checked = true,
      enabled = false,
      onValueChange = {
        // Handle value changes
      }
    )
  }
}
