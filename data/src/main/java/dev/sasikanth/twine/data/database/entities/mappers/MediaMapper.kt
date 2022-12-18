package dev.sasikanth.twine.data.database.entities

import dev.sasikanth.twine.data.api.models.MediaPayload
import dev.sasikanth.twine.data.api.models.MediaTypePayload
import dev.sasikanth.twine.data.api.models.MediaTypePayload.AnimatedGif
import dev.sasikanth.twine.data.api.models.MediaTypePayload.Photo
import dev.sasikanth.twine.data.api.models.MediaTypePayload.Video

fun Media.Companion.from(
  tweetId: String,
  payload: MediaPayload
) = Media(
  mediaKey = payload.mediaKey,
  type = MediaType.from(payload.type),
  url = payload.url,
  previewImage = payload.previewImage,
  tweetId = tweetId
)

fun MediaType.Companion.from(
  mediaTypePayload: MediaTypePayload
) = when (mediaTypePayload) {
  Photo -> MediaType.Photo
  Video -> MediaType.Video
  AnimatedGif -> MediaType.AnimatedGif
}
