package dev.sasikanth.twine.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetaPayload(
  @Json(name = "result_count")
  val resultCount: Int,
  @Json(name = "newest_id")
  val newestId: String?,
  @Json(name = "oldest_id")
  val oldestId: String?,
  @Json(name = "next_token")
  val nextToken: String?
)
