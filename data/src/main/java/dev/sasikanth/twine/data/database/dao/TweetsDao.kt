package dev.sasikanth.twine.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.TweetWithContent
import kotlinx.coroutines.flow.Flow

@Dao
interface TweetsDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun save(tweets: List<Tweet>)

  @Query(
    """
      SELECT * FROM Tweet
      WHERE conversation_id = :conversationId
      ORDER BY created_at ASC
    """
  )
  @Transaction
  fun tweetsInConversation(conversationId: String): Flow<List<TweetWithContent>>
}
