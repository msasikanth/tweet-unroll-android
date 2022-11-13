package dev.sasikanth.twine.home

data class HomeUiState(
  val tweetUrl: String?,
  val inputErrors: List<InputError>
) {

  companion object {
    val DEFAULT = HomeUiState(
      tweetUrl = null,
      inputErrors = emptyList()
    )
  }

  fun onTweetUrlChanged(tweetUrl: String?): HomeUiState {
    return copy(tweetUrl = tweetUrl, inputErrors = emptyList())
  }

  fun invalidUrl(): HomeUiState {
    return copy(inputErrors = listOf(InvalidUrl))
  }
}
