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
import dev.sasikanth.twine.data.database.dao.UsersDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  // TODO: Remove destructive migration once data layer is finished
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
      .fallbackToDestructiveMigration()
      .build()
  }

  @Provides
  fun providesTweetsDao(database: TwineDatabase): TweetsDao {
    return database.tweetsDao()
  }

  @Provides
  fun providesUsersDao(database: TwineDatabase): UsersDao {
    return database.usersDao()
  }
}
