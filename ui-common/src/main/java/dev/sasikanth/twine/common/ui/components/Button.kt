package dev.sasikanth.twine.common.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.contentColorFor

private object ButtonDefaults {
  val MinHeight = 48.dp
  val HorizontalPaddingStart = 16.dp
  val HorizontalPaddingEnd = 24.dp
  val HorizontalSpacing = 8.dp
  val IconSize = 20.dp
}

/**
 * Custom ripple theme that uses onBrand color for ripple
 */
private object ButtonRippleTheme : RippleTheme {

  @Composable
  override fun defaultColor(): Color {
    return TwineTheme.colorScheme.onBrand
  }

  @Composable
  override fun rippleAlpha(): RippleAlpha {
    return RippleAlpha(
      draggedAlpha = TwineTheme.opacity.dragged,
      hoveredAlpha = TwineTheme.opacity.hovered,
      pressedAlpha = TwineTheme.opacity.pressed,
      focusedAlpha = TwineTheme.opacity.focused
    )
  }
}

/**
 * Standard filled button that supports icon and text
 *
 * @param text: set button label
 * @param icon: set button icon
 * @param onClick: called when button is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwineButton(
  modifier: Modifier = Modifier,
  text: String,
  icon: Painter? = null,
  onClick: () -> Unit
) {
  CompositionLocalProvider(
    LocalRippleTheme provides ButtonRippleTheme
  ) {
    val containerColor = TwineTheme.colorScheme.brand

    Surface(
      onClick = onClick,
      modifier = modifier
        .defaultMinSize(
          minHeight = ButtonDefaults.MinHeight
        ),
      shape = TwineTheme.shapes.medium,
      color = containerColor,
      contentColor = contentColorFor(backgroundColor = containerColor)
    ) {
      Row(
        modifier = Modifier
          .padding(
            start = ButtonDefaults.HorizontalPaddingStart,
            end = ButtonDefaults.HorizontalPaddingEnd
          ),
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (icon != null) {
          Icon(
            modifier = Modifier
              .size(ButtonDefaults.IconSize)
              .testTag("TwineButton:Icon"),
            painter = icon,
            contentDescription = null
          )
        }

        Spacer(
          modifier = Modifier
            .width(ButtonDefaults.HorizontalSpacing)
        )

        Text(
          modifier = Modifier
            .testTag("TwineButton:Label"),
          text = text,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = TwineTheme.typography.labelLarge
        )
      }
    }
  }
}

@Preview
@Composable
private fun ButtonPreview() {
  TwineTheme {
    TwineButton(
      text = "Sign in with Twitter",
      onClick = {
        // Handle clicks
      }
    )
  }
}

@Preview
@Composable
private fun ButtonWithIconPreview() {
  TwineTheme {
    TwineButton(
      text = "Create",
      icon = rememberVectorPainter(image = Icons.Default.Add),
      onClick = {
        // Handle clicks
      }
    )
  }
}
