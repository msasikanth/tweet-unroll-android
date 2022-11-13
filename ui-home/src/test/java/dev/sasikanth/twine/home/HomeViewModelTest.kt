package dev.sasikanth.twine.home

import com.google.common.truth.Truth.assertThat
import dev.sasikanth.twine.common.testing.data.clipboard.FakeClipboard
import dev.sasikanth.twine.common.testing.sync.FakeConversationSyncQueue
import dev.sasikanth.twine.common.utils.TweetLinkParser
import dev.sasikanth.twine.data.sync.ConversationSyncQueueItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

  private val fakeClipboard = FakeClipboard()
  private val defaultUiState = HomeUiState.DEFAULT
  private val conversationSyncQueue = FakeConversationSyncQueue(
    itemFactory = { syncQueueItem ->
      val id = UUID.randomUUID()
      Pair(id, syncQueueItem)
    }
  )
  private lateinit var viewModel: HomeViewModel

  @Before
  fun setup() {
    viewModel = HomeViewModel(
      clipboard = fakeClipboard,
      tweetLinkParser = TweetLinkParser(),
      conversationSyncQueue = conversationSyncQueue,
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
    val expectedUiState = viewModel.homeUiState.value.invalidUrl()
    assertThat(viewModel.homeUiState.value).isEqualTo(expectedUiState)
  }

  @Test
  fun `when go button is clicked and tweet link is valid, then add to sync queue`() = runTest {
    // given
    val tweetUrl = "https://twitter.com/its_sasikanth/status/1588742946387824644"
    viewModel.tweetUrlChanged(tweetUrl)

    // when
    viewModel.validateAndSync()

    val syncQueue = conversationSyncQueue.queue()

    // then
    val expectedUiState = viewModel.homeUiState.value.onTweetUrlChanged(tweetUrl)
    assertThat(viewModel.homeUiState.value).isEqualTo(expectedUiState)
    assertThat(syncQueue.first()).isEqualTo(
      listOf(
        ConversationSyncQueueItem(
          tweetId = "1588742946387824644",
          tweetBy = "its_sasikanth"
        )
      )
    )
  }

  @Test
  fun `when url is pasted and is valid, then add to sync queue`() = runTest {
    // given
    val tweetUrl = "https://twitter.com/its_sasikanth/status/1588742946387824644"
    fakeClipboard.setText(tweetUrl)

    // when
    viewModel.pasteUrl()

    val syncQueue = conversationSyncQueue.queue()

    // then
    val expectedUiState = viewModel.homeUiState.value.onTweetUrlChanged(tweetUrl)
    assertThat(viewModel.homeUiState.value).isEqualTo(expectedUiState)
    assertThat(syncQueue.first()).isEqualTo(
      listOf(
        ConversationSyncQueueItem(
          tweetId = "1588742946387824644",
          tweetBy = "its_sasikanth"
        )
      )
    )
  }

  @Test
  fun `when url is pasted and is not valid, then update state to show errors`() = runTest {
    // given
    val tweetUrl = "https://twitter.com/status/1588742946387824644"
    fakeClipboard.setText(tweetUrl)

    // when
    viewModel.pasteUrl()

    // then
    val expectedUiState = viewModel.homeUiState.value.invalidUrl()
    assertThat(viewModel.homeUiState.value).isEqualTo(expectedUiState)
  }
}
