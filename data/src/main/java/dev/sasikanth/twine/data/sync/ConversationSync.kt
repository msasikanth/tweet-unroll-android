package dev.sasikanth.twine.data.sync

import dev.sasikanth.twine.common.dispatchers.CoroutineDispatchers
import dev.sasikanth.twine.common.utils.UserClock
import dev.sasikanth.twine.data.api.TwitterRemoteSource
import dev.sasikanth.twine.data.api.models.TweetLookupPayload
import dev.sasikanth.twine.data.api.models.TweetPayload
import dev.sasikanth.twine.data.api.models.UserPayload
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.User
import dev.sasikanth.twine.data.database.entities.from
import dev.sasikanth.twine.data.database.repository.TweetsRepository
import dev.sasikanth.twine.data.database.repository.UsersRepository
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class ConversationSync @Inject constructor(
  private val twitterRemoteSource: TwitterRemoteSource,
  private val tweetsRepository: TweetsRepository,
  private val usersRepository: UsersRepository,
  private val userClock: UserClock,
  private val dispatchers: CoroutineDispatchers,
) {

  companion object {
    private const val CONVERSATION_START_DELTA_CEILING = 7
  }

  suspend fun trySync(tweetId: String): Response {
    return withContext(dispatchers.io) {
      val response = try {
        val conversationHead = fetchConversationHead(tweetId)
        val conversationHeadTweet = conversationHead?.data

        when {
          conversationHeadTweet == null -> {
            Response.NoTweetFound
          }

          isConversationOlderThan7Days(conversationHeadTweet.createdAt) -> {
            Response.ConversationIsOlderThanSevenDays
          }

          else -> {
            val conversation = twitterRemoteSource.conversationsLookup(
              conversationId = conversationHeadTweet.conversationId,
              authorId = conversationHeadTweet.authorId
            )

            val conversationTweets = conversation?.data
            val users = conversation?.includes?.users

            sync(
              userPayloads = users,
              conversationHeadTweet = conversationHeadTweet,
              conversationTweets = conversationTweets
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

  // TODO: Once we add other sync resources, this function might have a lot of dependencies
  //  need to break it down then
  private suspend fun sync(
    userPayloads: List<UserPayload>?,
    conversationHeadTweet: TweetPayload,
    conversationTweets: List<TweetPayload>?
  ) {
    syncTweets(
      authorId = conversationHeadTweet.authorId,
      conversationHeadTweet = conversationHeadTweet,
      conversationTweets = conversationTweets
    )
    syncUsers(
      userPayloads = userPayloads
    )
  }

  private suspend fun syncTweets(
    authorId: String,
    conversationHeadTweet: TweetPayload,
    conversationTweets: List<TweetPayload>?,
  ) {
    val tweetDbModel = Tweet.from(conversationHeadTweet)
    val tweetsInConversation = conversationTweets
      ?.map(Tweet::from)
      ?.filterTweetsByAuthorOnly(authorId)
      .orEmpty()

    tweetsRepository.saveTweets(listOf(tweetDbModel) + tweetsInConversation)
  }

  private suspend fun syncUsers(userPayloads: List<UserPayload>?) {
    val users = userPayloads?.map(User::from).orEmpty()
    usersRepository.saveUsers(users)
  }

  private suspend fun fetchConversationHead(tweetId: String): TweetLookupPayload? {
    val tweetPayload = twitterRemoteSource.tweetLookup(tweetId)
    val conversationId = tweetPayload?.data?.conversationId

    val conversationHead = if (!conversationId.isNullOrBlank() && conversationId != tweetId) {
      twitterRemoteSource.tweetLookup(conversationId)
    } else {
      tweetPayload
    }

    return conversationHead
  }

  private fun isConversationOlderThan7Days(conversationStartedAt: LocalDateTime): Boolean {
    val now = LocalDateTime.now(userClock)
    val conversationStartDelta = ChronoUnit.DAYS.between(conversationStartedAt, now)

    return conversationStartDelta > CONVERSATION_START_DELTA_CEILING
  }

  /**
   * Ignore all the tweets that are not a direct reply to the author. Some of the
   * replies may contain author mention, but we don't want to include those. We only want
   * to include tweets from original thread
   */
  private fun List<Tweet>.filterTweetsByAuthorOnly(authorId: String): List<Tweet> =
    filter { it.inReplyToUserId == null || it.inReplyToUserId == authorId }
}

sealed interface Response {
  object Success : Response
  object NoTweetFound : Response
  object ConversationIsOlderThanSevenDays : Response
  data class Unknown(val throwable: Throwable) : Response
}
