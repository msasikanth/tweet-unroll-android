package dev.sasikanth.twine.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.auth.TwineAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DebugNetworkModule {

  @Provides
  @Singleton
  fun providesOkHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
      setLevel(Level.BASIC)
    }
  }

  @Provides
  @Singleton
  fun providesOkHttpClient(
    twineAuthInterceptor: TwineAuthInterceptor,
    okHttpLoggingInterceptor: HttpLoggingInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(twineAuthInterceptor)
      .addInterceptor(okHttpLoggingInterceptor)
      .build()
  }
}
