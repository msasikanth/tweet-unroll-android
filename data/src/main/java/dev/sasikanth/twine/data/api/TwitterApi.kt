package dev.sasikanth.twine.data.api

import dev.sasikanth.twine.data.api.models.ConversationsLookupPayload
import dev.sasikanth.twine.data.api.models.TweetLookupPayload
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TwitterApi {

  /**
   * Fetch tweet data
   *
   * [Tweet Lookup Docs](https://developer.twitter.com/en/docs/twitter-api/tweets/lookup/quick-start)
   *
   * @param id: ID of a tweet (You can find at the end of a tweet URL)
   * @param tweetFields: Tweet information we want to fetch
   * @param mediaFields: Media information we want from the tweet if present
   * @param pollFields: Poll information we want from the tweet if present
   * @param userFields: User information from the tweet
   * @param expansions: Expand the tweet information to include more data
   *
   */
  @GET("tweets/{id}")
  suspend fun tweetLookup(
    @Path("id") id: String,
    @Query("tweet.fields") tweetFields: String,
    @Query("media.fields") mediaFields: String,
    @Query("poll.fields") pollFields: String,
    @Query("user.fields") userFields: String,
    @Query("expansions") expansions: String
  ): TweetLookupPayload?

  /**
   * Fetch conversations of a tweet in the last **7 days**. We cannot fetch
   * anything older than that
   *
   * [Tweets Search Docs](https://developer.twitter.com/en/docs/twitter-api/tweets/search/quick-start/recent-search)
   *
   * @param query: Search query
   * @param tweetFields: Tweet information we want to fetch
   * @param mediaFields: Media information we want from the tweet if present
   * @param pollFields: Poll information we want from the tweet if present
   * @param userFields: User information from the tweet
   * @param expansions: Expand the tweet information to include more data
   * @param maxResults: Max number of items in the search results
   *
   */
  @GET("tweets/search/recent")
  suspend fun conversationsLookup(
    @Query("query") query: String,
    @Query("tweet.fields") tweetFields: String,
    @Query("media.fields") mediaFields: String,
    @Query("poll.fields") pollFields: String,
    @Query("user.fields") userFields: String,
    @Query("expansions") expansions: String,
    @Query("max_results") maxResults: Int
  ): ConversationsLookupPayload?
}
