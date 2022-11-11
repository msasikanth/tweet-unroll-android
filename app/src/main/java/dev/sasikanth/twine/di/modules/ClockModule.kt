package dev.sasikanth.twine.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.common.utils.RealUserClock
import dev.sasikanth.twine.common.utils.UserClock
import java.time.ZoneId
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClockModule {

  @Provides
  @Singleton
  fun systemDefaultZone(): ZoneId = ZoneId.systemDefault()

  @Provides
  @Singleton
  fun providesUserClock(userTimeZone: ZoneId): UserClock = RealUserClock(userTimeZone)
}
