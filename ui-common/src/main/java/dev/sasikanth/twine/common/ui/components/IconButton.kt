package dev.sasikanth.twine.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.components.IconButtonDefaults.IconButtonSize
import dev.sasikanth.twine.common.ui.components.IconButtonDefaults.IconSize
import dev.sasikanth.twine.common.ui.components.IconButtonDefaults.IconSizeSmall
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.contentColorFor

/**
 * IconButton is a clickable icon, used to represent actions. An IconButton has an overall minimum
 * touch target size of 48 x 48dp, to meet accessibility guidelines. [content] is centered
 * inside the IconButton.
 *
 * @param onClick the lambda to be invoked when this icon is pressed
 * @param modifier optional [Modifier] for this IconButton
 * @param content the content (icon) to be drawn inside the IconButton. This is typically an
 * [Icon].
 *
 * TODO: Change ripple color for background filled icon button
 */
@Composable
fun IconButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  backgroundColor: Color = Color.Unspecified,
  contentColor: Color = TwineTheme.colorScheme.contentColorFor(backgroundColor),
  content: @Composable () -> Unit
) {
  // TODO: Think of a better way to configure it. Param? Separate filled icon button component?
  val rippleColor = if (backgroundColor == TwineTheme.colorScheme.brand) {
    TwineTheme.colorScheme.onBrand
  } else {
    TwineTheme.colorScheme.primary
  }

  Box(
    modifier = modifier
      .clip(TwineTheme.shapes.medium)
      .background(backgroundColor)
      .size(IconButtonSize)
      .clickable(
        onClick = onClick,
        enabled = true,
        role = Role.Button,
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(color = rippleColor)
      ),
    contentAlignment = Alignment.Center
  ) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
      val iconModifier = if (backgroundColor != Color.Unspecified) {
        Modifier.size(IconSizeSmall)
      } else {
        Modifier.size(IconSize)
      }

      Box(iconModifier.testTag("Icon")) {
        content.invoke()
      }
    }
  }
}

internal object IconButtonDefaults {
  val IconButtonSize = 48.dp

  val IconSize = 24.dp
  val IconSizeSmall = 18.dp
}

@Preview
@Composable
private fun IconButtonPreview() {
  TwineTheme {
    IconButton(onClick = {
      // Handle clicks
    }) {
      Icon(
        imageVector = Icons.Filled.ArrowBack,
        contentDescription = null
      )
    }
  }
}

@Preview
@Composable
private fun IconButtonPreview_Filled() {
  TwineTheme {
    IconButton(
      backgroundColor = TwineTheme.colorScheme.brand,
      onClick = {
        // Handle clicks
      }
    ) {
      Icon(
        imageVector = Icons.Filled.ArrowBack,
        contentDescription = null
      )
    }
  }
}
