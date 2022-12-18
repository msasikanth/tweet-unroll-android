package dev.sasikanth.twine.common.testing.api

import dev.sasikanth.twine.data.api.TwitterRemoteSource
import dev.sasikanth.twine.data.api.models.ConversationsLookupPayload
import dev.sasikanth.twine.data.api.models.TweetLookupPayload
import javax.inject.Inject

class FakeTwitterRemoteSource @Inject constructor() : TwitterRemoteSource {

  private val tweetLookupPayloads = mutableListOf<TweetLookupPayload>()
  private val conversationLookupPayloads = mutableListOf<ConversationsLookupPayload>()

  override suspend fun tweetLookup(id: String): TweetLookupPayload? {
    return tweetLookupPayloads.firstOrNull { it.data?.id == id }
  }

  override suspend fun conversationsLookup(
    conversationId: String,
    authorId: String
  ): ConversationsLookupPayload? {
    return conversationLookupPayloads.firstOrNull { it.data?.firstOrNull()?.conversationId == conversationId }
  }

  fun addTweetLookupPayload(tweetLookupPayload: TweetLookupPayload) {
    tweetLookupPayloads.add(tweetLookupPayload)
  }

  fun addConversationLookupPayload(conversationsLookupPayload: ConversationsLookupPayload) {
    conversationLookupPayloads.add(conversationsLookupPayload)
  }

  fun clearAll() {
    tweetLookupPayloads.clear()
    conversationLookupPayloads.clear()
  }
}
