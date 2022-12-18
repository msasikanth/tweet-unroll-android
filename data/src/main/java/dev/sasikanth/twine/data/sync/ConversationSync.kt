package dev.sasikanth.twine.data.sync

import dev.sasikanth.twine.common.dispatchers.CoroutineDispatchers
import dev.sasikanth.twine.common.utils.UserClock
import dev.sasikanth.twine.data.api.TwitterRemoteSource
import dev.sasikanth.twine.data.api.models.IncludesPayload
import dev.sasikanth.twine.data.api.models.MediaPayload
import dev.sasikanth.twine.data.api.models.PollPayload
import dev.sasikanth.twine.data.api.models.TweetLookupPayload
import dev.sasikanth.twine.data.api.models.TweetPayload
import dev.sasikanth.twine.data.api.models.UserPayload
import dev.sasikanth.twine.data.database.entities.Media
import dev.sasikanth.twine.data.database.entities.Poll
import dev.sasikanth.twine.data.database.entities.ReferencedTweet
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.TweetEntity
import dev.sasikanth.twine.data.database.entities.User
import dev.sasikanth.twine.data.database.entities.from
import dev.sasikanth.twine.data.database.entities.mappers.from
import dev.sasikanth.twine.data.database.repository.TweetsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.Instant
import javax.inject.Inject

class ConversationSync @Inject constructor(
  private val twitterRemoteSource: TwitterRemoteSource,
  private val tweetsRepository: TweetsRepository,
  private val dispatchers: CoroutineDispatchers,
  private val userClock: UserClock
) {

  suspend fun trySync(tweetId: String): Response {
    return withContext(dispatchers.io) {
      val response = try {
        val conversationHead = fetchConversationHead(tweetId)

        when (val conversationHeadTweet = conversationHead?.data) {
          null -> {
            Response.NoTweetFound
          }

          else -> {
            syncConversation(
              conversationHeadTweetPayload = conversationHeadTweet,
              includesPayload = conversationHead.includes
            )

            Response.Success
          }
        }
      } catch (e: Exception) {
        Timber.e("Failed to sync conversation", e)
        Response.Unknown(e)
      }

      return@withContext response
    }
  }

  private suspend fun syncConversation(
    conversationHeadTweetPayload: TweetPayload,
    includesPayload: IncludesPayload?
  ) {
    val conversationId = conversationHeadTweetPayload.conversationId
    val authorId = conversationHeadTweetPayload.authorId

    val conversation = twitterRemoteSource.conversationsLookup(
      conversationId = conversationId,
      authorId = authorId
    )
    val conversationTweets = conversation?.data

    val tweetsInConversation = conversationTweets.orEmpty() + conversationHeadTweetPayload

    val referencedTweets = referencedTweetsInConversation(
      conversationId = conversationId,
      tweetsInConversation = tweetsInConversation
    )
    // We are fetching referenced tweets contents as the Twitter API doesn't include
    // nested includes content (depth = 1).
    val (referencedTweetsInConversation, referencedTweetsIncludes) = fetchReferencedTweets(
      referencedTweets = referencedTweets
    )

    val tweets = tweetsInConversation + referencedTweetsInConversation
    val includes = listOfNotNull(includesPayload, conversation?.includes) + referencedTweetsIncludes

    val users = includes.flatMap { it.users.orEmpty() }
    val media = includes.flatMap { it.media.orEmpty() }
    val polls = includes.flatMap { it.polls.orEmpty() }

    syncTweets(tweets = tweets)
    syncMedia(conversationId = conversationId, tweets = tweets, mediaPayloads = media)
    syncTweetEntities(conversationId = conversationId, tweets = tweets)
    syncPolls(conversationId = conversationId, tweets = tweets, pollsPayloads = polls)
    syncReferencedTweets(referencedTweets = referencedTweets)
    syncUsers(conversationId = conversationId, users = users)
  }

  private fun referencedTweetsInConversation(
    conversationId: String,
    tweetsInConversation: List<TweetPayload>
  ) = tweetsInConversation
    .flatMap { payload ->
      payload
        .referencedTweets
        ?.filter { rt -> isReferencedTweetInConversation(tweetsInConversation, rt.id) }
        ?.map { referencedTweetPayload ->
          ReferencedTweet.from(
            tweetId = payload.id,
            conversationId = conversationId,
            payload = referencedTweetPayload
          )
        }
        .orEmpty()
    }

  private fun isReferencedTweetInConversation(
    tweetsInConversation: List<TweetPayload>,
    referencedTweetId: String
  ) = !tweetsInConversation.any { it.id == referencedTweetId }

  private suspend fun syncReferencedTweets(referencedTweets: List<ReferencedTweet>) {
    tweetsRepository.saveReferencedTweets(referencedTweets)
  }

  private suspend fun syncPolls(
    conversationId: String,
    tweets: List<TweetPayload>,
    pollsPayloads: List<PollPayload>
  ) {
    val polls = tweets.flatMap { payload ->
      payload
        .attachments
        ?.pollIds
        ?.flatMap { pollId ->
          pollsPayloads
            .filter { it.id == pollId }
            .flatMap { it.options }
            .map { pollOptionPayload ->
              Poll.from(
                tweetId = payload.id,
                conversationId = conversationId,
                option = pollOptionPayload
              )
            }
        }
        .orEmpty()
    }

    tweetsRepository.savePolls(polls)
  }

  private suspend fun syncTweetEntities(conversationId: String, tweets: List<TweetPayload>) {
    val tweetEntities = tweets.flatMap { payload ->
      payload
        .entities
        ?.urls
        ?.map { tweetEntityPayload ->
          TweetEntity.from(
            tweetId = payload.id,
            conversationId = conversationId,
            payload = tweetEntityPayload
          )
        }
        .orEmpty()
    }

    tweetsRepository.saveTweetEntities(tweetEntities)
  }

  private suspend fun syncMedia(
    conversationId: String,
    tweets: List<TweetPayload>,
    mediaPayloads: List<MediaPayload>
  ) {
    val media = tweets.flatMap { payload ->
      payload
        .attachments
        ?.mediaKeys
        ?.flatMap { mediaKey ->
          mediaPayloads
            .filter { it.mediaKey == mediaKey }
            .map { mediaPayload ->
              Media.from(
                tweetId = payload.id,
                conversationId = conversationId,
                payload = mediaPayload
              )
            }
        }
        .orEmpty()
    }

    tweetsRepository.saveMedia(media)
  }

  private suspend fun syncTweets(tweets: List<TweetPayload>) {
    val now = Instant.now(userClock)
    val tweetsInConversation = tweets
      .map { payload ->
        Tweet.from(
          payload = payload,
          deviceCreatedAt = now
        )
      }

    tweetsRepository.saveTweets(tweetsInConversation)
  }

  private suspend fun syncUsers(users: List<UserPayload>, conversationId: String) {
    val usersInConversation = users.map {
      User.from(
        conversationId = conversationId,
        payload = it
      )
    }
    tweetsRepository.saveUsers(usersInConversation)
  }

  private suspend fun fetchReferencedTweets(
    referencedTweets: List<ReferencedTweet>
  ): Pair<List<TweetPayload>, List<IncludesPayload>> {
    val tweetReferencedInConversation = coroutineScope {
      referencedTweets
        .map { referencedTweet ->
          async(dispatchers.io) { twitterRemoteSource.tweetLookup(referencedTweet.id) }
        }
        .awaitAll()
        .filterNotNull()
    }

    val referencedTweetsInConversation = tweetReferencedInConversation.mapNotNull { it.data }
    val referencedTweetsIncludes = tweetReferencedInConversation.mapNotNull { it.includes }

    return Pair(referencedTweetsInConversation, referencedTweetsIncludes)
  }

  private suspend fun fetchConversationHead(tweetId: String): TweetLookupPayload? {
    val tweetPayload = twitterRemoteSource.tweetLookup(tweetId)
    val conversationId = tweetPayload?.data?.conversationId

    val conversationHead = if (!conversationId.isNullOrBlank() && conversationId != tweetId) {
      twitterRemoteSource.tweetLookup(conversationId) ?: tweetPayload
    } else {
      tweetPayload
    }

    return conversationHead
  }
}

sealed interface Response {
  object Success : Response
  object NoTweetFound : Response
  data class Unknown(val throwable: Throwable) : Response
}
