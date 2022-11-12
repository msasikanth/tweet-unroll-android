package dev.sasikanth.twine.data.database.repository

import androidx.paging.PagingSource
import dev.sasikanth.twine.common.dispatchers.CoroutineDispatchers
import dev.sasikanth.twine.data.database.dao.MediaDao
import dev.sasikanth.twine.data.database.dao.PollDao
import dev.sasikanth.twine.data.database.dao.RecentConversationsDao
import dev.sasikanth.twine.data.database.dao.ReferencedTweetDao
import dev.sasikanth.twine.data.database.dao.TweetEntityDao
import dev.sasikanth.twine.data.database.dao.TweetsDao
import dev.sasikanth.twine.data.database.entities.Media
import dev.sasikanth.twine.data.database.entities.Poll
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.entities.ReferencedTweet
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.TweetEntity
import dev.sasikanth.twine.data.database.entities.TweetWithContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TweetsRepository @Inject constructor(
  private val tweetsDao: TweetsDao,
  private val recentConversationDao: RecentConversationsDao,
  private val tweetEntityDao: TweetEntityDao,
  private val referencedTweetDao: ReferencedTweetDao,
  private val mediaDao: MediaDao,
  private val pollDao: PollDao,
  private val dispatchers: CoroutineDispatchers
) {

  fun recentConversations(): PagingSource<Int, RecentConversation> {
    return recentConversationDao.recentConversations()
  }

  suspend fun saveTweets(tweets: List<Tweet>) {
    withContext(dispatchers.io) {
      tweetsDao.save(tweets)
    }
  }

  fun tweetsInConversation(conversationId: String): Flow<List<TweetWithContent>> {
    return tweetsDao.tweetsInConversation(conversationId)
  }

  suspend fun saveTweetEntities(tweetEntities: List<TweetEntity>) {
    withContext(dispatchers.io) {
      tweetEntityDao.save(tweetEntities)
    }
  }

  suspend fun saveReferencedTweets(referencedTweets: List<ReferencedTweet>) {
    withContext(dispatchers.io) {
      referencedTweetDao.save(referencedTweets)
    }
  }

  suspend fun saveMedia(media: List<Media>) {
    withContext(dispatchers.io) {
      mediaDao.save(media)
    }
  }

  suspend fun savePolls(polls: List<Poll>) {
    withContext(dispatchers.io) {
      pollDao.save(polls)
    }
  }
}
