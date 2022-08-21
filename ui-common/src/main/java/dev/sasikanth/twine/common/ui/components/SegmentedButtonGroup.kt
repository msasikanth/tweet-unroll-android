package dev.sasikanth.twine.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation

data class SegmentedButtonItem(val label: String)

/**
 * Container for set of related and toggleable [SegmentedButton]'s
 *
 * @param items: list of [SegmentedButtonItem]'s to render
 * @param selectedItem: selected item to indicate in the button group
 * @param onItemChange: callback to notify selected item changes
 */
@Composable
fun SegmentedButtonGroup(
  modifier: Modifier = Modifier,
  items: List<SegmentedButtonItem>,
  selectedItem: SegmentedButtonItem,
  onItemChange: (SegmentedButtonItem) -> Unit
) {
  Row(
    modifier = modifier
      .testTag("SegmentedButtonGroup"),
    horizontalArrangement = Arrangement
      .spacedBy(SegmentedButtonGroupDefaults.ButtonSpacing)
  ) {
    items.forEach { item ->
      SegmentedButton(
        modifier = Modifier.weight(1f),
        label = item.label,
        checked = selectedItem == item
      ) {
        onItemChange(item)
      }
    }
  }
}

@Composable
private fun SegmentedButton(
  modifier: Modifier = Modifier,
  label: String,
  checked: Boolean,
  onClick: () -> Unit
) {
  val buttonBackground = if (checked) {
    TwineTheme.colorScheme.secondaryContainer
  } else {
    TwineTheme.colorScheme.surfaceColorAtElevation(ElevationTokens.Level2)
  }

  val labelColor = if (checked) {
    TwineTheme.colorScheme.onSecondaryContainer
  } else {
    TwineTheme.colorScheme.onSurface
  }

  val rippleColor = if (checked) {
    TwineTheme.colorScheme.secondary
  } else {
    TwineTheme.colorScheme.primary
  }
  val rippleTheme = SegmentedButtonGroupRippleTheme(rippleColor)

  CompositionLocalProvider(
    LocalRippleTheme provides rippleTheme
  ) {
    Box(
      modifier = modifier
        .defaultMinSize(minHeight = SegmentedButtonGroupDefaults.ButtonHeight)
        .clip(TwineTheme.shapes.medium)
        .background(color = buttonBackground)
        .toggleable(
          value = checked,
          onValueChange = {
            onClick()
          }
        )
        .padding(SegmentedButtonGroupDefaults.LabelPadding)
        .testTag("SegmentedButton"),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = label,
        color = labelColor,
        style = TwineTheme.typography.labelLarge,
      )
    }
  }
}

internal object SegmentedButtonGroupDefaults {
  val ButtonSpacing = 8.dp
  val LabelPadding = 12.dp

  val ButtonHeight = 48.dp
}

internal class SegmentedButtonGroupRippleTheme(
  private val rippleColor: Color
) : RippleTheme {

  @Composable
  override fun defaultColor(): Color {
    return rippleColor
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

@Preview
@Composable
private fun SegmentedButtonPreview_Unchecked() {
  TwineTheme {
    SegmentedButton(label = "Light", checked = false) {
      // Handle clicks
    }
  }
}

@Preview
@Composable
private fun SegmentedButtonPreview_Checked() {
  TwineTheme {
    SegmentedButton(label = "Light", checked = true) {
      // Handle clicks
    }
  }
}

@Preview
@Composable
private fun SegmentedButtonGroupPreview() {
  val light = SegmentedButtonItem(label = "Light")
  val dark = SegmentedButtonItem(label = "Dark")
  val system = SegmentedButtonItem(label = "System")

  val items = listOf(light, dark, system)

  TwineTheme {
    SegmentedButtonGroup(
      items = items,
      selectedItem = dark,
      onItemChange = {
        // Handle item changes
      }
    )
  }
}
