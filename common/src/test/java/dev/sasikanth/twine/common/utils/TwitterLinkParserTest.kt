package dev.sasikanth.twine.common.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TwitterLinkParserTest {

  private val linkParser = TwitterLinkParser()

  @Test
  fun `getting id from the tweet link should work correctly`() {
    // given
    val tweetLink = "https://twitter.com/its_sasikanth/status/1588742946387824640"

    // when
    val id = linkParser.getId(tweetLink)

    // then
    assertThat(id).isEqualTo("1588742946387824640")
  }

  @Test
  fun `getting username from the tweet link should work correctly`() {
    // given
    val tweetLink = "https://twitter.com/its_sasikanth/status/1588742946387824640"

    // when
    val userName = linkParser.getUserName(tweetLink)

    // then
    assertThat(userName).isEqualTo("its_sasikanth")
  }
}
