package dev.sasikanth.twine.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PublicMetrics(
  @Json(name = "retweet_count")
  val retweetCount: Int,
  @Json(name = "reply_count")
  val replyCount: Int,
  @Json(name = "like_count")
  val likeCount: Int,
  @Json(name = "quote_count")
  val quoteCount: Int
)
