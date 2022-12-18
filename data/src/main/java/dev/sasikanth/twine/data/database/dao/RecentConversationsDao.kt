package dev.sasikanth.twine.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dev.sasikanth.twine.data.database.entities.RecentConversation

@Dao
interface RecentConversationsDao {

  @Query(
    """
      SELECT * FROM RecentConversation
      ORDER BY conversationCreatedAt DESC
    """
  )
  @Transaction
  fun recentConversations(): PagingSource<Int, RecentConversation>
}
