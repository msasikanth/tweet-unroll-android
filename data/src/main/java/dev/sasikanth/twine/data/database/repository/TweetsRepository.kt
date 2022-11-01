package dev.sasikanth.twine.data.database.repository

import androidx.paging.PagingSource
import dev.sasikanth.twine.common.dispatchers.CoroutineDispatchers
import dev.sasikanth.twine.data.database.dao.RecentConversationsDao
import dev.sasikanth.twine.data.database.dao.TweetsDao
import dev.sasikanth.twine.data.database.dao.UsersDao
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.entities.Tweet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TweetsRepository @Inject constructor(
  private val tweetsDao: TweetsDao,
  private val usersDao: UsersDao,
  private val recentConversationDao: RecentConversationsDao,
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

  suspend fun saveUsers(users: List<User>) {
    withContext(dispatchers.io) {
      usersDao.save(users)
    }
  }

  fun tweetsInConversation(conversationId: String): Flow<List<Tweet>> {
    return tweetsDao.tweetsInConversation(conversationId)
  }
}
