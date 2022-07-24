package dev.sasikanth.twine.data.api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConversationsLookupPayload(
  val data: List<TweetPayload>?,
  val includes: IncludesPayload?,
  val meta: MetaPayload?
)
