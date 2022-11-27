package dev.sasikanth.twine.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  @Singleton
  fun providesOkHttpClient(
    interceptors: Set<@JvmSuppressWildcards Interceptor>
  ): OkHttpClient {
    val builder = OkHttpClient.Builder()
    interceptors.forEach { interceptor ->
      builder.addInterceptor(interceptor)
    }

    return builder.build()
  }
}
