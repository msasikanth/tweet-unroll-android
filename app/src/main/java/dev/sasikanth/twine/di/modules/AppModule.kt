package dev.sasikanth.twine.di.modules

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.BuildConfig
import dev.sasikanth.twine.common.utils.RealUserClock
import dev.sasikanth.twine.common.utils.UserClock
import java.time.ZoneId
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Named("auth_client_id")
  fun providesTwineClientId(): String = BuildConfig.CLIENT_ID

  @Provides
  @Named("auth_client_secret")
  fun providesTwineClientSecret(): String = BuildConfig.CLIENT_SECRET

  @Provides
  @Singleton
  fun systemDefaultZone(): ZoneId = ZoneId.systemDefault()

  @Provides
  @Singleton
  fun providesUserClock(userTimeZone: ZoneId): UserClock = RealUserClock(userTimeZone)

  @Provides
  fun providesWorkManager(
    @ApplicationContext context: Context
  ) = WorkManager.getInstance(context)
}
