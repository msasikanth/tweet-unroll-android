package dev.sasikanth.twine.data.database.entities

import dev.sasikanth.twine.data.api.models.PollOptionPayload

fun Poll.Companion.from(
  tweetId: String,
  option: PollOptionPayload
) = Poll(
  position = option.position,
  label = option.label,
  votes = option.votes,
  tweetId = tweetId
)
