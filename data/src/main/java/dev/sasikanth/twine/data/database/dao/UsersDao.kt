package dev.sasikanth.twine.data.database.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.sasikanth.twine.data.database.entities.User

@Dao
interface UsersDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun save(users: List<User>)

  @VisibleForTesting
  @Query("SELECT * FROM User WHERE conversation_id = :conversationId")
  fun usersInConversation(conversationId: String): List<User>
}
