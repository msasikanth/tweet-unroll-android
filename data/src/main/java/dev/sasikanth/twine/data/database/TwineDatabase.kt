package dev.sasikanth.twine.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sasikanth.twine.data.database.dao.TweetsDao
import dev.sasikanth.twine.data.database.dao.UsersDao
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.User
import dev.sasikanth.twine.data.util.LocalDatetimeConverter

@Database(
  version = 1,
  entities = [
    Tweet::class,
    User::class
  ]
)
@TypeConverters(
  LocalDatetimeConverter::class
)
abstract class TwineDatabase : RoomDatabase() {

  abstract fun tweetsDao(): TweetsDao

  abstract fun usersDao(): UsersDao
}
