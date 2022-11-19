package dev.sasikanth.twine.settings

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.components.AppBarBackButton
import dev.sasikanth.twine.common.ui.components.SegmentedButtonGroup
import dev.sasikanth.twine.common.ui.components.SegmentedButtonItem
import dev.sasikanth.twine.common.ui.components.SubHeader
import dev.sasikanth.twine.common.ui.components.Switch
import dev.sasikanth.twine.common.ui.components.TopAppBar
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation
import dev.sasikanth.twine.data.prefrences.Theme
import dev.sasikanth.twine.data.prefrences.Theme.DARK
import dev.sasikanth.twine.data.prefrences.Theme.LIGHT
import dev.sasikanth.twine.data.prefrences.Theme.SYSTEM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
  modifier: Modifier = Modifier,
  theme: Theme,
  useDynamicColors: Boolean,
  onThemeChange: (Theme) -> Unit,
  toggleDynamicColors: (Boolean) -> Unit,
  navigateBack: () -> Unit
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = stringResource(id = R.string.screen_settings),
        navigationIcon = {
          AppBarBackButton(onClick = navigateBack)
        }
      )
    }
  ) {
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(it)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp)
    ) {
      ThemeSection(
        theme = theme,
        isDynamicColorsSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
        useDynamicColors = useDynamicColors,
        onThemeChange = onThemeChange,
        toggleDynamicColors = toggleDynamicColors
      )
    }
  }
}

@Composable
private fun ThemeSection(
  modifier: Modifier = Modifier,
  theme: Theme,
  isDynamicColorsSupported: Boolean,
  useDynamicColors: Boolean,
  onThemeChange: (Theme) -> Unit,
  toggleDynamicColors: (Boolean) -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight()
  ) {
    SubHeader(text = stringResource(id = R.string.settings_header_theme))

    val lightTheme = SegmentedButtonItem(stringResource(id = R.string.theme_light))
    val darkTheme = SegmentedButtonItem(stringResource(id = R.string.theme_dark))
    val systemTheme = SegmentedButtonItem(stringResource(id = R.string.theme_system))

    val items = listOf(lightTheme, darkTheme, systemTheme)

    val selectedTheme = when (theme) {
      LIGHT -> lightTheme
      DARK -> darkTheme
      SYSTEM -> systemTheme
    }

    SegmentedButtonGroup(
      items = items,
      selectedItem = selectedTheme
    ) {
      val newTheme = when (it) {
        lightTheme -> LIGHT
        darkTheme -> DARK
        systemTheme -> SYSTEM
        else -> SYSTEM
      }
      onThemeChange(newTheme)
    }

    if (isDynamicColorsSupported) {
      Spacer(modifier = Modifier.height(16.dp))

      SettingsListItem(
        title = stringResource(id = R.string.use_material_you),
        subtitle = stringResource(id = R.string.use_material_you_desc),
        endSlot = {
          Switch(
            checked = useDynamicColors,
            onValueChange = toggleDynamicColors
          )
        },
        onClick = {
          toggleDynamicColors(!useDynamicColors)
        }
      )
    }
  }
}

@Composable
private fun SettingsListItem(
  modifier: Modifier = Modifier,
  title: String,
  subtitle: String? = null,
  endSlot: (@Composable () -> Unit)? = null,
  onClick: () -> Unit
) {
  val backgroundColor = TwineTheme
    .colorScheme
    .surfaceColorAtElevation(ElevationTokens.Level2)

  Row(
    modifier = modifier
      .fillMaxWidth()
      .clip(TwineTheme.shapes.large)
      .background(backgroundColor)
      .clickable(onClick = onClick)
      .padding(
        horizontal = 16.dp,
        vertical = 12.dp
      ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column(
      modifier = Modifier.weight(1f)
    ) {
      Text(
        text = title,
        style = TwineTheme.typography.titleMedium,
        color = TwineTheme.colorScheme.onSurface
      )
      if (subtitle != null) {
        Text(
          text = subtitle,
          style = TwineTheme.typography.bodyMedium,
          color = TwineTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    if (endSlot != null) {
      Spacer(modifier = Modifier.width(16.dp))
      endSlot.invoke()
    }
  }
}

@Preview
@Composable
private fun SettingsPagePreview() {
  TwineTheme {
    SettingsPage(
      theme = SYSTEM,
      useDynamicColors = true,
      navigateBack = {
        // navigate to previous page
      },
      onThemeChange = {
        // Handle theme changes
      },
      toggleDynamicColors = {
      }
    )
  }
}
