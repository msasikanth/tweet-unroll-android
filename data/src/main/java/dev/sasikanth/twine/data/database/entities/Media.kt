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
data class Media(
  @PrimaryKey
  @ColumnInfo(name = "media_key")
  val mediaKey: String,
  val type: MediaType,
  val url: String?,
  @ColumnInfo(name = "preview_image_url")
  val previewImage: String?,
  @ColumnInfo(name = "tweet_id")
  val tweetId: String
) {
  companion object
}
