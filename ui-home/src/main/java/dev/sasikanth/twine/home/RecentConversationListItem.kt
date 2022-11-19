package dev.sasikanth.twine.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation
import dev.sasikanth.twine.common.ui.utils.LocalTwineDateFormatter
import dev.sasikanth.twine.common.utils.TwineDateFormatter
import dev.sasikanth.twine.data.database.entities.RecentConversation
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecentConversationListItem(
  recentConversation: RecentConversation,
  onDelete: (id: String) -> Unit,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  val backgroundShape = TwineTheme.shapes.large

  val dismissState = rememberDismissState(
    confirmStateChange = {
      onDelete.invoke(recentConversation.conversationId)
      false
    }
  )

  SwipeToDismiss(
    modifier = modifier
      .graphicsLayer {
        this.clip = true
        this.shape = backgroundShape
      },
    state = dismissState,
    dismissThresholds = {
      FractionalThreshold(0.5f)
    },
    background = {
      SwipeBackground(
        dismissStateProvider = { dismissState }
      )
    }
  ) {
    Content(
      recentConversation = recentConversation,
      backgroundShape = backgroundShape,
      onClick = onClick
    )
  }
}

@Composable
private fun Content(
  recentConversation: RecentConversation,
  backgroundShape: CornerBasedShape,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  val backgroundColor = TwineTheme
    .colorScheme
    .surfaceColorAtElevation(ElevationTokens.Level2)

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(backgroundShape)
      .background(backgroundColor)
      .clickable(onClick = onClick)
      .padding(vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    val conversationRelativeTimeStamp = LocalTwineDateFormatter
      .current
      .getRelativeTime(recentConversation.conversationStartedAt)

    Row(
      modifier = Modifier
        .padding(
          start = 16.dp,
          end = 12.dp
        ),
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      ConversationHeader(
        modifier = Modifier.weight(1f),
        username = recentConversation.username,
        userFullName = recentConversation.userFullName,
        userProfileImage = recentConversation.userProfileImage,
        conversationRelativeTimeStamp = conversationRelativeTimeStamp
      )

      ThreadCountPill(
        count = recentConversation.numberOfTweetsInConversation
      )
    }

    Text(
      modifier = Modifier
        .padding(horizontal = 16.dp),
      text = recentConversation.conversationPreviewText,
      style = TwineTheme.typography.bodySmall,
      color = TwineTheme.colorScheme.onSurfaceVariant,
      maxLines = 3,
      overflow = TextOverflow.Ellipsis
    )
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwipeBackground(
  modifier: Modifier = Modifier,
  dismissStateProvider: () -> DismissState
) {
  val dismissState = dismissStateProvider.invoke()
  val dismissDirection = dismissState.dismissDirection
  val dismissOffset = dismissState.offset.value

  if (dismissDirection != null) {
    val arrangement = when (dismissDirection) {
      DismissDirection.StartToEnd -> Arrangement.Start
      DismissDirection.EndToStart -> Arrangement.End
    }

    val paddingValues = when (dismissDirection) {
      DismissDirection.StartToEnd -> PaddingValues(end = 8.dp)
      DismissDirection.EndToStart -> PaddingValues(start = 8.dp)
    }

    val contentAlignment = when (dismissDirection) {
      DismissDirection.StartToEnd -> Alignment.CenterEnd
      DismissDirection.EndToStart -> Alignment.CenterStart
    }

    val density = LocalDensity.current
    val backgroundWidth = with(density) {
      dismissOffset.absoluteValue.toDp()
    }

    Row(
      modifier = modifier.fillMaxSize(),
      horizontalArrangement = arrangement
    ) {
      Box(
        modifier = Modifier
          .fillMaxHeight()
          .width(backgroundWidth)
          .padding(paddingValues)
          .clip(TwineTheme.shapes.large)
          .background(
            color = TwineTheme.colorScheme.deleteButton
          )
          .padding(horizontal = 20.dp),
        contentAlignment = contentAlignment
      ) {
        Icon(
          painter = painterResource(id = R.drawable.ic_delete),
          contentDescription = null,
          tint = TwineTheme.colorScheme.onDeleteButton
        )
      }
    }
  }
}

@Composable
private fun ConversationHeader(
  username: String,
  userFullName: String,
  userProfileImage: String,
  conversationRelativeTimeStamp: String,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    AsyncImage(
      modifier = Modifier
        .size(16.dp)
        .clip(CircleShape),
      model = userProfileImage,
      contentDescription = null,
      placeholder = rememberVectorPainter(image = Icons.Outlined.AccountCircle)
    )

    Text(
      modifier = Modifier.weight(weight = 1f, fill = false),
      text = userFullName,
      style = TwineTheme.typography.labelMedium,
      color = TwineTheme.colorScheme.onSurface,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )

    Text(
      text = "@$username",
      style = TwineTheme.typography.labelMedium,
      color = TwineTheme.colorScheme.primary
    )

    Text(
      text = stringResource(
        id = R.string.home_recent_conversation_header_separator
      ),
      style = TwineTheme.typography.bodySmall,
      color = TwineTheme.colorScheme.outline
    )

    Text(
      text = conversationRelativeTimeStamp,
      style = TwineTheme.typography.bodySmall,
      color = TwineTheme.colorScheme.outline
    )
  }
}

@Composable
private fun ThreadCountPill(
  count: Int,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .background(
        color = TwineTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(percent = 50)
      )
      .padding(
        horizontal = 6.dp,
        vertical = 2.dp
      ),
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = count.toString(),
      style = TwineTheme.typography.labelSmall,
      color = TwineTheme.colorScheme.onPrimaryContainer
    )
    Icon(
      painter = painterResource(id = R.drawable.ic_thread_count),
      contentDescription = null,
      tint = TwineTheme.colorScheme.onPrimaryContainer
    )
  }
}

@Preview
@Composable
private fun RecentConversationListItemPreview() {
  val dateFormatter = TwineDateFormatter(
    zoneId = ZoneId.systemDefault(),
    locale = Locale.getDefault()
  )

  CompositionLocalProvider(
    LocalTwineDateFormatter provides dateFormatter
  ) {
    TwineTheme {
      RecentConversationListItem(
        modifier = Modifier.padding(24.dp),
        recentConversation = RecentConversation(
          conversationId = "1234567890",
          username = "its_sasikanth",
          userFullName = "Sasikanth",
          userProfileImage = "",
          conversationPreviewText = "This is the first tweet from the thread",
          conversationStartedAt = Instant.parse("2018-02-01T00:00:00Z"),
          conversationCreatedAt = Instant.parse("2018-02-01T00:00:00Z"),
          numberOfTweetsInConversation = 5
        ),
        onClick = {
          // Handle clicks
        },
        onDelete = {
          // Handle delete
        }
      )
    }
  }
}

@Preview
@Composable
fun ThreadCountPillPreview() {
  TwineTheme {
    ThreadCountPill(
      modifier = Modifier.padding(24.dp),
      count = 6
    )
  }
}
