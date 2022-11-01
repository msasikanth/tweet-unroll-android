package dev.sasikanth.twine.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.data.api.TwitterRemoteSource
import dev.sasikanth.twine.data.api.TwitterRemoteSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteSourceModule {

  @Binds
  @Singleton
  abstract fun bindsTwitterRemoteSource(
    twitterRemoteSourceImpl: TwitterRemoteSourceImpl
  ): TwitterRemoteSource
}
