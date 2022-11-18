package dev.sasikanth.twine.data.database.repository

import dev.sasikanth.twine.common.dispatchers.CoroutineDispatchers
import dev.sasikanth.twine.data.database.dao.UsersDao
import dev.sasikanth.twine.data.database.entities.User
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRepository @Inject constructor(
  private val usersDao: UsersDao,
  private val dispatchers: CoroutineDispatchers
) {

  suspend fun saveUsers(users: List<User>) {
    withContext(dispatchers.io) {
      usersDao.save(users)
    }
  }
}
