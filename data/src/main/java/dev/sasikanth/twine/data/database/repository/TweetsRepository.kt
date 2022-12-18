package dev.sasikanth.twine.data.database.repository

import androidx.annotation.VisibleForTesting
import androidx.paging.PagingSource
import dev.sasikanth.twine.common.dispatchers.CoroutineDispatchers
import dev.sasikanth.twine.data.database.TwineDatabase
import dev.sasikanth.twine.data.database.dao.MediaDao
import dev.sasikanth.twine.data.database.dao.PollDao
import dev.sasikanth.twine.data.database.dao.RecentConversationsDao
import dev.sasikanth.twine.data.database.dao.ReferencedTweetDao
import dev.sasikanth.twine.data.database.dao.TweetEntityDao
import dev.sasikanth.twine.data.database.dao.TweetsDao
import dev.sasikanth.twine.data.database.dao.UsersDao
import dev.sasikanth.twine.data.database.entities.Media
import dev.sasikanth.twine.data.database.entities.Poll
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.entities.ReferencedTweet
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.TweetEntity
import dev.sasikanth.twine.data.database.entities.TweetWithContent
import dev.sasikanth.twine.data.database.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface TweetsRepository {

  fun recentConversations(): PagingSource<Int, RecentConversation>

  fun tweetsInConversation(conversationId: String): Flow<List<TweetWithContent>>

  suspend fun saveTweets(tweets: List<Tweet>)

  suspend fun saveTweetEntities(tweetEntities: List<TweetEntity>)

  suspend fun saveReferencedTweets(referencedTweets: List<ReferencedTweet>)

  suspend fun saveMedia(media: List<Media>)

  suspend fun savePolls(polls: List<Poll>)

  suspend fun saveUsers(users: List<User>)

  suspend fun deleteConversation(conversationId: String)
}

class TweetsRepositoryImpl @Inject constructor(
  private val tweetsDao: TweetsDao,
  private val recentConversationDao: RecentConversationsDao,
  private val tweetEntityDao: TweetEntityDao,
  private val referencedTweetDao: ReferencedTweetDao,
  private val mediaDao: MediaDao,
  private val pollDao: PollDao,
  private val usersDao: UsersDao,
  private val twineDatabase: TwineDatabase,
  private val dispatchers: CoroutineDispatchers
) : TweetsRepository {

  override fun recentConversations(): PagingSource<Int, RecentConversation> {
    return recentConversationDao.recentConversations()
  }

  override suspend fun saveTweets(tweets: List<Tweet>) {
    withContext(dispatchers.io) {
      tweetsDao.save(tweets)
    }
  }

  override fun tweetsInConversation(conversationId: String): Flow<List<TweetWithContent>> {
    return tweetsDao.tweetsInConversation(conversationId)
  }

  override suspend fun saveTweetEntities(tweetEntities: List<TweetEntity>) {
    withContext(dispatchers.io) {
      tweetEntityDao.save(tweetEntities)
    }
  }

  override suspend fun saveReferencedTweets(referencedTweets: List<ReferencedTweet>) {
    withContext(dispatchers.io) {
      referencedTweetDao.save(referencedTweets)
    }
  }

  override suspend fun saveMedia(media: List<Media>) {
    withContext(dispatchers.io) {
      mediaDao.save(media)
    }
  }

  override suspend fun savePolls(polls: List<Poll>) {
    withContext(dispatchers.io) {
      pollDao.save(polls)
    }
  }

  override suspend fun saveUsers(users: List<User>) {
    withContext(dispatchers.io) {
      usersDao.save(users)
    }
  }

  override suspend fun deleteConversation(conversationId: String) {
    twineDatabase.runInTransaction {
      usersDao.deleteUsersInConversation(conversationId)
      tweetsDao.deleteReferencedTweetsInConversation(conversationId)

      // Deleting tweets at end since it has foreign key constraints
      tweetsDao.deleteTweetsInConversation(conversationId)
    }
  }

  @VisibleForTesting()
  suspend fun usersInConversation(conversationId: String): List<User> {
    return withContext(dispatchers.io) {
      usersDao.usersInConversation(conversationId)
    }
  }
}
