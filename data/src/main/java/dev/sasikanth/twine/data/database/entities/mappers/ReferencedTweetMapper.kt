package dev.sasikanth.twine.data.database.entities.mappers

import dev.sasikanth.twine.data.api.models.ReferenceTypePayload
import dev.sasikanth.twine.data.api.models.ReferenceTypePayload.Quoted
import dev.sasikanth.twine.data.api.models.ReferenceTypePayload.RepliedTo
import dev.sasikanth.twine.data.api.models.ReferencedTweetPayload
import dev.sasikanth.twine.data.database.entities.ReferenceType
import dev.sasikanth.twine.data.database.entities.ReferencedTweet

fun ReferencedTweet.Companion.from(
  tweetId: String,
  payload: ReferencedTweetPayload,
) = ReferencedTweet(
  id = payload.id,
  type = ReferenceType.from(payload.type),
  tweetId = tweetId
)

fun ReferenceType.Companion.from(
  referenceTypePayload: ReferenceTypePayload
) = when (referenceTypePayload) {
  RepliedTo -> ReferenceType.RepliedTo
  Quoted -> ReferenceType.Quoted
}
