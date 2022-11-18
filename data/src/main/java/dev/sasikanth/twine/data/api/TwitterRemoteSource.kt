package dev.sasikanth.twine.data.api

import dev.sasikanth.twine.data.api.models.ConversationsLookupPayload
import dev.sasikanth.twine.data.api.models.TweetLookupPayload
import javax.inject.Inject

interface TwitterRemoteSource {

  suspend fun tweetLookup(id: String): TweetLookupPayload?

  suspend fun conversationsLookup(
    conversationId: String,
    authorId: String
  ): ConversationsLookupPayload?
}

/**
 * A wrapper class around [TwitterApi], so that it's easier to define
 * default fields, instead of defining them individually in every
 * API call in [TwitterApi]
 */
class TwitterRemoteSourceImpl @Inject constructor(
  private val twitterApi: TwitterApi
) : TwitterRemoteSource {

  private val defaultTweetFields = listOf(
    "created_at",
    "author_id",
    "public_metrics",
    "attachments",
    "conversation_id",
    "entities"
  ).joinToString(separator = ",")

  private val defaultMediaFields = listOf(
    "alt_text",
    "url",
    "preview_image_url",
    "media_key"
  ).joinToString(separator = ",")

  private val defaultPollFields = listOf(
    "id",
    "options"
  ).joinToString(separator = ",")

  private val defaultUserFields = listOf(
    "public_metrics",
    "profile_image_url",
    "url",
    "username",
    "verified",
    "description"
  ).joinToString(separator = ",")

  private val defaultExpansions = listOf(
    "attachments.media_keys",
    "author_id",
    "referenced_tweets.id",
    "referenced_tweets.id.author_id",
    "in_reply_to_user_id",
    "attachments.poll_ids"
  ).joinToString(separator = ",")

  override suspend fun tweetLookup(id: String): TweetLookupPayload? {
    return twitterApi.tweetLookup(
      id = id,
      tweetFields = defaultTweetFields,
      mediaFields = defaultMediaFields,
      pollFields = defaultPollFields,
      userFields = defaultUserFields,
      expansions = defaultExpansions
    )
  }

  override suspend fun conversationsLookup(
    conversationId: String,
    authorId: String
  ): ConversationsLookupPayload? {
    val query = "conversation_id:$conversationId from:$authorId to:$authorId"

    return twitterApi.conversationsLookup(
      query = query,
      tweetFields = defaultTweetFields,
      mediaFields = defaultMediaFields,
      pollFields = defaultPollFields,
      userFields = defaultUserFields,
      expansions = defaultExpansions,
      maxResults = 100
    )
  }
}
