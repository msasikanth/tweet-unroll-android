package dev.sasikanth.twine.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import dev.sasikanth.twine.data.database.entities.Poll

@Dao
interface PollDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun save(polls: List<Poll>)
}
