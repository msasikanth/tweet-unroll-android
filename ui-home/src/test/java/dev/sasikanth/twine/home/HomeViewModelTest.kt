package dev.sasikanth.twine.home

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HomeViewModelTest {

  @Test
  fun `when tweet url is changed, then update the state`() {
    // given
    val defaultUiState = HomeUiState.DEFAULT
    val viewModel = HomeViewModel()
    val tweetUrl = "https://twitter.com/its_sasikanth/status/1588742946387824640"

    // when
    viewModel.tweetUrlChanged(tweetUrl = tweetUrl)

    // then
    val expectedUiState = defaultUiState.onTweetUrlChanged(tweetUrl)
    assertThat(viewModel.homeUiState.value).isEqualTo(expectedUiState)
  }
}
