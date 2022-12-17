package dev.sasikanth.twine.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.data.database.repository.TweetsRepository
import dev.sasikanth.twine.data.database.repository.TweetsRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Binds
  abstract fun bindsTweetsRepository(
    tweetsRepository: TweetsRepositoryImpl
  ): TweetsRepository
}
