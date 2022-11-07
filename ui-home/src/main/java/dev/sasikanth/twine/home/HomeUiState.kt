package dev.sasikanth.twine.home

data class HomeUiState(
  val tweetUrl: String?
) {

  companion object {
    val DEFAULT = HomeUiState(
      tweetUrl = null
    )
  }

  fun onTweetUrlChanged(tweetUrl: String): HomeUiState {
    return copy(tweetUrl = tweetUrl)
  }
}
