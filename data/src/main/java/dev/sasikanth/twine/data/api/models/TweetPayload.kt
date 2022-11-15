package dev.sasikanth.twine.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
data class TweetPayload(
  val attachments: AttachmentsPayload?,
  val id: String,
  @Json(name = "author_id")
  val authorId: String,
  @Json(name = "conversation_id")
  val conversationId: String,
  @Json(name = "created_at")
  val createdAt: Instant,
  @Json(name = "entities")
  val entities: TweetEntitiesPayload?,
  @Json(name = "in_reply_to_user_id")
  val inReplyToUserId: String?,
  @Json(name = "referenced_tweets")
  val referencedTweets: List<ReferencedTweetPayload>?,
  val text: String,
  @Json(name = "public_metrics")
  val publicMetrics: PublicMetrics
)
