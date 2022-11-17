package dev.sasikanth.twine.home.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.home.usecase.PagedRecentConversationsUseCase
import dev.sasikanth.twine.home.usecase.PagedSourceUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

  @Binds
  abstract fun bindsPagedRecentConversationsUseCase(
    pagedRecentConversationsUseCase: PagedRecentConversationsUseCase
  ): PagedSourceUseCase<RecentConversation>
}
