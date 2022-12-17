package dev.sasikanth.twine.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index

@Entity(
  indices = [
    Index("tweet_id")
  ],
  foreignKeys = [
    ForeignKey(
      entity = Tweet::class,
      parentColumns = ["id"],
      childColumns = ["tweet_id"],
      onDelete = CASCADE
    )
  ],
  primaryKeys = [
    "position",
    "tweet_id",
    "conversation_id"
  ]
)
data class Poll(
  val position: Int,
  val label: String,
  val votes: Int,
  @ColumnInfo(name = "tweet_id")
  val tweetId: String,
  @ColumnInfo(name = "conversation_id")
  val conversationId: String
) {
  companion object
}
