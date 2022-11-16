package dev.sasikanth.twine.data.sync

import androidx.lifecycle.asFlow
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkInfo.State.BLOCKED
import androidx.work.WorkInfo.State.CANCELLED
import androidx.work.WorkInfo.State.ENQUEUED
import androidx.work.WorkInfo.State.FAILED
import androidx.work.WorkInfo.State.RUNNING
import androidx.work.WorkInfo.State.SUCCEEDED
import androidx.work.WorkManager
import dev.sasikanth.twine.data.sync.Status.Enqueued
import dev.sasikanth.twine.data.sync.Status.Failure
import dev.sasikanth.twine.data.sync.Status.InProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

interface ConversationSyncQueue {

  fun add(item: ConversationSyncQueueItem): UUID

  fun remove(item: ConversationSyncQueueItem)

  fun queue(): Flow<List<ConversationSyncQueueItem>>
}

class AndroidConversationSyncQueue @Inject constructor(
  private val workManager: WorkManager
) : ConversationSyncQueue {

  override fun add(item: ConversationSyncQueueItem): UUID {
    val tweetId = item.tweetId
    val tweetBy = item.tweetBy

    val workRequest = ConversationSyncWorker.createWorkRequest(
      tweetId = tweetId,
      tweetBy = tweetBy
    )

    val workName = "sync_$tweetId"
    workManager.enqueueUniqueWork(
      workName,
      ExistingWorkPolicy.REPLACE,
      workRequest
    )

    return workRequest.id
  }

  override fun remove(item: ConversationSyncQueueItem) {
    val tag = ConversationSyncWorker.tag(item.tweetId)
    workManager.cancelAllWorkByTag(tag)
  }

  override fun queue(): Flow<List<ConversationSyncQueueItem>> {
    val tag = ConversationSyncWorker.tag()

    return workManager
      .getWorkInfosByTagLiveData(tag)
      .asFlow()
      .map { workInfos ->
        workInfos
          .filter { workInfo ->
            workInfo.state != SUCCEEDED &&
                workInfo.state != CANCELLED &&
                workInfo.state != BLOCKED
          }
          .map(::mapToQueueItems)
      }
  }

  private fun mapToQueueItems(workInfo: WorkInfo): ConversationSyncQueueItem {
    val tags = workInfo.tags
    val tweetId = ConversationSyncWorker.tweetId(tags)
    val tweetBy = ConversationSyncWorker.tweetBy(tags)

    return ConversationSyncQueueItem(
      tweetId = tweetId,
      tweetBy = tweetBy,
      status = workInfo.state.toStatus()
    )
  }

  private fun WorkInfo.State.toStatus() = when (this) {
    ENQUEUED -> Enqueued

    RUNNING -> InProgress

    FAILED -> Failure

    SUCCEEDED,
    BLOCKED,
    CANCELLED -> throw IllegalStateException()
  }
}
