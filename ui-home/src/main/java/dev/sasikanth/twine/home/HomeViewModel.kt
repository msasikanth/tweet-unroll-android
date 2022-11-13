package dev.sasikanth.twine.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sasikanth.twine.common.utils.TweetLinkParser
import dev.sasikanth.twine.data.clipboard.Clipboard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val clipboard: Clipboard,
  private val tweetLinkParser: TweetLinkParser
) : ViewModel() {

  private val defaultUiState = HomeUiState.DEFAULT
  private val _homeUiState = MutableStateFlow(defaultUiState)
  val homeUiState: StateFlow<HomeUiState>
    get() = _homeUiState.asStateFlow()

  fun tweetUrlChanged(tweetUrl: String?) {
    _homeUiState.update {
      it.onTweetUrlChanged(tweetUrl = tweetUrl)
    }
  }

  fun pasteUrl() {
    tweetUrlChanged(tweetUrl = clipboard.content)
  }

  fun clearUrl() {
    tweetUrlChanged(tweetUrl = null)
  }

  fun validateUrl() {
    val tweetLink = _homeUiState.value.tweetUrl.orEmpty()
    val isAValidTweetLink = tweetLinkParser.isAValidTweetUrl(tweetLink = tweetLink)

    if (!isAValidTweetLink) {
      _homeUiState.update { it.invalidUrl() }
    }
  }
}
