package dev.sasikanth.twine.data.database.entities

import androidx.room.DatabaseView
import java.time.Instant

@DatabaseView(
  """
    SELECT
      T.conversation_id conversationId, T.text conversationPreviewText, 
      T.created_at conversationStartedAt, T.device_created_at conversationCreatedAt,
      
      U.name userFullName, U.username username,
      U.profile_image_url userProfileImage,
      
      ( SELECT COUNT(*) FROM Tweet WHERE conversation_id = T.conversation_id ) numberOfTweetsInConversation
    FROM Tweet T
    INNER JOIN User U ON U.id = T.author_id
    GROUP BY conversation_id HAVING MIN(created_at)
  """
)
data class RecentConversation(
  val conversationId: String,
  val conversationPreviewText: String,
  val conversationStartedAt: Instant,
  val conversationCreatedAt: Instant,
  val username: String,
  val userFullName: String,
  val userProfileImage: String,
  val numberOfTweetsInConversation: Int
)
