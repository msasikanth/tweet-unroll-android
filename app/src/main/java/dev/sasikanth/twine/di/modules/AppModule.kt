package dev.sasikanth.twine.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.BuildConfig
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Named("auth_client_id")
  fun providesTwineClientId(): String = BuildConfig.CLIENT_ID

  @Provides
  @Named("auth_client_secret")
  fun providesTwineClientSecret(): String = BuildConfig.CLIENT_SECRET
}
