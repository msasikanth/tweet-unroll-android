package dev.sasikanth.twine.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.R
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation

private object AlertToggleDefaults {
  val containerCornerRadius = 20.dp
  val containerPadding = 4.dp
  val contentPadding = 16.dp
}

/**
 * Icon button that is toggleable.
 *
 * Indicates whether alerts are enabled or not
 *
 * @param isToggled: indicates whether alerts are on or off
 * @param onAlertToggled: callback to be invoked with new state when button is clicked
 * to request state change
 */
@Composable
fun AlertToggle(
  modifier: Modifier = Modifier,
  isToggled: Boolean,
  onAlertToggled: (Boolean) -> Unit
) {
  val containerColor = if (isToggled) {
    TwineTheme.colorScheme.surfaceColorAtElevation(ElevationTokens.Level5)
  } else {
    Color.Unspecified
  }

  val rippleColor = if (isToggled) {
    TwineTheme.colorScheme.onBrand
  } else {
    TwineTheme.colorScheme.primary
  }

  Box(
    modifier = modifier
      .wrapContentHeight()
      .wrapContentWidth()
      .background(
        color = containerColor,
        shape = RoundedCornerShape(AlertToggleDefaults.containerCornerRadius)
      )
      .padding(AlertToggleDefaults.containerPadding)
      .clip(TwineTheme.shapes.large)
      .toggleable(
        value = isToggled,
        onValueChange = onAlertToggled,
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(color = rippleColor)
      ),
  ) {
    Content(isToggled = isToggled)
  }
}

@Composable
private fun Content(
  modifier: Modifier = Modifier,
  isToggled: Boolean
) {
  val contentColor = if (isToggled) {
    TwineTheme.colorScheme.brand
  } else {
    TwineTheme
      .colorScheme
      .primary
      .copy(
        alpha = TwineTheme.opacity.hovered
      )
  }

  val onContentColor = if (isToggled) {
    TwineTheme.colorScheme.onBrand
  } else {
    TwineTheme.colorScheme.onPrimaryContainer
  }

  val iconResource = if (isToggled) {
    R.drawable.ic_alert_on
  } else {
    R.drawable.ic_alert_off
  }

  val contentDescription = if (isToggled) {
    R.string.cd_alert_toggle_on
  } else {
    R.string.cd_alert_toggle_off
  }

  Box(
    modifier = modifier
      .background(
        color = contentColor,
        shape = TwineTheme.shapes.medium
      )
      .padding(AlertToggleDefaults.contentPadding)
  ) {
    Icon(
      painter = painterResource(id = iconResource),
      contentDescription = stringResource(id = contentDescription),
      tint = onContentColor
    )
  }
}

@Preview
@Composable
private fun AlertTogglePreview_isToggled() {
  TwineTheme {
    AlertToggle(
      isToggled = true
    ) {

    }
  }
}

@Preview
@Composable
private fun AlertTogglePreview_isUnToggled() {
  TwineTheme {
    AlertToggle(
      isToggled = false
    ) {

    }
  }
}
