package dev.sasikanth.twine.common.dispatchers

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatchersModule {

  @Binds
  abstract fun bindsCoroutineDispatchers(
    coroutineDispatchers: RealCoroutineDispatchers
  ): CoroutineDispatchers
}
