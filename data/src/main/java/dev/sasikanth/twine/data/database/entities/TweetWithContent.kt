package dev.sasikanth.twine.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TweetWithContent(
  @Embedded
  val tweet: Tweet,
  @Relation(
    parentColumn = "id",
    entityColumn = "tweet_id"
  )
  val entities: List<TweetEntity>,
  @Relation(
    parentColumn = "id",
    entityColumn = "tweet_id"
  )
  val referencedTweets: List<ReferencedTweet>,
  @Relation(
    parentColumn = "id",
    entityColumn = "tweet_id"
  )
  val media: List<Media>,
  @Relation(
    parentColumn = "id",
    entityColumn = "tweet_id"
  )
  val polls: List<Poll>,
)
