package dev.sasikanth.twine.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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

private val CONTAINER_CORNER_RADIUS = 20.dp
private val CONTAINER_PADDING = 4.dp
private val CONTENT_PADDING = 16.dp

private class AlertToggleRippleTheme(
  private val rippleColor: Color
) : RippleTheme {

  @Composable
  override fun defaultColor(): Color {
    return rippleColor
  }

  @Composable
  override fun rippleAlpha(): RippleAlpha {
    return RippleAlpha(
      TwineTheme.opacity.dragged,
      TwineTheme.opacity.focused,
      TwineTheme.opacity.hovered,
      TwineTheme.opacity.pressed
    )
  }
}

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
  val rippleTheme = AlertToggleRippleTheme(rippleColor)

  CompositionLocalProvider(
    LocalRippleTheme provides rippleTheme
  ) {
    Box(
      modifier = modifier
        .wrapContentHeight()
        .wrapContentWidth()
        .background(
          color = containerColor,
          shape = RoundedCornerShape(CONTAINER_CORNER_RADIUS)
        )
        .padding(CONTAINER_PADDING)
        .clip(TwineTheme.shapes.large)
        .toggleable(
          value = isToggled,
          onValueChange = onAlertToggled
        ),
    ) {
      Content(isToggled = isToggled)
    }
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
      .padding(CONTENT_PADDING)
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
fun AlertTogglePreview_isToggled() {
  TwineTheme {
    AlertToggle(
      isToggled = true
    ) {

    }
  }
}

@Preview
@Composable
fun AlertTogglePreview_isUnToggled() {
  TwineTheme {
    AlertToggle(
      isToggled = false
    ) {

    }
  }
}
