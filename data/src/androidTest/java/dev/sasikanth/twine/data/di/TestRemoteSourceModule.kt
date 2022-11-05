package dev.sasikanth.twine.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sasikanth.twine.data.api.FakeTwitterRemoteSource
import dev.sasikanth.twine.data.api.TwitterRemoteSource
import javax.inject.Singleton

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [RemoteSourceModule::class]
)
abstract class TestRemoteSourceModule {

  @Binds
  @Singleton
  abstract fun bindsFakeTwitterRemoteSource(
    fakeTwitterRemoteSource: FakeTwitterRemoteSource
  ): TwitterRemoteSource
}
