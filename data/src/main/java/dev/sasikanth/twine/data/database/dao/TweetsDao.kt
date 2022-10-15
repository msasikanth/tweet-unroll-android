package dev.sasikanth.twine.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.entities.Tweet
import kotlinx.coroutines.flow.Flow

@Dao
interface TweetsDao {

  @Query(
    """
      SELECT * FROM RecentConversation
      ORDER BY conversationStartedAt DESC
    """
  )
  fun recentConversations(): PagingSource<Int, RecentConversation>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun save(tweets: List<Tweet>)
}
