package dev.sasikanth.twine.data.database.repository

import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult.Page
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sasikanth.twine.data.api.models.PublicMetrics
import dev.sasikanth.twine.data.database.TwineDatabase
import dev.sasikanth.twine.data.database.entities.Media
import dev.sasikanth.twine.data.database.entities.MediaType
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.entities.ReferenceType
import dev.sasikanth.twine.data.database.entities.ReferencedTweet
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.TweetWithContent
import dev.sasikanth.twine.data.database.entities.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import javax.inject.Inject

@HiltAndroidTest
class TweetsRepositoryIntegrationTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var tweetsRepository: TweetsRepositoryImpl

  @Inject
  lateinit var database: TwineDatabase

  @Before
  fun setup() {
    hiltRule.inject()
  }

  @After
  fun tearDown() {
    database.clearAllTables()
  }

  @Test
  fun getting_recent_conversations_should_work_correctly() = runTest {
    // given
    val author1Id = "280595048"
    val author2Id = "774149244"

    val conversation1Id = "7258469438729982175"
    val conversation2Id = "5139342649340358390"

    val user1 = User(
      id = author1Id,
      name = "Sasikanth",
      username = "its_sasikanth",
      profileImage = "https://twitter.com/image/its_sasikanth.png",
      conversationId = conversation1Id
    )

    val user2 = User(
      id = author2Id,
      name = "Random User",
      username = "its_a_random",
      profileImage = "https://twitter.com/image/random_user.png",
      conversationId = conversation2Id
    )

    val tweetsFromConversation1 = listOf(
      Tweet(
        id = "7258469438729982175",
        authorId = author1Id,
        conversationId = conversation1Id,
        inReplyToUserId = null,
        text = "Tweet 1 from conversation 1",
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
        authorId = author1Id,
        conversationId = conversation1Id,
        inReplyToUserId = null,
        text = "Tweet 2 from conversation 1",
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

    val tweetsFromConversation2 = listOf(
      Tweet(
        id = "5139342649340358390",
        authorId = author2Id,
        conversationId = conversation2Id,
        inReplyToUserId = null,
        text = "Tweet 1 from conversation 2",
        createdAt = Instant.parse("2022-02-01T10:00:00Z"),
        deviceCreatedAt = Instant.parse("2022-02-02T00:00:00Z"),
        publicMetrics = PublicMetrics(
          retweetCount = 0,
          replyCount = 4,
          likeCount = 15,
          quoteCount = 2
        )
      ),
      Tweet(
        id = "6288716957019134084",
        authorId = author2Id,
        conversationId = conversation2Id,
        inReplyToUserId = null,
        text = "Tweet 2 from conversation 2",
        createdAt = Instant.parse("2022-02-01T10:05:00Z"),
        deviceCreatedAt = Instant.parse("2022-02-02T00:00:00Z"),
        publicMetrics = PublicMetrics(
          retweetCount = 0,
          replyCount = 4,
          likeCount = 15,
          quoteCount = 2
        )
      ),
      Tweet(
        id = "1756291676301077873",
        authorId = author2Id,
        conversationId = conversation2Id,
        inReplyToUserId = null,
        text = "Tweet 3 from conversation 2",
        createdAt = Instant.parse("2022-02-01T10:11:00Z"),
        deviceCreatedAt = Instant.parse("2022-02-02T00:00:00Z"),
        publicMetrics = PublicMetrics(
          retweetCount = 0,
          replyCount = 4,
          likeCount = 15,
          quoteCount = 2
        )
      )
    )

    tweetsRepository.saveUsers(listOf(user1, user2))
    tweetsRepository.saveTweets(tweetsFromConversation1 + tweetsFromConversation2)

    // when
    val recentTweetsLoadResult = tweetsRepository
      .recentConversations()
      .load(
        Refresh(
          key = null,
          loadSize = 10,
          placeholdersEnabled = false
        )
      )
    val recentTweets = (recentTweetsLoadResult as Page<Int, RecentConversation>).data

    // then
    assertThat(recentTweets).isEqualTo(
      listOf(
        RecentConversation(
          conversationId = conversation2Id,
          conversationPreviewText = "Tweet 1 from conversation 2",
          conversationStartedAt = Instant.parse("2022-02-01T10:00:00Z"),
          conversationCreatedAt = Instant.parse("2022-02-02T00:00:00Z"),
          username = "its_a_random",
          userFullName = "Random User",
          userProfileImage = "https://twitter.com/image/random_user.png",
          numberOfTweetsInConversation = 3
        ),
        RecentConversation(
          conversationId = conversation1Id,
          conversationPreviewText = "Tweet 1 from conversation 1",
          conversationStartedAt = Instant.parse("2022-01-01T10:00:00Z"),
          conversationCreatedAt = Instant.parse("2022-01-02T00:00:00Z"),
          username = "its_sasikanth",
          userFullName = "Sasikanth",
          userProfileImage = "https://twitter.com/image/its_sasikanth.png",
          numberOfTweetsInConversation = 2
        )
      )
    )
  }

  @Test
  fun getting_tweets_in_a_conversation_should_work_correctly() = runTest {
    // given
    val conversation1 = "8128951476496855206"
    val conversation2 = "5720678304432505288"

    val tweet1FromConversation1 = Tweet(
      id = "8128951476496855206",
      authorId = "475050358",
      conversationId = conversation1,
      inReplyToUserId = null,
      text = "Tweet 1 from conversation 1",
      createdAt = Instant.parse("2022-01-01T10:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-02T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val tweet2FromConversation1 = Tweet(
      id = "4917848029302871597",
      authorId = "475050358",
      conversationId = conversation1,
      inReplyToUserId = null,
      text = "Tweet 2 from conversation 1",
      createdAt = Instant.parse("2022-01-01T10:05:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-02T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val tweet1FromConversation2 = Tweet(
      id = "5720678304432505288",
      authorId = "919217562",
      conversationId = conversation2,
      inReplyToUserId = null,
      text = "Tweet 1 from conversation 2",
      createdAt = Instant.parse("2022-01-01T10:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-02T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    tweetsRepository.saveTweets(
      listOf(
        tweet1FromConversation1,
        tweet2FromConversation1,
        tweet1FromConversation2
      )
    )

    // when
    val tweetsInConversation = tweetsRepository.tweetsInConversation(conversation1).first()

    // then
    assertThat(tweetsInConversation).isEqualTo(
      listOf(
        TweetWithContent(
          tweet = tweet1FromConversation1,
          entities = emptyList(),
          referencedTweets = emptyList(),
          media = emptyList(),
          polls = emptyList()
        ),
        TweetWithContent(
          tweet = tweet2FromConversation1,
          entities = emptyList(),
          referencedTweets = emptyList(),
          media = emptyList(),
          polls = emptyList()
        )
      )
    )
  }

  @Test
  fun filter_tweets_if_they_are_referenced_tweets_when_fetching_recent_conversations() = runTest {
    // given
    val tweet1 = Tweet(
      id = "5826750211618182376",
      authorId = "621146655",
      conversationId = "5826750211618182376",
      inReplyToUserId = null,
      text = "Tweet 1 in thread, with quoted tweet",
      createdAt = Instant.parse("2022-01-07T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-07T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val tweet2 = Tweet(
      id = "5826750211618182378",
      authorId = "621146655",
      conversationId = "5826750211618182376",
      inReplyToUserId = null,
      text = "Tweet 2 in the thread",
      createdAt = Instant.parse("2022-01-07T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-07T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val quotedTweet = Tweet(
      id = "2384818331972948098",
      authorId = "393780059",
      conversationId = "2384818331972948098",
      inReplyToUserId = null,
      text = "Quoted tweet",
      createdAt = Instant.parse("2022-01-06T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-06T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val referencedTweet = ReferencedTweet(
      id = "2384818331972948098",
      type = ReferenceType.Quoted,
      tweetId = "5826750211618182376",
      conversationId = "5826750211618182376"
    )

    val user1 = User(
      id = "621146655",
      name = "Sasikanth",
      username = "its_sasikanth",
      profileImage = "https://twitter.com/image/its_sasikanth.png",
      conversationId = "5826750211618182376"
    )

    val user2 = User(
      id = "393780059",
      name = "Ramesh",
      username = "ramesh_prasad",
      profileImage = "https://twitter.com/image/ramesh_prasad.png",
      conversationId = "5826750211618182376"
    )

    tweetsRepository.saveTweets(listOf(tweet1, tweet2, quotedTweet))
    tweetsRepository.saveReferencedTweets(listOf(referencedTweet))
    tweetsRepository.saveUsers(listOf(user1, user2))

    // when
    val recentTweetsLoadResult = tweetsRepository
      .recentConversations()
      .load(
        Refresh(
          key = null,
          loadSize = 10,
          placeholdersEnabled = false
        )
      ) as Page<Int, RecentConversation>

    val recentTweets = recentTweetsLoadResult.data

    // then
    assertThat(recentTweets).isEqualTo(
      listOf(
        RecentConversation(
          conversationId = "5826750211618182376",
          conversationPreviewText = "Tweet 1 in thread, with quoted tweet",
          conversationStartedAt = Instant.parse("2022-01-07T00:00:00Z"),
          conversationCreatedAt = Instant.parse("2022-01-07T00:00:00Z"),
          username = "its_sasikanth",
          userFullName = "Sasikanth",
          userProfileImage = "https://twitter.com/image/its_sasikanth.png",
          numberOfTweetsInConversation = 2
        )
      )
    )
  }

  @Test
  fun deleting_conversation_should_delete_all_tweets_associated_content_in_it() = runTest {
    // given
    val tweet1 = Tweet(
      id = "5826750211618182376",
      authorId = "621146655",
      conversationId = "5826750211618182376",
      inReplyToUserId = null,
      text = "Tweet 1 in thread",
      createdAt = Instant.parse("2022-01-07T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-07T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val tweet2 = Tweet(
      id = "5826750211618182378",
      authorId = "621146655",
      conversationId = "5826750211618182376",
      inReplyToUserId = null,
      text = "Tweet 2 in the thread",
      createdAt = Instant.parse("2022-01-07T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-07T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val tweet2Media = Media(
      mediaKey = "3_7291856149861120695",
      type = MediaType.Photo,
      url = "https://twitter.com/img/photo.jpg",
      previewImage = "https://twitter.com/img/photo_preview.jpg",
      tweetId = "5826750211618182378"
    )

    val user1 = User(
      id = "621146655",
      name = "Sasikanth",
      username = "its_sasikanth",
      profileImage = "https://twitter.com/image/its_sasikanth.png",
      conversationId = "5826750211618182376"
    )

    tweetsRepository.saveTweets(listOf(tweet1, tweet2))
    tweetsRepository.saveMedia(listOf(tweet2Media))
    tweetsRepository.saveUsers(listOf(user1))

    val tweetsInConversationBeforeDelete = tweetsRepository.tweetsInConversation(
      conversationId = "5826750211618182376"
    ).first()
    assertThat(tweetsInConversationBeforeDelete).isEqualTo(
      listOf(
        TweetWithContent(
          tweet = tweet1,
          entities = emptyList(),
          referencedTweets = emptyList(),
          media = emptyList(),
          polls = emptyList()
        ),
        TweetWithContent(
          tweet = tweet2,
          entities = emptyList(),
          referencedTweets = emptyList(),
          media = listOf(tweet2Media),
          polls = emptyList()
        )
      )
    )

    // when
    tweetsRepository.deleteConversation(conversationId = "5826750211618182376")

    // then
    val tweetsInConversationAfterDelete = tweetsRepository.tweetsInConversation(
      conversationId = "5826750211618182376"
    ).first()
    assertThat(tweetsInConversationAfterDelete).isEmpty()
  }

  @Test
  fun deleting_conversation_should_delete_referenced_tweets_in_the_conversation() = runTest {
    // given
    val tweet1 = Tweet(
      id = "5826750211618182376",
      authorId = "621146655",
      conversationId = "5826750211618182376",
      inReplyToUserId = null,
      text = "Tweet 1 in thread, with quoted tweet",
      createdAt = Instant.parse("2022-01-07T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-07T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val tweet2 = Tweet(
      id = "5826750211618182378",
      authorId = "621146655",
      conversationId = "5826750211618182376",
      inReplyToUserId = null,
      text = "Tweet 2 in the thread",
      createdAt = Instant.parse("2022-01-07T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-07T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val quotedTweet = Tweet(
      id = "5826750211618182377",
      authorId = "393780059",
      conversationId = "5826750211618182377",
      inReplyToUserId = null,
      text = "Quoted tweet",
      createdAt = Instant.parse("2018-01-01T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2018-01-01T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val tweet2Media = Media(
      mediaKey = "3_7291856149861120695",
      type = MediaType.Photo,
      url = "https://twitter.com/img/photo.jpg",
      previewImage = "https://twitter.com/img/photo_preview.jpg",
      tweetId = "5826750211618182378"
    )

    val user1 = User(
      id = "621146655",
      name = "Sasikanth",
      username = "its_sasikanth",
      profileImage = "https://twitter.com/image/its_sasikanth.png",
      conversationId = "5826750211618182376"
    )

    val referencedTweetUser = User(
      id = "393780059",
      name = "Random User",
      username = "RandomUser",
      profileImage = "https://twitter.com/image/random_user.png",
      conversationId = "5826750211618182376"
    )

    val referencedTweet = ReferencedTweet(
      id = "5826750211618182377",
      type = ReferenceType.Quoted,
      tweetId = "5826750211618182376",
      conversationId = "5826750211618182376"
    )

    tweetsRepository.saveTweets(listOf(quotedTweet, tweet1, tweet2))
    tweetsRepository.saveMedia(listOf(tweet2Media))
    tweetsRepository.saveReferencedTweets(listOf(referencedTweet))
    tweetsRepository.saveUsers(listOf(user1, referencedTweetUser))

    val tweetsInConversationBeforeDelete = tweetsRepository.tweetsInConversation(
      conversationId = "5826750211618182376"
    ).first()
    assertThat(tweetsInConversationBeforeDelete).isEqualTo(
      listOf(
        TweetWithContent(
          tweet = tweet1,
          entities = emptyList(),
          referencedTweets = listOf(referencedTweet),
          media = emptyList(),
          polls = emptyList()
        ),
        TweetWithContent(
          tweet = tweet2,
          entities = emptyList(),
          referencedTweets = emptyList(),
          media = listOf(tweet2Media),
          polls = emptyList()
        )
      )
    )

    val referencedTweetsInConversationBeforeDelete = tweetsRepository.tweetsInConversation(
      conversationId = "5826750211618182377"
    ).first()
    assertThat(referencedTweetsInConversationBeforeDelete).isEqualTo(
      listOf(
        TweetWithContent(
          tweet = quotedTweet,
          entities = emptyList(),
          referencedTweets = emptyList(),
          media = emptyList(),
          polls = emptyList()
        )
      )
    )

    // when
    tweetsRepository.deleteConversation(conversationId = "5826750211618182376")

    // then
    val tweetsInConversationAfterDelete = tweetsRepository.tweetsInConversation(
      conversationId = "5826750211618182376"
    ).first()
    assertThat(tweetsInConversationAfterDelete).isEmpty()

    val referencedTweetsInConversationAfterDelete = tweetsRepository.tweetsInConversation(
      conversationId = "5826750211618182377"
    ).first()
    assertThat(referencedTweetsInConversationAfterDelete).isEmpty()

    // We are also verifying that the referenced tweet is not showing up in the recent conversations
    // after deleting.
    val recentConversations = tweetsRepository
      .recentConversations()
      .load(
        Refresh(
          key = null,
          loadSize = 10,
          placeholdersEnabled = false
        )
      ) as Page<Int, RecentConversation>

    assertThat(recentConversations.data).isEmpty()
  }

  @Test
  fun deleting_a_conversation_should_delete_users_in_the_conversation() = runTest {
    // given
    val authorInConversation1 = User(
      id = "621146655",
      name = "Sasikanth",
      username = "its_sasikanth",
      profileImage = "https://twitter.com/image/its_sasikanth.png",
      conversationId = "5826750211618182376"
    )

    val authorInConversation2 = User(
      id = "621146656",
      name = "Prakash",
      username = "prakash",
      profileImage = "https://twitter.com/image/prakash.png",
      conversationId = "5826750211618182377"
    )

    val tweetInConversation1 = Tweet(
      id = "5826750211618182376",
      authorId = "621146655",
      conversationId = "5826750211618182376",
      inReplyToUserId = null,
      text = "Tweet 1 in thread 1",
      createdAt = Instant.parse("2022-01-07T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-07T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val tweetInConversation2 = Tweet(
      id = "5826750211618182377",
      authorId = "621146656",
      conversationId = "5826750211618182377",
      inReplyToUserId = null,
      text = "Tweet 1 in thread 2",
      createdAt = Instant.parse("2022-01-07T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-07T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    tweetsRepository.saveUsers(listOf(authorInConversation1, authorInConversation2))
    tweetsRepository.saveTweets(listOf(tweetInConversation1, tweetInConversation2))

    // when
    tweetsRepository.deleteConversation(conversationId = "5826750211618182376")

    // then
    val usersInConversation1 = tweetsRepository.usersInConversation(
      conversationId = "5826750211618182376"
    )
    assertThat(usersInConversation1).isEmpty()

    val usersInConversation2 = tweetsRepository.usersInConversation(
      conversationId = "5826750211618182377"
    )
    assertThat(usersInConversation2).isEqualTo(
      listOf(
        authorInConversation2
      )
    )
  }

  @Test
  fun deleting_a_conversation_should_not_delete_tweets_referenced_in_another_tweet() = runTest {
    // given
    val authorInConversation1 = User(
      id = "621146655",
      name = "Sasikanth",
      username = "its_sasikanth",
      profileImage = "https://twitter.com/image/its_sasikanth.png",
      conversationId = "5826750211618182376"
    )

    val authorInConversation2 = User(
      id = "621146655",
      name = "Sasikanth",
      username = "its_sasikanth",
      profileImage = "https://twitter.com/image/its_sasikanth.png",
      conversationId = "5826750211618182377"
    )

    val tweet1InThread1 = Tweet(
      id = "5826750211618182376",
      authorId = "621146655",
      conversationId = "5826750211618182376",
      inReplyToUserId = null,
      text = "Tweet 1 in thread 1",
      createdAt = Instant.parse("2022-01-07T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-07T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val tweet2InThread1 = Tweet(
      id = "5826750211618182386",
      authorId = "621146655",
      conversationId = "5826750211618182376",
      inReplyToUserId = null,
      text = "Tweet 1 in thread 1",
      createdAt = Instant.parse("2022-01-07T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-07T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val tweetInThread2 = Tweet(
      id = "5826750211618182377",
      authorId = "621146655",
      conversationId = "5826750211618182377",
      inReplyToUserId = null,
      text = "Tweet 2 in thread 1 quoting tweet 1 in thread 1",
      createdAt = Instant.parse("2022-01-07T00:00:00Z"),
      deviceCreatedAt = Instant.parse("2022-01-07T00:00:00Z"),
      publicMetrics = PublicMetrics(
        retweetCount = 0,
        replyCount = 4,
        likeCount = 15,
        quoteCount = 2
      )
    )

    val referencedTweet = ReferencedTweet(
      id = "5826750211618182376",
      type = ReferenceType.Quoted,
      tweetId = "5826750211618182377",
      conversationId = "5826750211618182377"
    )

    tweetsRepository.saveUsers(listOf(authorInConversation1, authorInConversation2))
    tweetsRepository.saveTweets(listOf(tweet1InThread1, tweet2InThread1, tweetInThread2))
    tweetsRepository.saveReferencedTweets(listOf(referencedTweet))

    // when
    tweetsRepository.deleteConversation(conversationId = "5826750211618182376")

    // then
    val tweetsInConversation1 = tweetsRepository.tweetsInConversation(
      conversationId = "5826750211618182376"
    ).first()
    assertThat(tweetsInConversation1).isEqualTo(
      listOf(
        TweetWithContent(
          tweet = tweet1InThread1,
          entities = emptyList(),
          referencedTweets = emptyList(),
          media = emptyList(),
          polls = emptyList()
        )
      )
    )
  }
}
