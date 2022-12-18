package dev.sasikanth.twine.data.database.entities.mappers

import dev.sasikanth.twine.data.api.models.TweetEntityPayload
import dev.sasikanth.twine.data.database.entities.TweetEntity

fun TweetEntity.Companion.from(
  tweetId: String,
  payload: TweetEntityPayload
) = TweetEntity(
  tweetId = tweetId,
  start = payload.start,
  end = payload.end,
  displayUrl = payload.displayUrl,
  expandedUrl = payload.expandedUrl
)
