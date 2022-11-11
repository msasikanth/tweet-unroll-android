package dev.sasikanth.twine.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

enum class ReferenceType {
  RepliedTo,
  Quoted;

  companion object
}

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
  ]
)
data class ReferencedTweet(
  @PrimaryKey
  val id: String,
  val type: ReferenceType,
  @ColumnInfo(name = "tweet_id")
  val tweetId: String
) {
  companion object
}
