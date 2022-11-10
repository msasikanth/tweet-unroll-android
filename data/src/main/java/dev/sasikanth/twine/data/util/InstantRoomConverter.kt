package dev.sasikanth.twine.data.util

import androidx.room.TypeConverter
import java.time.Instant

class InstantRoomConverter {

  @TypeConverter
  fun fromString(value: String?): Instant? {
    return value?.let(Instant::parse)
  }

  @TypeConverter
  fun toString(instant: Instant?): String? {
    return instant?.toString()
  }
}
