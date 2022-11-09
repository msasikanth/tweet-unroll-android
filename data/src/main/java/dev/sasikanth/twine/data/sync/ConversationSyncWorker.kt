package dev.sasikanth.twine.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ConversationSyncWorker @AssistedInject constructor(
  @Assisted appContext: Context,
  @Assisted workerParams: WorkerParameters,
  private val conversationSync: ConversationSync
) : CoroutineWorker(appContext, workerParams) {

  companion object {
    private const val CONVERSATION_SYNC_WORKER_TAG = "conversation_tag:"
    private const val KEY_TWEET_ID = "tweet_id"
    private const val KEY_TWEET_BY = "tweet_by"

    fun createWorkRequest(
      tweetId: String,
      tweetBy: String
    ): OneTimeWorkRequest {
      val tag = "$CONVERSATION_SYNC_WORKER_TAG$tweetId"
      val input = workDataOf(
        KEY_TWEET_ID to tweetId,
        KEY_TWEET_BY to tweetBy
      )

      return OneTimeWorkRequest.Builder(ConversationSyncWorker::class.java)
        .setInputData(input)
        .addTag(tag)
        .build()
    }
  }

  override suspend fun doWork(): Result {
    val tweetId = inputData.getString(KEY_TWEET_ID) ?: return Result.failure()

    return when (conversationSync.trySync(tweetId = tweetId)) {
      Response.NoTweetFound,
      is Response.Unknown -> Result.failure(inputData)

      Response.Success -> Result.success(inputData)
    }
  }
}
