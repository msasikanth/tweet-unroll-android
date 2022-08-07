package dev.sasikanth.twine.auth.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.auth.AuthManager
import dev.sasikanth.twine.auth.TwineAuthManager

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthManagerModule {

  @Binds
  abstract fun bindsAuthManager(twineAuthManager: TwineAuthManager): AuthManager
}
