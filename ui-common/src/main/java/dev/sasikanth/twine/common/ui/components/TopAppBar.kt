package dev.sasikanth.twine.common.ui.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.R
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.contentColorFor
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation

internal object AppBarDefaults {
  val AppBarContentSpacing = 24.dp
  val AppBarPadding = 24.dp
  val AppBarIconButtonSize = 40.dp
  val TitlePadding = 16.dp
  val NavigationIconEndPadding = 8.dp
  val ActionsStartPadding = 8.dp
}

/**
 * Top app bar displays information and actions related to current screen. Like navigation icon,
 * title, actions.
 *
 * @param title: title to be displayed in the app bar
 * @param navigationIcon: navigation icon to be displayed at start of app bar.
 * Use [AppBarBackButton] for displaying back button, or provide custom composable
 * @param actions: actions displayed at end of the app bar. Use [AppBarActionButton] for displaying
 * action buttons or provide a custom composable. Default layout for displaying actions is [Row],
 * so actions are played horizontally.
 * @param backgroundColor: container color of the app bar
 * @param contentColor: content color provided for title of the app bar
 */
@Composable
fun TopAppBar(
  title: String,
  modifier: Modifier = Modifier,
  navigationIcon: @Composable (() -> Unit)? = null,
  actions: @Composable (RowScope.() -> Unit)? = null,
  backgroundColor: Color = TwineTheme.colorScheme.surface,
  contentColor: Color = TwineTheme.colorScheme.contentColorFor(backgroundColor)
) {
  val configuration = LocalConfiguration.current
  val insetsModifier = when (configuration.orientation) {
    Configuration.ORIENTATION_LANDSCAPE -> modifier.systemBarsPadding()
    Configuration.ORIENTATION_PORTRAIT -> modifier.statusBarsPadding()
    else -> modifier
  }

  Row(
    modifier = insetsModifier
      .fillMaxWidth()
      .wrapContentHeight()
      .requiredHeight(88.dp)
      .background(backgroundColor)
      .padding(AppBarDefaults.AppBarPadding),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (navigationIcon != null) {
      Row(
        modifier = Modifier.testTag("TopAppBar:NavigationIcon"),
        verticalAlignment = Alignment.CenterVertically
      ) {
        navigationIcon.invoke()
        Spacer(modifier = Modifier.width(AppBarDefaults.NavigationIconEndPadding))
      }
    }

    Row(
      modifier = Modifier
        .weight(1f)
        .padding(
          start = AppBarDefaults.TitlePadding,
          end = AppBarDefaults.TitlePadding
        )
    ) {
      Text(
        text = title,
        style = TwineTheme.typography.headlineMedium,
        color = contentColor
      )
    }

    if (actions != null) {
      Row(
        modifier = Modifier
          .fillMaxHeight()
          .testTag("TopAppBar:Actions"),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Spacer(modifier = Modifier.width(AppBarDefaults.ActionsStartPadding))
        actions.invoke(this)
      }
    }
  }
}

/**
 * Icon button with brand color background and onBrand icon tint
 */
@Composable
fun AppBarBackButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  AppBarIconButton(
    modifier = modifier,
    icon = R.drawable.ic_arrow_back,
    backgroundColor = TwineTheme.colorScheme.brand,
    rippleColor = TwineTheme.colorScheme.onBrand,
    onClick = onClick
  )
}

@Composable
fun AppBarActionButton(
  @DrawableRes icon: Int,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  val backgroundColor = TwineTheme
    .colorScheme
    .surfaceColorAtElevation(ElevationTokens.Level2)

  AppBarIconButton(
    modifier = modifier,
    icon = icon,
    backgroundColor = backgroundColor,
    contentColor = TwineTheme.colorScheme.onPrimaryContainer,
    onClick = onClick
  )
}

@Composable
private fun AppBarIconButton(
  @DrawableRes icon: Int,
  backgroundColor: Color,
  modifier: Modifier = Modifier,
  contentColor: Color = TwineTheme.colorScheme.contentColorFor(backgroundColor),
  rippleColor: Color = TwineTheme.colorScheme.primary,
  onClick: () -> Unit
) {
  Box(
    modifier = modifier
      .wrapContentSize()
      .requiredSize(AppBarDefaults.AppBarIconButtonSize)
      .clip(CircleShape)
      .background(backgroundColor)
      .clickable(
        role = Role.Button,
        onClick = onClick,
        interactionSource = remember {
          MutableInteractionSource()
        },
        indication = rememberRipple(color = rippleColor)
      ),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      painter = painterResource(id = icon),
      contentDescription = null,
      tint = contentColor
    )
  }
}

@Preview
@Composable
private fun TopAppBarDefaultPreview() {
  TwineTheme {
    TopAppBar(title = "Twine")
  }
}

@Preview
@Composable
private fun TopAppBarWithBackButtonPreview() {
  TwineTheme {
    TopAppBar(
      title = "Settings",
      navigationIcon = {
        AppBarBackButton {
          // Handle clicks
        }
      }
    )
  }
}

@Preview
@Composable
private fun TopAppBarWithActionButtonsPreview() {
  TwineTheme {
    TopAppBar(
      title = "Twine",
      actions = {
        AppBarActionButton(icon = R.drawable.ic_alert_off) {
          // Handle clicks
        }

        Spacer(modifier = Modifier.width(AppBarDefaults.AppBarContentSpacing))

        AppBarActionButton(icon = R.drawable.ic_view_mode_timeline) {
          // Handle clicks
        }
      }
    )
  }
}
