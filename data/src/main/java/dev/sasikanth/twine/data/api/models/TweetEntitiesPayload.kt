package dev.sasikanth.twine.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TweetEntityPayload(
  val start: Int,
  val end: Int,
  @Json(name = "display_url")
  val displayUrl: String,
  @Json(name = "expanded_url")
  val expandedUrl: String
)

@JsonClass(generateAdapter = true)
data class TweetEntitiesPayload(
  val urls: List<TweetEntityPayload>?
)
