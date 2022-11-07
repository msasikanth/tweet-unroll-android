package dev.sasikanth.twine.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.data.clipboard.AndroidClipboard
import dev.sasikanth.twine.data.clipboard.Clipboard

@Module
@InstallIn(SingletonComponent::class)
abstract class ClipboardModule {

  @Binds
  abstract fun bindsClipboard(clipboard: AndroidClipboard): Clipboard
}
