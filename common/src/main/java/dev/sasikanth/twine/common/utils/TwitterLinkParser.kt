package dev.sasikanth.twine.common.utils

import java.net.URI
import javax.inject.Inject

// Based on : https://github.com/saket/unfurl/blob/3d753e38b00ec6fe396154031a9fe40386cc80b8/unfurl-social/src/main/kotlin/me/saket/unfurl/social/TweetUnfurler.kt#L199
class TwitterLinkParser @Inject constructor() {

  companion object {
    private const val GROUP_TAG_USERNAME = "username"
    private const val GROUP_TAG_ID = "id"
  }

  private val regex = Regex(
    pattern = "^/(?<$GROUP_TAG_USERNAME>[\\w\\d]+)/status/(?<$GROUP_TAG_ID>[\\w\\d]+)\$"
  )

  fun getId(tweetLink: String): String? {
    val uri = tweetLink.toURI() ?: return null

    if (!uri.isTwitterLink()) {
      return null
    }

    val result = regex.find(uri.path) ?: return null
    return result.groups[GROUP_TAG_ID]?.value ?: return null
  }

  fun getUserName(tweetLink: String): String? {
    val uri = tweetLink.toURI() ?: return null

    if (!uri.isTwitterLink()) {
      return null
    }

    val result = regex.find(uri.path) ?: return null
    return result.groups[GROUP_TAG_USERNAME]?.value ?: return null
  }

  private fun URI.isTwitterLink(): Boolean = host?.contains("twitter.com") ?: false

  private fun String.toURI(): URI? = try {
    URI.create(this)
  } catch (e: IllegalArgumentException) {
    null
  }
}
