package dev.sasikanth.twine.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import dev.sasikanth.twine.data.database.entities.ReferencedTweet

@Dao
interface ReferencedTweetDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun save(referencedTweets: List<ReferencedTweet>)
}
