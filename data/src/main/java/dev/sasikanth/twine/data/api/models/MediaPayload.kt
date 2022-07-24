package dev.sasikanth.twine.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class MediaTypePayload {

  @Json(name = "photo")
  Photo,

  @Json(name = "video")
  Video,

  @Json(name = "animated_gif")
  AnimatedGif
}

@JsonClass(generateAdapter = true)
data class MediaPayload(
  @Json(name = "media_key")
  val mediaKey: String,
  val type: MediaTypePayload,
  val url: String?,
  @Json(name = "preview_image_url")
  val previewImage: String?
)
