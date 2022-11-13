package dev.sasikanth.twine.data.sync

import dev.sasikanth.twine.common.dispatchers.CoroutineDispatchers
import dev.sasikanth.twine.common.utils.UserClock
import dev.sasikanth.twine.data.api.TwitterRemoteSource
import dev.sasikanth.twine.data.api.models.IncludesPayload
import dev.sasikanth.twine.data.api.models.TweetLookupPayload
import dev.sasikanth.twine.data.api.models.TweetPayload
import dev.sasikanth.twine.data.api.models.UserPayload
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.User
import dev.sasikanth.twine.data.database.entities.mappers.from
import dev.sasikanth.twine.data.database.repository.TweetsRepository
import dev.sasikanth.twine.data.database.repository.UsersRepository
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

class ConversationSync @Inject constructor(
  private val twitterRemoteSource: TwitterRemoteSource,
  private val tweetsRepository: TweetsRepository,
  private val usersRepository: UsersRepository,
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

    val tweets = (conversationTweets.orEmpty() + conversationHeadTweetPayload)
    val includes = listOfNotNull(includesPayload, conversation?.includes)

    val users = includes.flatMap { it.users.orEmpty() }

    syncTweets(tweets = tweets)
    syncUsers(users = users)
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

  private suspend fun syncUsers(users: List<UserPayload>) {
    val usersInConversation = users.map(User::from)
    usersRepository.saveUsers(usersInConversation)
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
