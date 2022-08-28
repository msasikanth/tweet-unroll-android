package dev.sasikanth.twine.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.components.AppBarActionButton
import dev.sasikanth.twine.common.ui.components.TopAppBar
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.R as commonR

@Composable
fun HomePage(
  modifier: Modifier = Modifier,
  navigateToSearch: () -> Unit,
  navigateToSettings: () -> Unit,
) {
  Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    TopAppBar(
      title = stringResource(id = R.string.home_app_bar_title),
      actions = {
        AppBarActionButton(
          icon = commonR.drawable.ic_search,
          onClick = navigateToSearch
        )

        Spacer(modifier = Modifier.width(24.dp))

        AppBarActionButton(
          icon = commonR.drawable.ic_settings,
          onClick = navigateToSettings
        )
      },
    )

    // Add home page content
    Text(
      text = "Work in progress!"
    )
  }
}

@Preview
@Composable
private fun HomePagePreview() {
  TwineTheme {
    HomePage(
      navigateToSearch = {
        // Handle navigation
      }, navigateToSettings = {
        // Handle navigation
      })
  }
}
