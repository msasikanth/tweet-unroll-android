package dev.sasikanth.twine.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserPayload(
  val id: String,
  val name: String,
  val username: String,
  @Json(name = "profile_image_url")
  val profileImage: String
)
