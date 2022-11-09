package dev.sasikanth.twine.data.sync

import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult.Page
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sasikanth.twine.common.dispatchers.CoroutineDispatchers
import dev.sasikanth.twine.data.api.FakeTwitterRemoteSource
import dev.sasikanth.twine.data.api.models.ConversationsLookupPayload
import dev.sasikanth.twine.data.api.models.IncludesPayload
import dev.sasikanth.twine.data.api.models.TweetLookupPayload
import dev.sasikanth.twine.data.api.models.TweetPayload
import dev.sasikanth.twine.data.api.models.UserPayload
import dev.sasikanth.twine.data.database.TwineDatabase
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.repository.TweetsRepository
import dev.sasikanth.twine.data.database.repository.UsersRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import javax.inject.Inject

@HiltAndroidTest
class ConversationSyncTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var fakeTwitterRemoteSource: FakeTwitterRemoteSource

  @Inject
  lateinit var tweetsRepository: TweetsRepository

  @Inject
  lateinit var usersRepository: UsersRepository

  @Inject
  lateinit var dispatchers: CoroutineDispatchers

  @Inject
  lateinit var appDatabase: TwineDatabase

  private lateinit var conversationSync: ConversationSync

  @Before
  fun setup() {
    hiltRule.inject()

    conversationSync = ConversationSync(
      twitterRemoteSource = fakeTwitterRemoteSource,
      tweetsRepository = tweetsRepository,
      usersRepository = usersRepository,
      dispatchers = dispatchers
    )
  }

  @After
  fun tearDown() {
    fakeTwitterRemoteSource.clearAll()
    appDatabase.clearAllTables()
  }

  @Test
  fun syncing_a_tweet_should_work_correctly() = runTest {
    // given
    val tweetLookupPayload = TweetLookupPayload(
      data = TweetPayload(
        id = "1550874190793674753",
        authorId = "280595048",
        conversationId = "1550874190793674753",
        text = "Tweet 1 in the thread",
        attachments = null,
        entities = null,
        inReplyToUserId = null,
        referencedTweets = null,
        createdAt = LocalDateTime.parse("2022-07-23T16:03:15")
      ),
      includes = IncludesPayload(
        tweets = null,
        media = null,
        users = listOf(
          UserPayload(
            id = "280595048",
            name = "Sasikanth",
            username = "its_sasikanth",
            profileImage = "https://pbs.twimg.com/profile_images/1535630758777602050/q1qaITTW_normal.jpg"
          )
        ),
        polls = null
      )
    )

    val conversationLookupPayload = ConversationsLookupPayload(
      data = listOf(
        TweetPayload(
          id = "1550874190793674756",
          authorId = "280595048",
          conversationId = "1550874190793674753",
          text = "Tweet 2 in the thread",
          attachments = null,
          entities = null,
          inReplyToUserId = null,
          referencedTweets = null,
          createdAt = LocalDateTime.parse("2022-07-23T16:10:15")
        )
      ),
      includes = IncludesPayload(
        tweets = null,
        media = null,
        users = listOf(
          UserPayload(
            id = "280595048",
            name = "Sasikanth",
            username = "its_sasikanth",
            profileImage = "https://pbs.twimg.com/profile_images/1535630758777602050/q1qaITTW_normal.jpg"
          )
        ),
        polls = null
      ),
      meta = null
    )

    fakeTwitterRemoteSource.addTweetLookupPayload(tweetLookupPayload)
    fakeTwitterRemoteSource.addConversationLookupPayload(conversationLookupPayload)

    // when
    val result = conversationSync.trySync(tweetId = "1550874190793674753")

    val recentConversationsPage = tweetsRepository.recentConversations()
      .load(
        Refresh(
          key = null,
          loadSize = 10,
          placeholdersEnabled = false
        )
      ) as Page<Int, RecentConversation>

    // then
    assertThat(result).isEqualTo(Response.Success)
    assertThat(recentConversationsPage.data).isEqualTo(
      listOf(
        RecentConversation(
          conversationId = "1550874190793674753",
          conversationPreviewText = "Tweet 1 in the thread",
          conversationStartedAt = LocalDateTime.parse("2022-07-23T16:03:15"),
          username = "its_sasikanth",
          userFullName = "Sasikanth",
          userProfileImage = "https://pbs.twimg.com/profile_images/1535630758777602050/q1qaITTW_normal.jpg",
          numberOfTweetsInConversation = 2
        )
      )
    )
  }

  @Test
  fun syncing_a_tweet_that_is_not_present_should_fail() = runTest {
    // when
    val result = conversationSync.trySync(tweetId = "1550874190793674256")

    // then
    assertThat(result).isEqualTo(Response.NoTweetFound)
  }

  @Test
  fun syncing_a_conversation_without_head_should_fail() = runTest {
    // given
    val tweetLookupPayload = TweetLookupPayload(
      data = TweetPayload(
        id = "1550874190793674757",
        authorId = "280595048",
        conversationId = "1550874190793674753",
        text = "Tweet 3 in the thread",
        attachments = null,
        entities = null,
        inReplyToUserId = null,
        referencedTweets = null,
        createdAt = LocalDateTime.parse("2022-07-23T16:03:15")
      ),
      includes = IncludesPayload(
        tweets = null,
        media = null,
        users = listOf(
          UserPayload(
            id = "280595048",
            name = "Sasikanth",
            username = "its_sasikanth",
            profileImage = "https://pbs.twimg.com/profile_images/1535630758777602050/q1qaITTW_normal.jpg"
          )
        ),
        polls = null
      )
    )

    fakeTwitterRemoteSource.addTweetLookupPayload(tweetLookupPayload)

    // when
    val result = conversationSync.trySync(tweetId = "1550874190793674753")

    // then
    assertThat(result).isEqualTo(Response.NoTweetFound)
  }
}
