package dev.sasikanth.twine.common.testing.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.sasikanth.twine.data.database.entities.Media
import dev.sasikanth.twine.data.database.entities.Poll
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.entities.ReferencedTweet
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.TweetEntity
import dev.sasikanth.twine.data.database.entities.TweetWithContent
import dev.sasikanth.twine.data.database.entities.User
import dev.sasikanth.twine.data.database.repository.TweetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTweetsRepository : TweetsRepository {

  private val tweets = mutableListOf<Tweet>()
  private val tweetEntities = mutableListOf<TweetEntity>()
  private val referencedTweets = mutableListOf<ReferencedTweet>()
  private val tweetsMedia = mutableListOf<Media>()
  private val tweetsPoll = mutableListOf<Poll>()
  private val users = mutableListOf<User>()

  override fun recentConversations(): PagingSource<Int, RecentConversation> {
    return object : PagingSource<Int, RecentConversation>() {
      override fun getRefreshKey(state: PagingState<Int, RecentConversation>): Int? {
        return null
      }

      override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecentConversation> {
        val recentConversations = tweets
          .filter { tweet ->
            referencedTweets.none { it.id in tweet.id }
          }
          .groupBy { it.conversationId }
          .map { (conversationId, tweets) ->
            val tweet = tweets.minBy { it.createdAt }
            val user = users.first { it.id == tweet.authorId }

            RecentConversation(
              conversationId = conversationId,
              conversationPreviewText = tweet.text,
              conversationStartedAt = tweet.createdAt,
              conversationCreatedAt = tweet.deviceCreatedAt,
              username = user.username,
              userFullName = user.name,
              userProfileImage = user.profileImage,
              numberOfTweetsInConversation = tweets.size
            )
          }
          .sortedByDescending { it.conversationCreatedAt }

        return LoadResult.Page(
          data = recentConversations,
          prevKey = null,
          nextKey = null
        )
      }
    }
  }

  override fun tweetsInConversation(conversationId: String): Flow<List<TweetWithContent>> {
    return flowOf(
      tweets
        .filter { it.conversationId == conversationId }
        .sortedBy { it.createdAt }
        .map { tweet ->
          TweetWithContent(
            tweet = tweet,
            entities = tweetEntities.filter { it.tweetId == tweet.id },
            referencedTweets = referencedTweets.filter { it.tweetId == tweet.id },
            media = tweetsMedia.filter { it.tweetId == tweet.id },
            polls = tweetsPoll.filter { it.tweetId == tweet.id }
          )
        }
    )
  }

  override suspend fun saveTweets(tweets: List<Tweet>) {
    this.tweets.addAll(tweets)
  }

  override suspend fun saveTweetEntities(tweetEntities: List<TweetEntity>) {
    this.tweetEntities.addAll(tweetEntities)
  }

  override suspend fun saveReferencedTweets(referencedTweets: List<ReferencedTweet>) {
    this.referencedTweets.addAll(referencedTweets)
  }

  override suspend fun saveMedia(media: List<Media>) {
    this.tweetsMedia.addAll(media)
  }

  override suspend fun savePolls(polls: List<Poll>) {
    this.tweetsPoll.addAll(polls)
  }

  override suspend fun deleteConversation(conversationId: String) {
    val tweetIds = tweets.filter { it.conversationId == conversationId }.map { it.id }
    val referencedTweetIds = referencedTweets.filter { it.conversationId == conversationId }.map { it.id }
    val authorIds = tweets.filter { it.conversationId == conversationId }.map { it.authorId }
    val referencedTweetAuthorIds = tweets
      .filter { it.id in referencedTweetIds }
      .map { it.authorId }

    tweets.removeAll { it.conversationId == conversationId || it.id in referencedTweetIds }
    tweetEntities.removeAll { it.tweetId in tweetIds || it.tweetId in referencedTweetIds }
    referencedTweets.removeAll { it.tweetId in tweetIds || it.tweetId in referencedTweetIds }
    tweetsMedia.removeAll { it.tweetId in tweetIds || it.tweetId in referencedTweetIds }
    tweetsPoll.removeAll { it.tweetId in tweetIds || it.tweetId in referencedTweetIds }
    users.removeAll { it.id in authorIds || it.id in referencedTweetAuthorIds }
  }

  override suspend fun saveUsers(users: List<User>) {
    this.users.addAll(users)
  }

  fun clearAll() {
    tweets.clear()
    tweetEntities.clear()
    referencedTweets.clear()
    tweetsMedia.clear()
    tweetsPoll.clear()
    users.clear()
  }
}
