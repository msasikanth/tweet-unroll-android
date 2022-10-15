package dev.sasikanth.twine.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.data.database.TwineDatabase
import dev.sasikanth.twine.data.database.dao.TweetsDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Provides
  @Singleton
  fun providesAppDatabase(
    @ApplicationContext context: Context
  ): TwineDatabase {
    return Room
      .databaseBuilder(
        context,
        TwineDatabase::class.java,
        "twine-db"
      )
      .build()
  }

  @Provides
  fun providesTweetsDao(database: TwineDatabase): TweetsDao {
    return database.tweetsDao()
  }
}
