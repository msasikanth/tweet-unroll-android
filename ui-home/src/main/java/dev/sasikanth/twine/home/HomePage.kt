package dev.sasikanth.twine.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import dev.sasikanth.twine.common.ui.components.AppBarActionButton
import dev.sasikanth.twine.common.ui.components.SubHeader
import dev.sasikanth.twine.common.ui.components.TopAppBar
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.data.sync.ConversationSyncQueueItem
import dev.sasikanth.twine.data.sync.Status
import kotlinx.collections.immutable.toImmutableList
import dev.sasikanth.twine.common.ui.R as commonR

@OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalLifecycleComposeApi::class,
  ExperimentalFoundationApi::class
)
@Composable
fun HomePage(
  navigateToSearch: () -> Unit,
  navigateToSettings: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = hiltViewModel()
) {
  val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
  val recentConversations = viewModel.recentConversations.collectAsLazyPagingItems()

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
        }
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
        inputErrors = homeUiState.errors.toImmutableList(),
        onPasteClick = viewModel::pasteUrl,
        onTextChange = viewModel::tweetUrlChanged,
        onClearTextClick = viewModel::clearUrl,
        onGoClick = viewModel::validateAndSync
      )

      val syncQueueItems = homeUiState.syncQueue

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
          start = 24.dp,
          end = 24.dp,
          bottom = 24.dp
        )
      ) {
        item {
          SubHeader(text = stringResource(id = R.string.home_recent_conversations_sub_head))
        }

        if (syncQueueItems.isNotEmpty()) {
          SyncQueue(
            syncQueueItems = syncQueueItems,
            onCancelClick = viewModel::cancelSync,
            onRetryClick = viewModel::retrySync
          )
        }

        items(
          items = recentConversations,
          key = { item -> "rc-${item.conversationId}" }
        ) { recentConversation ->
          recentConversation?.let {
            RecentConversationListItem(
              modifier = Modifier
                .animateItemPlacement()
                .padding(bottom = 8.dp),
              recentConversation = recentConversation
            ) {
              // Handle clicks
            }
          }
        }
      }
    }
  }
}

private fun LazyListScope.SyncQueue(
  syncQueueItems: List<ConversationSyncQueueItem>,
  onCancelClick: (item: ConversationSyncQueueItem) -> Unit,
  onRetryClick: (item: ConversationSyncQueueItem) -> Unit
) {
  items(
    items = syncQueueItems,
    key = { item -> "sync-${item.tweetId}" }
  ) { item ->
    when (item.status) {
      Status.Enqueued,
      Status.InProgress -> ConversationSyncingListItem(
        modifier = Modifier.padding(bottom = 8.dp),
        item = item,
        onCancelClick = onCancelClick
      )

      Status.Failure -> ConversationSyncFailedListItem(
        modifier = Modifier.padding(bottom = 8.dp),
        item = item,
        onRetryClick = onRetryClick
      )
    }
  }

  item { Spacer(modifier = Modifier.height(16.dp)) }
}

@Preview
@Composable
private fun HomePagePreview() {
  TwineTheme {
    HomePage(
      navigateToSearch = {
        // Handle navigation
      },
      navigateToSettings = {
        // Handle navigation
      }
    )
  }
}
