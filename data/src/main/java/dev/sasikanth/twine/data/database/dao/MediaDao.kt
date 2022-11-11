package dev.sasikanth.twine.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import dev.sasikanth.twine.data.database.entities.Media

@Dao
interface MediaDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun save(media: List<Media>)
}
