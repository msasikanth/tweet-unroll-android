package dev.sasikanth.twine.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AttachmentsPayload(
  @Json(name = "media_keys")
  val mediaKeys: List<String>?,
  @Json(name = "poll_ids")
  val pollIds: List<String>?
)
