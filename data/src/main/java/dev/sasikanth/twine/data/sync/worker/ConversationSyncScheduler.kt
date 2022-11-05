package dev.sasikanth.twine.data.sync.worker

import androidx.work.WorkManager
import javax.inject.Inject

class ConversationSyncScheduler @Inject constructor(
  private val workManager: WorkManager
) {

  fun schedule(tweetId: String) {
    val request = ConversationSyncWorker.createWorkRequest(tweetId)

    workManager.enqueue(request)
  }
}
