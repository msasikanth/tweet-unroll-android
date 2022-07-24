package dev.sasikanth.twine.data.api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TweetLookupPayload(
  val data: TweetPayload?,
  val includes: IncludesPayload?
)
