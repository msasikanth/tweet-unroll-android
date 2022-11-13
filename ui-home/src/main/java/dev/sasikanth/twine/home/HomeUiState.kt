package dev.sasikanth.twine.home

data class HomeUiState(
  val tweetUrl: String?,
  val errors: List<InputError>
) {

  companion object {
    val DEFAULT = HomeUiState(
      tweetUrl = null,
      errors = emptyList()
    )
  }

  fun onTweetUrlChanged(tweetUrl: String?): HomeUiState {
    return copy(tweetUrl = tweetUrl, errors = emptyList())
  }

  fun invalidUrl(): HomeUiState {
    return copy(errors = listOf(InvalidUrl))
  }
}
