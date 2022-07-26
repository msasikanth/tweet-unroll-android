package dev.sasikanth.twine.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.auth.TwineAuthInterceptor
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  @Singleton
  fun providesOkHttpClient(
    twineAuthInterceptor: TwineAuthInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(twineAuthInterceptor)
      .build()
  }
}
