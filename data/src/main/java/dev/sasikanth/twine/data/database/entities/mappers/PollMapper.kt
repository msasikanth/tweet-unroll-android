package dev.sasikanth.twine.data.database.entities.mappers

import dev.sasikanth.twine.data.api.models.PollOptionPayload
import dev.sasikanth.twine.data.database.entities.Poll

fun Poll.Companion.from(
  tweetId: String,
  conversationId: String,
  option: PollOptionPayload
) = Poll(
  position = option.position,
  label = option.label,
  votes = option.votes,
  tweetId = tweetId,
  conversationId = conversationId
)
