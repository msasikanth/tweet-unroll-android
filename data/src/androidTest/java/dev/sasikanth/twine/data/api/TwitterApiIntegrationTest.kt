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
class TwitterApiIntegrationTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var twitterApi: TwitterApi

  @Before
  fun setup() {
    hiltRule.inject()
  }

  @Test
  fun fetching_a_text_only_tweet_should_work_correctly() = runTest {
    // when
    val tweetLookupPayload = twitterApi.tweetLookup(id = "1550874190793674753")

    // then
    assertThat(tweetLookupPayload?.data).isNotNull()
    assertThat(tweetLookupPayload?.includes?.users).isNotEmpty()
  }

  @Test
  fun fetching_a_tweet_with_media_should_work_correctly() = runTest {
    // when
    val tweetLookupPayload = twitterApi.tweetLookup(id = "1257031096249679872")

    // then
    assertThat(tweetLookupPayload?.data).isNotNull()
    assertThat(tweetLookupPayload?.includes?.media).isNotNull()
  }

  @Test
  fun fetching_a_tweet_with_poll_should_work_correctly() = runTest {
    // when
    val tweetLookupPayload = twitterApi.tweetLookup(id = "1551053769932419072")

    // then
    assertThat(tweetLookupPayload?.data).isNotNull()
    assertThat(tweetLookupPayload?.includes?.polls).isNotEmpty()
  }

  @Test
  fun fetching_a_tweet_referencing_another_tweet_should_work_correctly() = runTest {
    // when
    val tweetLookupPayload = twitterApi.tweetLookup(id = "1550400941673050112")

    // then
    assertThat(tweetLookupPayload?.data).isNotNull()
    assertThat(tweetLookupPayload?.includes?.tweets).isNotNull()
  }

  /**
   * Since this API endpoint is time sensitive, we are not verifying the data received.
   * We are just checking if the API call is successfully made.
   */
  @Test
  fun conversation_lookup_api_should_work_correctly() = runTest {
    // given
    val query = "conversation_id:1550867609368686593 from:17424947 to:17424947"

    // when
    val conversationLookupPayload = twitterApi.conversationsLookup(query = query)

    // then
    assertThat(conversationLookupPayload).isNotNull()
  }
}
