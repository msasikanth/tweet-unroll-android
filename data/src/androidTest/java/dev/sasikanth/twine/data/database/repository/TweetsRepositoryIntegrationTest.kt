package dev.sasikanth.twine.data.database.repository

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import javax.inject.Inject

@HiltAndroidTest
class TweetsRepositoryIntegrationTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var repository: TweetsRepository

  @Before
  fun setup() {
    hiltRule.inject()
  }

  @Test
  fun getting_recent_conversations_should_work_correctly() = runTest {
    // given
    val author1Id = "280595048"
    val author2Id = "774149244"

    val user1 = User(
      id = author1Id,
      name = "Sasikanth",
      username = "its_sasikanth",
      profileImage = "https://twitter.com/image/its_sasikanth.png"
    )

    val user2 = User(
      id = author2Id,
      name = "Random User",
      username = "its_a_random",
      profileImage = "https://twitter.com/image/random_user.png"
    )

    val conversation1Id = "7258469438729982175"
    val conversation2Id = "5139342649340358390"

    val tweetsFromConversation1 = listOf(
      Tweet(
        id = "7258469438729982175",
        authorId = author1Id,
        conversationId = conversation1Id,
        inReplyToUserId = null,
        text = "Tweet 1 from conversation 1",
        createdAt = LocalDateTime.parse("2022-01-01T10:00:00")
      ),
      Tweet(
        id = "9025201324322053244",
        authorId = author1Id,
        conversationId = conversation1Id,
        inReplyToUserId = null,
        text = "Tweet 2 from conversation 1",
        createdAt = LocalDateTime.parse("2022-01-01T10:05:00")
      )
    )

    val tweetsFromConversation2 = listOf(
      Tweet(
        id = "5139342649340358390",
        authorId = author2Id,
        conversationId = conversation2Id,
        inReplyToUserId = null,
        text = "Tweet 1 from conversation 2",
        createdAt = LocalDateTime.parse("2022-02-01T10:00:00")
      ),
      Tweet(
        id = "6288716957019134084",
        authorId = author2Id,
        conversationId = conversation2Id,
        inReplyToUserId = null,
        text = "Tweet 2 from conversation 2",
        createdAt = LocalDateTime.parse("2022-02-01T10:05:00")
      ),
      Tweet(
        id = "1756291676301077873",
        authorId = author2Id,
        conversationId = conversation2Id,
        inReplyToUserId = null,
        text = "Tweet 3 from conversation 2",
        createdAt = LocalDateTime.parse("2022-02-01T10:11:00")
      )
    )

    repository.saveUsers(listOf(user1, user2))
    repository.saveTweets(tweetsFromConversation1 + tweetsFromConversation2)

    // when
    val recentTweets = repository.recentConversations().first()

    // then
    assertThat(recentTweets).containsExactly(
      RecentConversation(
        conversationId = conversation2Id,
        conversationPreviewText = "Tweet 1 from conversation 2",
        conversationStartedAt = LocalDateTime.parse("2022-02-01T10:00:00"),
        username = "its_a_random",
        userFullName = "Random User",
        userProfileImage = "https://twitter.com/image/random_user.png",
        numberOfTweetsInConversation = 3
      ),
      RecentConversation(
        conversationId = conversation1Id,
        conversationPreviewText = "Tweet 1 from conversation 1",
        conversationStartedAt = LocalDateTime.parse("2022-01-01T10:00:00"),
        username = "its_sasikanth",
        userFullName = "Sasikanth",
        userProfileImage = "https://twitter.com/image/its_sasikanth.png",
        numberOfTweetsInConversation = 2
      )
    )
  }
}
