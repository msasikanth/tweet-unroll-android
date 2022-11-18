package dev.sasikanth.twine.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Entity
@Fts4(contentEntity = Tweet::class)
data class TweetFts(
  val id: String,
  @ColumnInfo(name = "conversation_id")
  val conversationId: String,
  @ColumnInfo(name = "text")
  val tweet: String
)
