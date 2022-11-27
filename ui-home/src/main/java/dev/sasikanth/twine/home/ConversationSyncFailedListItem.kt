package dev.sasikanth.twine.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.components.IconButton
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation
import dev.sasikanth.twine.data.sync.ConversationSyncQueueItem

@Composable
fun ConversationSyncFailedListItem(
  item: ConversationSyncQueueItem,
  modifier: Modifier = Modifier,
  onRetryClick: (item: ConversationSyncQueueItem) -> Unit
) {
  val backgroundColor = TwineTheme
    .colorScheme
    .surfaceColorAtElevation(ElevationTokens.Level2)

  Row(
    modifier = modifier
      .fillMaxWidth()
      .clip(TwineTheme.shapes.large)
      .background(backgroundColor)
      .padding(
        horizontal = 16.dp,
        vertical = 12.dp
      ),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      ConversationHeader(
        tweetBy = item.tweetBy
      )

      Text(
        text = stringResource(id = R.string.home_recent_conversation_sync_failed),
        style = TwineTheme.typography.labelMedium,
        color = TwineTheme.colorScheme.error
      )
    }

    Spacer(modifier = Modifier.width(16.dp))

    IconButton(
      modifier = Modifier.clip(CircleShape),
      backgroundColor = TwineTheme.colorScheme.inversePrimary,
      contentColor = TwineTheme.colorScheme.onPrimaryContainer,
      onClick = {
        onRetryClick.invoke(item)
      }
    ) {
      Icon(
        imageVector = Icons.Filled.Refresh,
        contentDescription = null
      )
    }
  }
}

@Composable
private fun ConversationHeader(tweetBy: String) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      modifier = Modifier.size(16.dp),
      imageVector = Icons.Outlined.AccountCircle,
      contentDescription = null,
      tint = TwineTheme.colorScheme.secondary
    )

    Text(
      text = "@$tweetBy",
      style = TwineTheme.typography.labelMedium,
      color = TwineTheme.colorScheme.secondary
    )
  }
}

@Preview
@Composable
private fun ConversationSyncFailedListItemPreview() {
  TwineTheme {
    Box {
      ConversationSyncFailedListItem(
        modifier = Modifier.padding(24.dp),
        item = ConversationSyncQueueItem(
          tweetId = "7077269206869072005",
          tweetBy = "its_sasikanth"
        ),
        onRetryClick = {
          // Handle sync retry
        }
      )
    }
  }
}
