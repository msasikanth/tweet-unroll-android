package dev.sasikanth.twine.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sasikanth.twine.data.database.dao.MediaDao
import dev.sasikanth.twine.data.database.dao.PollDao
import dev.sasikanth.twine.data.database.dao.RecentConversationsDao
import dev.sasikanth.twine.data.database.dao.ReferencedTweetDao
import dev.sasikanth.twine.data.database.dao.TweetsDao
import dev.sasikanth.twine.data.database.dao.UsersDao
import dev.sasikanth.twine.data.database.entities.Media
import dev.sasikanth.twine.data.database.entities.Poll
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.entities.ReferencedTweet
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.database.entities.User
import dev.sasikanth.twine.data.util.InstantRoomConverter

@Database(
  version = 1,
  entities = [
    Tweet::class,
    User::class,
    Media::class,
    Poll::class,
    ReferencedTweet::class
  ],
  views = [
    RecentConversation::class
  ]
)
@TypeConverters(
  InstantRoomConverter::class
)
abstract class TwineDatabase : RoomDatabase() {

  abstract fun tweetsDao(): TweetsDao

  abstract fun usersDao(): UsersDao

  abstract fun recentConversationsDao(): RecentConversationsDao

  abstract fun mediaDao(): MediaDao

  abstract fun pollDao(): PollDao

  abstract fun referencedTweetDao(): ReferencedTweetDao
}
