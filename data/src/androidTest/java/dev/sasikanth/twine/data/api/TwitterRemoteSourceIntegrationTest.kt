package dev.sasikanth.twine.data.api

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class TwitterRemoteSourceIntegrationTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var twitterRemoteSource: TwitterRemoteSource

  @Before
  fun setup() {
    hiltRule.inject()
  }

  @Test
  fun fetching_a_text_only_tweet_should_work_correctly() = runTest {
    // when
    val tweetLookupPayload = twitterRemoteSource.tweetLookup(id = "1550874190793674753")

    // then
    assertThat(tweetLookupPayload?.data?.text).isNotEmpty()
    assertThat(tweetLookupPayload?.includes?.users).isNotEmpty()
    assertThat(tweetLookupPayload?.includes?.media).isNull()
    assertThat(tweetLookupPayload?.includes?.tweets).isNull()
    assertThat(tweetLookupPayload?.includes?.polls).isNull()
  }

  @Test
  fun fetching_a_tweet_with_media_should_work_correctly() = runTest {
    // when
    val tweetLookupPayload = twitterRemoteSource.tweetLookup(id = "1257031096249679872")

    // then
    assertThat(tweetLookupPayload?.data?.attachments?.mediaKeys).isNotEmpty()
    assertThat(tweetLookupPayload?.includes?.users).isNotEmpty()
    assertThat(tweetLookupPayload?.includes?.media).isNotEmpty()
    assertThat(tweetLookupPayload?.includes?.tweets).isNull()
    assertThat(tweetLookupPayload?.includes?.polls).isNull()
  }

  @Test
  fun fetching_a_tweet_with_poll_should_work_correctly() = runTest {
    // when
    val tweetLookupPayload = twitterRemoteSource.tweetLookup(id = "1551053769932419072")

    // then
    assertThat(tweetLookupPayload?.data?.attachments?.pollIds).isNotEmpty()
    assertThat(tweetLookupPayload?.includes?.users).isNotEmpty()
    assertThat(tweetLookupPayload?.includes?.polls).isNotEmpty()
    assertThat(tweetLookupPayload?.includes?.media).isNull()
    assertThat(tweetLookupPayload?.includes?.tweets).isNull()
  }

  @Test
  fun fetching_a_tweet_referencing_another_tweet_should_work_correctly() = runTest {
    // when
    val tweetLookupPayload = twitterRemoteSource.tweetLookup(id = "1550400941673050112")

    // then
    assertThat(tweetLookupPayload?.data?.referencedTweets).isNotEmpty()
    assertThat(tweetLookupPayload?.includes?.tweets).isNotEmpty()
  }

  /**
   * Since this API endpoint is time sensitive, we are not verifying the data received.
   * We are just checking if the API call is successfully made.
   */
  @Test
  fun conversation_lookup_api_should_work_correctly() = runTest {
    // when
    val conversationLookupPayload = twitterRemoteSource.conversationsLookup(
      conversationId = "1550867609368686593",
      authorId = "17424947"
    )

    // then
    assertThat(conversationLookupPayload).isNotNull()
  }
}
