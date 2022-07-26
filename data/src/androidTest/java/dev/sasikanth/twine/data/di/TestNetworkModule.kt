package dev.sasikanth.twine.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.data.BuildConfig
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestNetworkModule {

  @Provides
  @Singleton
  fun providesTestOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor { chain ->
        val request = chain.request()
          .newBuilder()
          .addHeader(
            "Authorization",
            "Bearer ${BuildConfig.TEST_TWINE_BEARER_TOKEN}"
          )
          .build()

        chain.proceed(request)
      }
      .build()
  }
}
