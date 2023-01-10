package dev.sasikanth.twine.home

import androidx.recyclerview.widget.DiffUtil
import dev.sasikanth.twine.common.testing.data.clipboard.FakeClipboard
import dev.sasikanth.twine.common.testing.data.repository.FakeTweetsRepository
import dev.sasikanth.twine.common.testing.rules.MainDispatcherRule
import dev.sasikanth.twine.common.testing.sync.FakeConversationSyncQueue
import dev.sasikanth.twine.common.utils.TweetLinkParser
import dev.sasikanth.twine.data.api.models.PublicMetrics
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.User
import dev.sasikanth.twine.data.sync.ConversationSyncQueue
import dev.sasikanth.twine.home.usecase.PagedRecentConversationsUseCase
import dev.sasikanth.twine.home.util.TestPagingDataDiffer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

  private val tweetLinkParser = TweetLinkParser()

  private lateinit var viewModel: HomeViewModel
  private lateinit var fakeClipboard: FakeClipboard
  private lateinit var conversationSyncQueue: ConversationSyncQueue
  private lateinit var pagedRecentConversationsUseCase: PagedRecentConversationsUseCase
  private lateinit var tweetsRepository: FakeTweetsRepository

  private val testDispatcher = StandardTestDispatcher()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule(testDispatcher)

  @Before
  fun setup() {
    fakeClipboard = FakeClipboard()
    conversationSyncQueue = FakeConversationSyncQueue(
      itemFactory = { syncQueueItem ->
        val id = UUID.randomUUID()
        Pair(id, syncQueueItem)
      }
    )
    pagedRecentConversationsUseCase = PagedRecentConversationsUseCase()
    tweetsRepository = FakeTweetsRepository()

    viewModel = HomeViewModel(
      clipboard = fakeClipboard,
      tweetLinkParser = tweetLinkParser,
      conversationSyncQueue = conversationSyncQueue,
      tweetsRepository = tweetsRepository,
      pagedRecentConversationsUseCase = pagedRecentConversationsUseCase
    )
  }

  @Test
  fun when_screen_is_created_then_observe_recent_conversations() = runTest {
    // given
    val author = User(
      id = "280595048",
      name = "Sasikanth",
      username = "its_sasikanth",
      profileImage = "https://twitter.com/image/its_sasikanth.png",
      conversationId = "7258469438729982175"
    )

    val tweetsFromConversation = listOf(
      Tweet(
        id = "7258469438729982175",
        authorId = "280595048",
        conversationId = "7258469438729982175",
        inReplyToUserId = null,
        text = "Tweet 1 in the thread",
        createdAt = Instant.parse("2022-01-01T10:00:00Z"),
        deviceCreatedAt = Instant.parse("2022-01-02T00:00:00Z"),
        publicMetrics = PublicMetrics(
          retweetCount = 0,
          replyCount = 4,
          likeCount = 15,
          quoteCount = 2
        )
      ),
      Tweet(
        id = "9025201324322053244",
        authorId = "280595048",
        conversationId = "7258469438729982175",
        inReplyToUserId = null,
        text = "Tweet 2 in the thread",
        createdAt = Instant.parse("2022-01-01T10:05:00Z"),
        deviceCreatedAt = Instant.parse("2022-01-02T00:00:00Z"),
        publicMetrics = PublicMetrics(
          retweetCount = 0,
          replyCount = 4,
          likeCount = 15,
          quoteCount = 2
        )
      )
    )

    val diffCallback = object : DiffUtil.ItemCallback<RecentConversation>() {
      override fun areItemsTheSame(oldItem: RecentConversation, newItem: RecentConversation): Boolean {
        return oldItem.conversationId == newItem.conversationId
      }

      override fun areContentsTheSame(oldItem: RecentConversation, newItem: RecentConversation): Boolean {
        return oldItem == newItem
      }
    }
    val differ = TestPagingDataDiffer(
      diffCallback = diffCallback
    )

    tweetsRepository.saveTweets(tweetsFromConversation)
    tweetsRepository.saveUsers(listOf(author))

    val job = launch {
      viewModel.recentConversations.collectLatest { pagingData ->
        differ.submitData(pagingData)
      }
    }

    // when
    advanceUntilIdle()

    // then
    differ.assertContent(
      RecentConversation(
        conversationId = "7258469438729982175",
        conversationPreviewText = "Tweet 1 in the thread",
        conversationStartedAt = Instant.parse("2022-01-01T10:00:00Z"),
        conversationCreatedAt = Instant.parse("2022-01-02T00:00:00Z"),
        username = "its_sasikanth",
        userFullName = "Sasikanth",
        userProfileImage = "https://twitter.com/image/its_sasikanth.png",
        numberOfTweetsInConversation = 2
      )
    )

    job.cancel()
  }
}
