package dev.sasikanth.twine.data.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.test.core.app.ApplicationProvider
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestDriver
import androidx.work.testing.WorkManagerTestInitHelper
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.sasikanth.twine.data.api.FakeTwitterRemoteSource
import dev.sasikanth.twine.data.api.TwitterRemoteSource
import dev.sasikanth.twine.data.api.models.TweetLookupPayload
import dev.sasikanth.twine.data.api.models.TweetPayload
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import javax.inject.Inject

@HiltAndroidTest
class ConversationSyncQueueIntegrationTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  private val context: Context = ApplicationProvider.getApplicationContext()

  private lateinit var conversationSyncQueue: ConversationSyncQueue

  @Inject
  lateinit var conversationSync: ConversationSync

  @Inject
  lateinit var twitterRemoteSource: TwitterRemoteSource

  @Inject
  lateinit var hiltWorkerFactory: HiltWorkerFactory

  private lateinit var workManager: WorkManager
  private var testDriver: TestDriver? = null

  private val fakeTwitterRemoteSource
    get() = twitterRemoteSource as FakeTwitterRemoteSource

  @Before
  fun setup() {
    hiltRule.inject()

    val configuration = Configuration.Builder()
      .setMinimumLoggingLevel(Log.DEBUG)
      .setWorkerFactory(hiltWorkerFactory)
      .setExecutor(SynchronousExecutor())
      .build()

    WorkManagerTestInitHelper.initializeTestWorkManager(context, configuration)

    workManager = WorkManager.getInstance(context)
    testDriver = WorkManagerTestInitHelper.getTestDriver(context)

    conversationSyncQueue = AndroidConversationSyncQueue(workManager)
  }

  @After
  fun teardown() {
    testDriver = null

    workManager.cancelAllWork()
    workManager.pruneWork()

    fakeTwitterRemoteSource.clearAll()
  }

  @Test
  fun adding_item_to_sync_queue_should_work_correctly() = runTest {
    // given
    val tweetId = "3088649298172857728"
    val queueItem = ConversationSyncQueueItem(
      tweetId = tweetId,
      tweetBy = "its_sasikanth",
      status = null
    )

    // when
    val id = conversationSyncQueue.add(queueItem)

    // then
    assertThat(id).isNotNull()
    conversationSyncQueue.queue().test {
      assertThat(awaitItem()).isEqualTo(
        listOf(
          ConversationSyncQueueItem(
            tweetId = tweetId,
            tweetBy = "its_sasikanth",
            status = Status.Enqueued
          )
        )
      )
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun adding_multiple_items_to_sync_queue_should_work_correctly() = runTest {
    // given
    val tweetId1 = "6490826145275248087"
    val tweetId2 = "8737058209038926995"

    val queueItem1 = ConversationSyncQueueItem(
      tweetId = tweetId1,
      tweetBy = "its_sasikanth",
      status = null
    )

    val queueItem2 = ConversationSyncQueueItem(
      tweetId = tweetId2,
      tweetBy = "its_sasikanth",
      status = null
    )

    // when
    val id1 = conversationSyncQueue.add(queueItem1)
    val id2 = conversationSyncQueue.add(queueItem2)

    // then
    assertThat(id1).isNotNull()
    assertThat(id2).isNotNull()

    conversationSyncQueue.queue().test {
      assertThat(awaitItem()).containsExactly(
        ConversationSyncQueueItem(
          tweetId = tweetId2,
          tweetBy = "its_sasikanth",
          status = Status.Enqueued
        ),
        ConversationSyncQueueItem(
          tweetId = tweetId1,
          tweetBy = "its_sasikanth",
          status = Status.Enqueued
        )
      )
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun removing_item_from_queue_should_work_correctly() = runTest {
    // given
    val queueItem = ConversationSyncQueueItem(
      tweetId = "2338820652804611367",
      tweetBy = "its_sasikanth",
      status = null
    )
    conversationSyncQueue.add(queueItem)

    // when
    conversationSyncQueue.remove(queueItem)

    // then
    conversationSyncQueue.queue().test {
      assertThat(awaitItem()).isEmpty()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun failed_sync_items_should_be_present_in_the_queue() = runTest {
    // given
    val failedTweet = "4485039768006780412"
    val enqueuedTweet = "4960689628190732585"

    val queueItem1 = ConversationSyncQueueItem(
      tweetId = failedTweet,
      tweetBy = "its_sasikanth",
      status = null
    )

    val queueItem2 = ConversationSyncQueueItem(
      tweetId = enqueuedTweet,
      tweetBy = "its_sasikanth",
      status = null
    )

    // when
    val id1 = conversationSyncQueue.add(queueItem1)
    testDriver?.setAllConstraintsMet(id1)

    val id2 = conversationSyncQueue.add(queueItem2)

    // then
    assertThat(id1).isNotNull()
    assertThat(id2).isNotNull()

    conversationSyncQueue.queue().test {
      assertThat(awaitItem()).containsExactly(
        ConversationSyncQueueItem(
          tweetId = failedTweet,
          tweetBy = "its_sasikanth",
          status = Status.Failure
        ),
        ConversationSyncQueueItem(
          tweetId = enqueuedTweet,
          tweetBy = "its_sasikanth",
          status = Status.Enqueued
        )
      )
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun successful_sync_items_should_not_be_present_in_the_queue() = runTest {
    // given
    val successfullySyncedTweet = "1373775694771373936"
    val enqueuedTweet = "6191369528317145282"

    val queueItem1 = ConversationSyncQueueItem(
      tweetId = successfullySyncedTweet,
      tweetBy = "its_sasikanth",
      status = null
    )

    val queueItem2 = ConversationSyncQueueItem(
      tweetId = enqueuedTweet,
      tweetBy = "its_sasikanth",
      status = null
    )

    fakeTwitterRemoteSource.addTweetLookupPayload(
      TweetLookupPayload(
        data = TweetPayload(
          id = successfullySyncedTweet,
          attachments = null,
          authorId = "280595048",
          conversationId = successfullySyncedTweet,
          createdAt = Instant.parse("2022-01-01T10:00:00Z"),
          entities = null,
          inReplyToUserId = null,
          referencedTweets = null,
          text = "Test tweet 1"
        ),
        includes = null
      )
    )

    // when
    val id1 = conversationSyncQueue.add(queueItem1)
    testDriver?.setAllConstraintsMet(id1)

    val id2 = conversationSyncQueue.add(queueItem2)

    // then
    assertThat(id1).isNotNull()
    assertThat(id2).isNotNull()

    conversationSyncQueue.queue().test {
      assertThat(awaitItem()).containsExactly(
        ConversationSyncQueueItem(
          tweetId = successfullySyncedTweet,
          tweetBy = "its_sasikanth",
          status = Status.InProgress
        ),
        ConversationSyncQueueItem(
          tweetId = enqueuedTweet,
          tweetBy = "its_sasikanth",
          status = Status.Enqueued
        )
      )
      assertThat(awaitItem()).containsExactly(
        ConversationSyncQueueItem(
          tweetId = enqueuedTweet,
          tweetBy = "its_sasikanth",
          status = Status.Enqueued
        )
      )
      cancelAndIgnoreRemainingEvents()
    }
  }
}
