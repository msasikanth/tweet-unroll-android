package dev.sasikanth.twine.home

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.sasikanth.twine.common.testing.data.clipboard.FakeClipboard
import dev.sasikanth.twine.common.testing.rules.MainDispatcherRule
import dev.sasikanth.twine.common.testing.sync.FakeConversationSyncQueue
import dev.sasikanth.twine.common.utils.TweetLinkParser
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.sync.ConversationSyncQueue
import dev.sasikanth.twine.data.sync.ConversationSyncQueueItem
import dev.sasikanth.twine.data.sync.Status
import dev.sasikanth.twine.home.usecase.FakePagedSourceUseCase
import dev.sasikanth.twine.home.util.collectDataForTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

  private val defaultUiState = HomeUiState.DEFAULT
  private val tweetLinkParser = TweetLinkParser()

  private lateinit var viewModel: HomeViewModel
  private lateinit var fakeClipboard: FakeClipboard
  private lateinit var conversationSyncQueue: ConversationSyncQueue
  private lateinit var fakePagedSourceUseCase: FakePagedSourceUseCase

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    fakeClipboard = FakeClipboard()
    conversationSyncQueue = FakeConversationSyncQueue(
      itemFactory = { syncQueueItem ->
        val id = UUID.randomUUID()
        Pair(id, syncQueueItem)
      }
    )
    fakePagedSourceUseCase = FakePagedSourceUseCase()

    viewModel = HomeViewModel(
      clipboard = fakeClipboard,
      tweetLinkParser = tweetLinkParser,
      conversationSyncQueue = conversationSyncQueue,
      pagedRecentConversationsUseCase = fakePagedSourceUseCase
    )
  }

  @Test
  fun `when tweet url is changed, then update the state`() {
    // given
    val tweetUrl = "https://twitter.com/its_sasikanth/status/1588742946387824640"

    // when
    viewModel.tweetUrlChanged(tweetUrl = tweetUrl)

    // then
    val expectedUiState = defaultUiState.onTweetUrlChanged(tweetUrl)
    assertThat(viewModel.homeUiState.value).isEqualTo(expectedUiState)
  }

  @Test
  fun `when clear button is clicked, then clear the text`() {
    // given
    val tweetUrl = "https://twitter.com/its_sasikanth/status/1588742946387824644"
    viewModel.tweetUrlChanged(tweetUrl)

    // when
    viewModel.clearUrl()

    // then
    val expectedUiState = defaultUiState.onTweetUrlChanged(tweetUrl = null)
    assertThat(viewModel.homeUiState.value).isEqualTo(expectedUiState)
  }

  @Test
  fun `when go button is clicked and tweet link is invalid, then update the state`() {
    // given
    val tweetUrl = "https://twitter.com/status/1588742946387824644"
    viewModel.tweetUrlChanged(tweetUrl)

    // when
    viewModel.validateAndSync()

    // then
    val expectedUiState = defaultUiState
      .onTweetUrlChanged(tweetUrl)
      .invalidUrl()
    assertThat(viewModel.homeUiState.value).isEqualTo(expectedUiState)
  }

  @Test
  fun `when go button is clicked and tweet link is valid, then add to sync queue`() = runTest {
    // given
    val tweetUrl = "https://twitter.com/its_sasikanth/status/1588742946387824644"
    val syncQueueItem = ConversationSyncQueueItem(
      tweetId = "1588742946387824644",
      tweetBy = "its_sasikanth"
    )

    viewModel.tweetUrlChanged(tweetUrl)

    // when
    viewModel.validateAndSync()

    val syncQueue = conversationSyncQueue.queue()

    // then
    val expectedUiState = defaultUiState.onSyncQueueLoaded(listOf(syncQueueItem))

    assertThat(viewModel.homeUiState.value).isEqualTo(expectedUiState)
    assertThat(syncQueue.first()).isEqualTo(listOf(syncQueueItem))
  }

  @Test
  fun `when url is pasted and is valid, then add to sync queue`() = runTest {
    // given
    val tweetUrl = "https://twitter.com/its_sasikanth/status/1588742946387824644"
    val syncQueueItem = ConversationSyncQueueItem(
      tweetId = "1588742946387824644",
      tweetBy = "its_sasikanth"
    )

    fakeClipboard.setText(tweetUrl)

    // when
    viewModel.pasteUrl()

    val syncQueue = conversationSyncQueue.queue()

    // then
    val expectedUiState = defaultUiState.onSyncQueueLoaded(listOf(syncQueueItem))

    assertThat(viewModel.homeUiState.value).isEqualTo(expectedUiState)
    assertThat(syncQueue.first()).isEqualTo(listOf(syncQueueItem))
  }

  @Test
  fun `when url is pasted and is not valid, then update state to show errors`() = runTest {
    // given
    val tweetUrl = "https://twitter.com/status/1588742946387824644"
    fakeClipboard.setText(tweetUrl)

    // when
    viewModel.pasteUrl()

    // then
    val expectedUiState = defaultUiState
      .onTweetUrlChanged(tweetUrl)
      .invalidUrl()
    assertThat(viewModel.homeUiState.value).isEqualTo(expectedUiState)
  }

  @Test
  fun `when paste is clicked and url is null or blank, then do nothing`() = runTest {
    val tweetUrl = ""
    fakeClipboard.setText(tweetUrl)

    // when
    viewModel.pasteUrl()

    // then
    assertThat(fakeClipboard.content).isEqualTo("")
    assertThat(viewModel.homeUiState.value).isEqualTo(defaultUiState)
  }

  @Test
  fun `when screen is created, then observe sync queue`() = runTest {
    // given
    val syncQueueItem = ConversationSyncQueueItem(
      tweetId = "1588742946387824644",
      tweetBy = "its_sasikanth"
    )

    assertThat(conversationSyncQueue.queue().first()).isEmpty()

    // when & then
    val expectedUiState = defaultUiState
      .onSyncQueueLoaded(listOf(syncQueueItem))

    viewModel.homeUiState.test {
      assertThat(awaitItem()).isEqualTo(defaultUiState)

      conversationSyncQueue.add(syncQueueItem)

      assertThat(awaitItem()).isEqualTo(expectedUiState)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `when screen is created, then observe recent conversations`() = runTest {
    // given
    val items = listOf(
      RecentConversation(
        conversationId = "1550874190793674753",
        conversationPreviewText = "Tweet 1 in the thread",
        conversationStartedAt = Instant.parse("2022-07-23T16:03:15Z"),
        conversationCreatedAt = Instant.parse("2022-08-01T00:00:00Z"),
        username = "its_sasikanth",
        userFullName = "Sasikanth",
        userProfileImage = "https://pbs.twimg.com/profile_images/1535630758777602050/q1qaITTW_normal.jpg",
        numberOfTweetsInConversation = 2
      )
    )
    val pagingData = PagingData.from(items)

    // when & then
    viewModel.recentConversations.test {
      assertThat(awaitItem().collectDataForTest()).isEqualTo(emptyList<RecentConversation>())

      fakePagedSourceUseCase.addPagingData(pagingData)

      assertThat(awaitItem().collectDataForTest()).isEqualTo(items)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `when cancel sync button is clicked, then remove the item from sync queue`() = runTest {
    // given
    val syncQueueItem = ConversationSyncQueueItem(
      tweetId = "1588742946387824644",
      tweetBy = "its_sasikanth"
    )

    conversationSyncQueue.add(syncQueueItem)

    // when & then
    viewModel.homeUiState.test {
      assertThat(awaitItem()).isEqualTo(
        defaultUiState
          .onSyncQueueLoaded(listOf(syncQueueItem))
      )

      viewModel.cancelSync(syncQueueItem)

      assertThat(awaitItem()).isEqualTo(
        defaultUiState
          .onSyncQueueLoaded(emptyList())
      )

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `when retry sync button is clicked, then retry sync`() = runTest {
    // given
    val syncQueueItem = ConversationSyncQueueItem(
      tweetId = "1588742946387824644",
      tweetBy = "its_sasikanth",
      status = Status.Failure
    )

    conversationSyncQueue.add(syncQueueItem)

    // when & then
    viewModel.homeUiState.test {
      assertThat(awaitItem()).isEqualTo(
        defaultUiState
          .onSyncQueueLoaded(listOf(syncQueueItem))
      )

      viewModel.retrySync(syncQueueItem)

      assertThat(awaitItem()).isEqualTo(
        defaultUiState
          .onSyncQueueLoaded(listOf(syncQueueItem.updateStatus(Status.Enqueued)))
      )

      cancelAndIgnoreRemainingEvents()
    }
  }
}
