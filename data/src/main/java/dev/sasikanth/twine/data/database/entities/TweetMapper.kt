package dev.sasikanth.twine.data.database.entities

import dev.sasikanth.twine.data.api.models.TweetPayload
import java.time.Instant

fun Tweet.Companion.from(
  payload: TweetPayload,
  deviceCreatedAt: Instant
) = Tweet(
  id = payload.id,
  authorId = payload.authorId,
  conversationId = payload.conversationId,
  inReplyToUserId = payload.inReplyToUserId,
  text = payload.text,
  createdAt = payload.createdAt,
  deviceCreatedAt = deviceCreatedAt
)
