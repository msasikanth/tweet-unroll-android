package dev.sasikanth.twine.common.testing.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.common.testing.util.TestUserClock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestClockModule {

  @Provides
  @Singleton
  fun providesTestUserClock(): TestUserClock {
    return TestUserClock()
  }
}
