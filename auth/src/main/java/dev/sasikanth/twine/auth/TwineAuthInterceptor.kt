package dev.sasikanth.twine.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
 * Class to intercept OkHttps [Request]'s and add `Authorization` header
 * if there is an access token
 *
 * @param authManager: [AuthManager] is used to fetch the fresh access token
 *
 */
class TwineAuthInterceptor @Inject constructor(
  private val authManager: TwineAuthManager
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    return chain.proceed(addAuthorizationHeader(request))
  }

  private fun addAuthorizationHeader(request: Request): Request {
    val accessToken = runBlocking { authManager.fetchFreshAccessToken() }

    return if (accessToken != null) {
      request
        .newBuilder()
        .header("Authorization", "Bearer $accessToken")
        .build()
    } else {
      request
    }
  }
}
