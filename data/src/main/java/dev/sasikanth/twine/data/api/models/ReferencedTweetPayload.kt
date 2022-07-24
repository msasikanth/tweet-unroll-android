package dev.sasikanth.twine.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class ReferenceTypePayload {
  @Json(name = "replied_to")
  RepliedTo,

  @Json(name = "quoted")
  Quoted
}

@JsonClass(generateAdapter = true)
data class ReferencedTweetPayload(
  val id: String,
  val type: ReferenceTypePayload
)
