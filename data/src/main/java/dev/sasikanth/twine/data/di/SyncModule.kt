package dev.sasikanth.twine.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.data.sync.AndroidConversationSyncQueue
import dev.sasikanth.twine.data.sync.ConversationSyncQueue
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {

  @Binds
  @Singleton
  abstract fun bindsConversationSyncQueue(
    androidConversationSyncQueue: AndroidConversationSyncQueue
  ): ConversationSyncQueue
}
