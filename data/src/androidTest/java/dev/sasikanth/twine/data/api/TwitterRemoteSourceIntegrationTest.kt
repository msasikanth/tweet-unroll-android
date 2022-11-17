package dev.sasikanth.twine.data.api

import androidx.test.filters.FlakyTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sasikanth.twine.data.api.models.AttachmentsPayload
import dev.sasikanth.twine.data.api.models.IncludesPayload
import dev.sasikanth.twine.data.api.models.MediaPayload
import dev.sasikanth.twine.data.api.models.MediaTypePayload
import dev.sasikanth.twine.data.api.models.PollOptionPayload
import dev.sasikanth.twine.data.api.models.PollPayload
import dev.sasikanth.twine.data.api.models.PublicMetrics
import dev.sasikanth.twine.data.api.models.ReferenceTypePayload
import dev.sasikanth.twine.data.api.models.ReferencedTweetPayload
import dev.sasikanth.twine.data.api.models.TweetEntitiesPayload
import dev.sasikanth.twine.data.api.models.TweetEntityPayload
import dev.sasikanth.twine.data.api.models.TweetLookupPayload
import dev.sasikanth.twine.data.api.models.TweetPayload
import dev.sasikanth.twine.data.api.models.UserPayload
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import javax.inject.Inject

@HiltAndroidTest
@FlakyTest(
  detail = """
    Inherently this test is flaky because it depends on API data from Twitter 
    which means any data that I have specified here might change. Things like 
    public metrics, quoted tweets, etc.,
  """
)
class TwitterRemoteSourceIntegrationTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var twitterRemoteSource: TwitterRemoteSourceImpl

  @Before
  fun setup() {
    hiltRule.inject()
  }

  @Test
  fun fetching_a_text_only_tweet_should_work_correctly() = runTest {
    // when
    val tweetLookupPayload = twitterRemoteSource.tweetLookup(id = "1550874190793674753")

    // then
    assertThat(tweetLookupPayload).isEqualTo(
      TweetLookupPayload(
        data = TweetPayload(
          id = "1550874190793674753",
          authorId = "280595048",
          conversationId = "1550874190793674753",
          text = "Today has been the most productive weekend I had in a while. Was able to make decent progress on my side project 🙌🏾",
          attachments = null,
          entities = null,
          inReplyToUserId = null,
          referencedTweets = null,
          createdAt = Instant.parse("2022-07-23T16:03:15Z"),
          publicMetrics = PublicMetrics(
            retweetCount = 0,
            replyCount = 0,
            likeCount = 2,
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
    )
  }

  @Test
  fun fetching_a_tweet_with_media_should_work_correctly() = runTest {
    // when
    val tweetLookupPayload = twitterRemoteSource.tweetLookup(id = "1257031096249679872")

    // then
    assertThat(tweetLookupPayload).isEqualTo(
      TweetLookupPayload(
        data = TweetPayload(
          id = "1257031096249679872",
          authorId = "280595048",
          conversationId = "1257031096249679872",
          text = """
            Meet Pinnit!

            • Create &amp; pin notes as notifications. They will be recreated as permanent notifications in your notification panel!

            Much more to come: Full app notification history support, tracking and filtering!

            Designed by @edpratti 

            https://t.co/FiYehtcERY

            #MaterialDesign https://t.co/kqcQ7SqFQf
          """.trimIndent(),
          attachments = AttachmentsPayload(
            mediaKeys = listOf("3_1257031093653434368"),
            pollIds = null
          ),
          entities = TweetEntitiesPayload(
            urls = listOf(
              TweetEntityPayload(
                start = 243,
                end = 266,
                displayUrl = "play.google.com/store/apps/det…",
                expandedUrl = "https://play.google.com/store/apps/details?id=dev.sasikanth.pinnit"
              ),
              TweetEntityPayload(
                start = 284,
                end = 307,
                displayUrl = "pic.twitter.com/kqcQ7SqFQf",
                expandedUrl = "https://twitter.com/its_sasikanth/status/1257031096249679872/photo/1"
              )
            )
          ),
          inReplyToUserId = null,
          referencedTweets = null,
          createdAt = Instant.parse("2020-05-03T19:35:42Z"),
          publicMetrics = PublicMetrics(
            retweetCount = 29,
            replyCount = 13,
            likeCount = 122,
            quoteCount = 7
          )
        ),
        includes = IncludesPayload(
          tweets = null,
          media = listOf(
            MediaPayload(
              mediaKey = "3_1257031093653434368",
              type = MediaTypePayload.Photo,
              url = "https://pbs.twimg.com/media/EXHfHqDVcAAXpX3.jpg",
              previewImage = null
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
    )
  }

  @Test
  fun fetching_a_tweet_with_poll_should_work_correctly() = runTest {
    // when
    val tweetLookupPayload = twitterRemoteSource.tweetLookup(id = "1551053769932419072")

    // then
    assertThat(tweetLookupPayload).isEqualTo(
      TweetLookupPayload(
        data = TweetPayload(
          id = "1551053769932419072",
          authorId = "280595048",
          conversationId = "1551053769932419072",
          text = "*ignore*",
          attachments = AttachmentsPayload(
            mediaKeys = null,
            pollIds = listOf("1551053769877889024")
          ),
          entities = null,
          inReplyToUserId = null,
          referencedTweets = null,
          createdAt = Instant.parse("2022-07-24T03:56:50Z"),
          publicMetrics = PublicMetrics(
            retweetCount = 0,
            replyCount = 1,
            likeCount = 0,
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
          polls = listOf(
            PollPayload(
              id = "1551053769877889024",
              options = listOf(
                PollOptionPayload(
                  position = 1,
                  label = "Yes",
                  votes = 4
                ),
                PollOptionPayload(
                  position = 2,
                  label = "No",
                  votes = 5
                )
              )
            )
          )
        )
      )
    )
  }

  @Test
  fun fetching_a_tweet_referencing_another_tweet_should_work_correctly() = runTest {
    // when
    val tweetLookupPayload = twitterRemoteSource.tweetLookup(id = "1550400941673050112")

    // then
    assertThat(tweetLookupPayload).isEqualTo(
      TweetLookupPayload(
        data = TweetPayload(
          id = "1550400941673050112",
          authorId = "280595048",
          conversationId = "1550400941673050112",
          text = "Nice. Now let's see how long it will take before someone does this with Compose UI 😂 https://t.co/6dMCeffykA",
          attachments = null,
          entities = TweetEntitiesPayload(
            urls = listOf(
              TweetEntityPayload(
                start = 85,
                end = 108,
                displayUrl = "twitter.com/philipcdavis/s…",
                expandedUrl = "https://twitter.com/philipcdavis/status/1550133881168269312"
              )
            )
          ),
          inReplyToUserId = null,
          referencedTweets = listOf(
            ReferencedTweetPayload(
              id = "1550133881168269312",
              type = ReferenceTypePayload.Quoted
            )
          ),
          createdAt = Instant.parse("2022-07-22T08:42:44Z"),
          publicMetrics = PublicMetrics(
            retweetCount = 0,
            replyCount = 0,
            likeCount = 4,
            quoteCount = 0
          )
        ),
        includes = IncludesPayload(
          tweets = listOf(
            TweetPayload(
              id = "1550133881168269312",
              authorId = "19266100",
              conversationId = "1550133881168269312",
              text = "Here’s a cloth effect made in SwiftUI https://t.co/5KzWB0IkCh",
              attachments = AttachmentsPayload(
                mediaKeys = listOf("7_1550133776134463496"),
                pollIds = null
              ),
              entities = TweetEntitiesPayload(
                urls = listOf(
                  TweetEntityPayload(
                    start = 38,
                    end = 61,
                    displayUrl = "pic.twitter.com/5KzWB0IkCh",
                    expandedUrl = "https://twitter.com/philipcdavis/status/1550133881168269312/video/1"
                  )
                )
              ),
              inReplyToUserId = null,
              referencedTweets = null,
              createdAt = Instant.parse("2022-07-21T15:01:32Z"),
              publicMetrics = PublicMetrics(
                retweetCount = 100,
                replyCount = 24,
                likeCount = 1966,
                quoteCount = 9
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
            ),
            UserPayload(
              id = "19266100",
              name = "Philip Davis",
              username = "philipcdavis",
              profileImage = "https://pbs.twimg.com/profile_images/659824786260934656/syJKampr_normal.jpg"
            )
          ),
          polls = null
        )
      )
    )
  }
}
