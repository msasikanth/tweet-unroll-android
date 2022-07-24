package dev.sasikanth.twine.data.api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PollOptionPayload(
  val position: Int,
  val label: String,
  val votes: Int
)

@JsonClass(generateAdapter = true)
data class PollPayload(
  val id: String,
  val options: List<PollOptionPayload>
)
