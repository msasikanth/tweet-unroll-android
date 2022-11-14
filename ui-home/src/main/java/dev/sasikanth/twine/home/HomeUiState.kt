package dev.sasikanth.twine.home

import dev.sasikanth.twine.data.sync.ConversationSyncQueueItem

data class HomeUiState(
  val tweetUrl: String?,
  val errors: List<InputError>,
  val syncQueue: List<ConversationSyncQueueItem>,
) {

  companion object {
    val DEFAULT = HomeUiState(
      tweetUrl = null,
      errors = emptyList(),
      syncQueue = emptyList()
    )
  }

  fun onTweetUrlChanged(tweetUrl: String?): HomeUiState {
    return copy(tweetUrl = tweetUrl, errors = emptyList())
  }

  fun invalidUrl(): HomeUiState {
    return copy(errors = listOf(InvalidUrl))
  }

  fun onSyncQueueLoaded(syncQueue: List<ConversationSyncQueueItem>): HomeUiState {
    return copy(syncQueue = syncQueue)
  }
}
