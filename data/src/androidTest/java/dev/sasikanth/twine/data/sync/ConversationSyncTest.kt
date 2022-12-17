package dev.sasikanth.twine.data.sync

import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult.Page
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sasikanth.twine.common.testing.util.TestUserClock
import dev.sasikanth.twine.data.api.FakeTwitterRemoteSource
import dev.sasikanth.twine.data.api.TwitterRemoteSource
import dev.sasikanth.twine.data.api.models.AttachmentsPayload
import dev.sasikanth.twine.data.api.models.ConversationsLookupPayload
import dev.sasikanth.twine.data.api.models.IncludesPayload
import dev.sasikanth.twine.data.api.models.MediaPayload
import dev.sasikanth.twine.data.api.models.MediaTypePayload
import dev.sasikanth.twine.data.api.models.PublicMetrics
import dev.sasikanth.twine.data.api.models.ReferenceTypePayload
import dev.sasikanth.twine.data.api.models.ReferencedTweetPayload
import dev.sasikanth.twine.data.api.models.TweetLookupPayload
import dev.sasikanth.twine.data.api.models.TweetPayload
import dev.sasikanth.twine.data.api.models.UserPayload
import dev.sasikanth.twine.data.database.TwineDatabase
import dev.sasikanth.twine.data.database.entities.Media
import dev.sasikanth.twine.data.database.entities.MediaType
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.entities.ReferenceType
import dev.sasikanth.twine.data.database.entities.ReferencedTweet
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.TweetWithContent
import dev.sasikanth.twine.data.database.repository.TweetsRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

@HiltAndroidTest
class ConversationSyncTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var twitterRemoteSource: TwitterRemoteSource

  @Inject
  lateinit var tweetsRepository: TweetsRepositoryImpl

  @Inject
  lateinit var conversationSync: ConversationSync

  @Inject
  lateinit var appDatabase: TwineDatabase

  @Inject
  lateinit var testUserClock: TestUserClock

  private val fakeTwitterRemoteSource
    get() = (twitterRemoteSource as FakeTwitterRemoteSource)

  @Before
  fun setup() {
    hiltRule.inject()

    testUserClock.setDate(LocalDate.parse("2022-08-01"))
  }

  @After
  fun tearDown() {
    fakeTwitterRemoteSource.clearAll()
    appDatabase.clearAllTables()
  }

  @Test
  fun syncing_a_conversation_should_work_correctly() = runTest {
    // given
    val tweetLookupPayload = TweetLookupPayload(
      data = TweetPayload(
        id = "1550874190793674753",
        authorId = "280595048",
        conversationId = "1550874190793674753",
        text = "Tweet 1 in the thread",
        attachments = AttachmentsPayload(
          mediaKeys = listOf(
            "3_7291856149861120695"
          ),
          pollIds = null
        ),
        entities = null,
        inReplyToUserId = null,
        referencedTweets = null,
        createdAt = Instant.parse("2022-07-23T16:03:15Z"),
        publicMetrics = PublicMetrics(
          retweetCount = 0,
          replyCount = 10,
          likeCount = 25,
          quoteCount = 0
        )
      ),
      includes = IncludesPayload(
        tweets = null,
        media = listOf(
          MediaPayload(
            mediaKey = "3_7291856149861120695",
            type = MediaTypePayload.Photo,
            url = "https://twitter.com/img/photo.jpg",
            previewImage = "https://twitter.com/img/photo_preview.jpg"
          )
        ),
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
          referencedTweets = listOf(
            ReferencedTweetPayload(
              id = "2387730861057376900",
              type = ReferenceTypePayload.Quoted
            )
          ),
          createdAt = Instant.parse("2022-07-23T16:10:15Z"),
          publicMetrics = PublicMetrics(
            retweetCount = 0,
            replyCount = 10,
            likeCount = 25,
            quoteCount = 0
          )
        )
      ),
      includes = IncludesPayload(
        tweets = listOf(
          TweetPayload(
            id = "2387730861057376900",
            authorId = "280595048",
            conversationId = "1550874190793674758",
            text = "Quoted tweet",
            attachments = null,
            entities = null,
            inReplyToUserId = null,
            referencedTweets = null,
            createdAt = Instant.parse("2022-06-23T16:10:15Z"),
            publicMetrics = PublicMetrics(
              retweetCount = 0,
              replyCount = 10,
              likeCount = 25,
              quoteCount = 0
            )
          )
        ),
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

    val tweetsInConversation = tweetsRepository.tweetsInConversation(
      conversationId = "1550874190793674753"
    ).first()

    // then
    assertThat(result).isEqualTo(Response.Success)
    assertThat(recentConversationsPage.data).isEqualTo(
      listOf(
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
    )
    assertThat(tweetsInConversation).isEqualTo(
      listOf(
        TweetWithContent(
          tweet = Tweet(
            id = "1550874190793674753",
            authorId = "280595048",
            conversationId = "1550874190793674753",
            inReplyToUserId = null,
            text = "Tweet 1 in the thread",
            createdAt = Instant.parse("2022-07-23T16:03:15Z"),
            deviceCreatedAt = Instant.parse("2022-08-01T00:00:00Z"),
            publicMetrics = PublicMetrics(
              retweetCount = 0,
              replyCount = 10,
              likeCount = 25,
              quoteCount = 0
            )
          ),
          entities = emptyList(),
          referencedTweets = emptyList(),
          media = listOf(
            Media(
              mediaKey = "3_7291856149861120695",
              type = MediaType.Photo,
              url = "https://twitter.com/img/photo.jpg",
              previewImage = "https://twitter.com/img/photo_preview.jpg",
              tweetId = "1550874190793674753",
              conversationId = "1550874190793674753"
            )
          ),
          polls = emptyList()
        ),
        TweetWithContent(
          tweet = Tweet(
            id = "1550874190793674756",
            authorId = "280595048",
            conversationId = "1550874190793674753",
            inReplyToUserId = null,
            text = "Tweet 2 in the thread",
            createdAt = Instant.parse("2022-07-23T16:10:15Z"),
            deviceCreatedAt = Instant.parse("2022-08-01T00:00:00Z"),
            publicMetrics = PublicMetrics(
              retweetCount = 0,
              replyCount = 10,
              likeCount = 25,
              quoteCount = 0
            )
          ),
          entities = emptyList(),
          referencedTweets = listOf(
            ReferencedTweet(
              id = "2387730861057376900",
              type = ReferenceType.Quoted,
              tweetId = "1550874190793674756",
              conversationId = "1550874190793674753"
            )
          ),
          media = emptyList(),
          polls = emptyList()
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
  fun syncing_a_conversation_without_an_head_tweet_should_work_correctly() = runTest {
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
        createdAt = Instant.parse("2022-07-23T16:03:15Z"),
        publicMetrics = PublicMetrics(
          retweetCount = 0,
          replyCount = 10,
          likeCount = 25,
          quoteCount = 0
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
      )
    )

    val conversationLookupPayload = ConversationsLookupPayload(
      data = listOf(
        TweetPayload(
          id = "1550874190793674759",
          authorId = "280595048",
          conversationId = "1550874190793674753",
          text = "Tweet 4 in the thread",
          attachments = null,
          entities = null,
          inReplyToUserId = null,
          referencedTweets = null,
          createdAt = Instant.parse("2022-07-23T16:10:15Z"),
          publicMetrics = PublicMetrics(
            retweetCount = 0,
            replyCount = 10,
            likeCount = 25,
            quoteCount = 0
          )
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
    val result = conversationSync.trySync(tweetId = "1550874190793674757")

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
          conversationPreviewText = "Tweet 3 in the thread",
          conversationStartedAt = Instant.parse("2022-07-23T16:03:15Z"),
          conversationCreatedAt = Instant.parse("2022-08-01T00:00:00Z"),
          username = "its_sasikanth",
          userFullName = "Sasikanth",
          userProfileImage = "https://pbs.twimg.com/profile_images/1535630758777602050/q1qaITTW_normal.jpg",
          numberOfTweetsInConversation = 2
        )
      )
    )
  }

  @Test
  fun syncing_a_tweet_without_conversation_should_sync_tweet_and_users() = runTest {
    // given
    val tweetLookupPayload = TweetLookupPayload(
      data = TweetPayload(
        id = "7735261120010996230",
        authorId = "280595048",
        conversationId = "2050090618859903104",
        text = "Tweet 1 in the thread",
        attachments = null,
        entities = null,
        inReplyToUserId = null,
        referencedTweets = null,
        createdAt = Instant.parse("2022-07-23T16:03:15Z"),
        publicMetrics = PublicMetrics(
          retweetCount = 0,
          replyCount = 10,
          likeCount = 25,
          quoteCount = 0
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
      )
    )

    fakeTwitterRemoteSource.addTweetLookupPayload(tweetLookupPayload)

    // when
    val result = conversationSync.trySync(tweetId = "7735261120010996230")

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
          conversationId = "2050090618859903104",
          conversationPreviewText = "Tweet 1 in the thread",
          conversationStartedAt = Instant.parse("2022-07-23T16:03:15Z"),
          conversationCreatedAt = Instant.parse("2022-08-01T00:00:00Z"),
          username = "its_sasikanth",
          userFullName = "Sasikanth",
          userProfileImage = "https://pbs.twimg.com/profile_images/1535630758777602050/q1qaITTW_normal.jpg",
          numberOfTweetsInConversation = 1
        )
      )
    )
  }

  @Test
  fun ignore_referenced_tweets_that_are_already_in_the_conversation() = runTest {
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
        createdAt = Instant.parse("2022-07-23T16:03:15Z"),
        publicMetrics = PublicMetrics(
          retweetCount = 0,
          replyCount = 10,
          likeCount = 25,
          quoteCount = 0
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
          referencedTweets = listOf(
            ReferencedTweetPayload(
              id = "1550874190793674753",
              type = ReferenceTypePayload.RepliedTo
            )
          ),
          createdAt = Instant.parse("2022-07-23T16:10:15Z"),
          publicMetrics = PublicMetrics(
            retweetCount = 0,
            replyCount = 10,
            likeCount = 25,
            quoteCount = 0
          )
        )
      ),
      includes = null,
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

    val tweetsInConversation = tweetsRepository.tweetsInConversation(
      conversationId = "1550874190793674753"
    ).first()

    // then
    assertThat(result).isEqualTo(Response.Success)
    assertThat(recentConversationsPage.data).isEqualTo(
      listOf(
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
    )
    assertThat(tweetsInConversation).isEqualTo(
      listOf(
        TweetWithContent(
          tweet = Tweet(
            id = "1550874190793674753",
            authorId = "280595048",
            conversationId = "1550874190793674753",
            inReplyToUserId = null,
            text = "Tweet 1 in the thread",
            createdAt = Instant.parse("2022-07-23T16:03:15Z"),
            deviceCreatedAt = Instant.parse("2022-08-01T00:00:00Z"),
            publicMetrics = PublicMetrics(
              retweetCount = 0,
              replyCount = 10,
              likeCount = 25,
              quoteCount = 0
            )
          ),
          entities = emptyList(),
          referencedTweets = emptyList(),
          media = emptyList(),
          polls = emptyList()
        ),
        TweetWithContent(
          tweet = Tweet(
            id = "1550874190793674756",
            authorId = "280595048",
            conversationId = "1550874190793674753",
            inReplyToUserId = null,
            text = "Tweet 2 in the thread",
            createdAt = Instant.parse("2022-07-23T16:10:15Z"),
            deviceCreatedAt = Instant.parse("2022-08-01T00:00:00Z"),
            publicMetrics = PublicMetrics(
              retweetCount = 0,
              replyCount = 10,
              likeCount = 25,
              quoteCount = 0
            )
          ),
          entities = emptyList(),
          referencedTweets = emptyList(),
          media = emptyList(),
          polls = emptyList()
        )
      )
    )
  }
}
