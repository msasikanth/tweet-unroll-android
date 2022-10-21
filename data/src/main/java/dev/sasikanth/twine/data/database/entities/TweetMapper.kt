package dev.sasikanth.twine.data.database.entities

import dev.sasikanth.twine.data.api.models.TweetPayload

fun Tweet.Companion.from(payload: TweetPayload) = Tweet(
  id = payload.id,
  authorId = payload.authorId,
  conversationId = payload.conversationId,
  inReplyToUserId = payload.inReplyToUserId,
  text = payload.text,
  createdAt = payload.createdAt
)
