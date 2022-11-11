package dev.sasikanth.twine.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

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
data class Poll(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "poll_id")
  val pollId: Int = 0,
  val position: Int,
  val label: String,
  val votes: Int,
  @ColumnInfo(name = "tweet_id")
  val tweetId: String
)
