package dev.sasikanth.twine.data.sync

import androidx.work.WorkManager
import javax.inject.Inject

class ConversationSyncScheduler @Inject constructor(
  private val workManager: WorkManager
) {

  /**
   * Schedule a background worker to sync conversation based on the tweet id
   *
   * @param tweetId: ID of the tweet
   * @param tweetBy: Username of the author of the tweet
   */
  fun schedule(
    tweetId: String,
    tweetBy: String
  ) {
    val request = ConversationSyncWorker.createWorkRequest(tweetId, tweetBy)

    workManager.enqueue(request)
  }
}
