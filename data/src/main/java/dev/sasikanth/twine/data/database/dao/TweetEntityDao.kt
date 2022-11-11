package dev.sasikanth.twine.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import dev.sasikanth.twine.data.database.entities.TweetEntity

@Dao
interface TweetEntityDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun save(tweetEntities: List<TweetEntity>)
}
