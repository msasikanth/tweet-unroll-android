package dev.sasikanth.twine.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import dev.sasikanth.twine.data.database.entities.RecentConversation

@Dao
interface RecentConversationsDao {

  @Query(
    """
      SELECT * FROM RecentConversation
      ORDER BY conversationStartedAt DESC
    """
  )
  fun recentConversations(): PagingSource<Int, RecentConversation>
}
