package dev.sasikanth.twine.data.api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IncludesPayload(
  val tweets: List<TweetPayload>?,
  val media: List<MediaPayload>?,
  val users: List<UserPayload>?,
  val polls: List<PollPayload>?
)
