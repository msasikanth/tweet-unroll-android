package dev.sasikanth.twine.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.sasikanth.twine.common.ui.components.AppBarActionButton
import dev.sasikanth.twine.common.ui.components.SubHeader
import dev.sasikanth.twine.common.ui.components.TopAppBar
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.data.sync.Status.Enqueued
import dev.sasikanth.twine.data.sync.Status.Failure
import dev.sasikanth.twine.data.sync.Status.InProgress
import dev.sasikanth.twine.common.ui.R as commonR

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun HomePage(
  modifier: Modifier = Modifier,
  navigateToSearch: () -> Unit,
  navigateToSettings: () -> Unit,
  viewModel: HomeViewModel = hiltViewModel()
) {
  val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
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
    }
  ) {
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(it),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      HeroInput(
        text = homeUiState.tweetUrl.orEmpty(),
        inputErrors = homeUiState.errors,
        onPasteClick = viewModel::pasteUrl,
        onTextChange = viewModel::tweetUrlChanged,
        onClearTextClick = viewModel::clearUrl,
        onGoClick = viewModel::validateAndSync
      )

      val syncQueueItems = homeUiState.syncQueue

      LazyColumn(
        contentPadding = PaddingValues(
          start = 24.dp,
          end = 24.dp,
          bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        item {
          SubHeader(text = stringResource(id = R.string.home_recent_conversations_sub_head))
        }

        items(
          items = syncQueueItems,
          key = { item -> item.tweetId }
        ) { item ->
          when (item.status) {
            Enqueued,
            InProgress -> ConversationSyncingListItem(
              item = item,
              onCancelClick = viewModel::cancelSync
            )

            // TODO: Replace with failure item composable
            Failure -> Box {}
          }
        }
      }
    }
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
