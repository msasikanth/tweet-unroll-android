package dev.sasikanth.twine.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType.CONNECTED
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Duration

@HiltWorker
class ConversationSyncWorker @AssistedInject constructor(
  @Assisted appContext: Context,
  @Assisted workerParams: WorkerParameters,
  private val conversationSync: ConversationSync
) : CoroutineWorker(appContext, workerParams) {

  companion object {
    private const val MAX_RETRY_COUNT = 3
    private const val CONVERSATION_SYNC_WORKER_TAG = "conversation_tag:"
    private const val TAG_TWEET_ID = "tweet_id:"
    private const val TAG_TWEET_BY = "tweet_by:"

    fun tag(): String {
      return ConversationSyncWorker::class.java.simpleName
    }

    fun tag(tweetId: String): String {
      return "$CONVERSATION_SYNC_WORKER_TAG$tweetId"
    }

    fun tweetId(tags: Set<String>): String {
      return tags
        .first { it.startsWith(TAG_TWEET_ID) }
        .removePrefix(TAG_TWEET_ID)
    }

    fun tweetBy(tags: Set<String>): String {
      return tags
        .first { it.startsWith(TAG_TWEET_BY) }
        .removePrefix(TAG_TWEET_BY)
    }

    fun createWorkRequest(
      tweetId: String,
      tweetBy: String
    ): OneTimeWorkRequest {
      val constraints = Constraints.Builder()
        .setRequiredNetworkType(CONNECTED)
        .build()

      return OneTimeWorkRequest.Builder(ConversationSyncWorker::class.java)
        .addTag(tag())
        .addTag(tag(tweetId))
        .addTag("$TAG_TWEET_ID$tweetId")
        .addTag("$TAG_TWEET_BY$tweetBy")
        .setConstraints(constraints)
        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMinutes(2))
        .build()
    }
  }

  override suspend fun doWork(): Result {
    val tweetId = tweetId(tags)

    return when (conversationSync.trySync(tweetId = tweetId)) {
      Response.NoTweetFound -> Result.failure()
      is Response.Unknown -> retryOrFail()

      Response.Success -> Result.success()
    }
  }

  private fun retryOrFail(): Result {
    return if (runAttemptCount > MAX_RETRY_COUNT) {
      Result.failure()
    } else {
      Result.retry()
    }
  }
}
