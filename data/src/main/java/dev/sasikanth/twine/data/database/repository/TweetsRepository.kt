package dev.sasikanth.twine.data.database.repository

import androidx.paging.PagingSource
import dev.sasikanth.twine.data.database.dao.TweetsDao
import dev.sasikanth.twine.data.database.dao.UsersDao
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TweetsRepository @Inject constructor(
  private val tweetsDao: TweetsDao,
  private val usersDao: UsersDao
) {

  fun recentConversations(): PagingSource<Int, RecentConversation> {
    return tweetsDao.recentConversations()
  }

  suspend fun saveTweets(tweets: List<Tweet>) {
    tweetsDao.save(tweets)
  }

  suspend fun saveUsers(users: List<User>) {
    usersDao.save(users)
  }

  fun tweetsInConversation(conversationId: String): Flow<List<Tweet>> {
    return tweetsDao.tweetsInConversation(conversationId)
  }
}
