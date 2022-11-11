package dev.sasikanth.twine.common.testing.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.sasikanth.twine.common.di.modules.ClockModule
import dev.sasikanth.twine.common.testing.util.TestUserClock
import dev.sasikanth.twine.common.utils.UserClock
import javax.inject.Singleton

@Module
@TestInstallIn(
  replaces = [ClockModule::class],
  components = [SingletonComponent::class]
)
object TestClockModule {

  @Provides
  @Singleton
  fun providesTestUserClock(): TestUserClock {
    return TestUserClock()
  }

  @Provides
  fun providesUserClock(
    testUserClock: TestUserClock
  ): UserClock = testUserClock
}
