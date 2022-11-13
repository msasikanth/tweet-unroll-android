package dev.sasikanth.twine.home

import com.google.common.truth.Truth.assertThat
import dev.sasikanth.twine.common.testing.data.clipboard.FakeClipboard
import dev.sasikanth.twine.common.utils.TweetLinkParser
import org.junit.Test

class HomeViewModelTest {

  private val fakeClipboard = FakeClipboard()
  private val defaultUiState = HomeUiState.DEFAULT
  private val viewModel = HomeViewModel(
    clipboard = fakeClipboard,
    tweetLinkParser = TweetLinkParser()
  )

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
  fun `when paste button is clicked, then fetch the text from clipboard and update model`() {
    // given
    val tweetUrl = "https://twitter.com/its_sasikanth/status/1588742946387824644"
    fakeClipboard.setText(tweetUrl)

    // when
    viewModel.pasteUrl()

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
    viewModel.validateUrl()

    // then
    val expectedUiState = viewModel.homeUiState.value.invalidUrl()
    assertThat(viewModel.homeUiState.value).isEqualTo(expectedUiState)
  }
}
