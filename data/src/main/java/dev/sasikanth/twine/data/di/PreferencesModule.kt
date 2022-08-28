package dev.sasikanth.twine.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.data.prefrences.AppPreferences
import dev.sasikanth.twine.data.prefrences.DataStoreAppPreferences

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

  @Binds
  abstract fun bindsAppPreferences(preferences: DataStoreAppPreferences): AppPreferences
}
