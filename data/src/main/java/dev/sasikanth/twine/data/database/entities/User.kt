package dev.sasikanth.twine.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
  primaryKeys = [
    "id",
    "conversation_id"
  ]
)
data class User(
  val id: String,
  val name: String,
  val username: String,
  @ColumnInfo(name = "profile_image_url")
  val profileImage: String,
  @ColumnInfo(name = "conversation_id")
  val conversationId: String
) {
  companion object
}
