package dev.sasikanth.twine.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level

@Module
@InstallIn(SingletonComponent::class)
class DebugNetworkModule {

  @Provides
  @IntoSet
  fun providesOkHttpLoggingInterceptor(): Interceptor {
    return HttpLoggingInterceptor().apply {
      setLevel(Level.BASIC)
    }
  }
}
