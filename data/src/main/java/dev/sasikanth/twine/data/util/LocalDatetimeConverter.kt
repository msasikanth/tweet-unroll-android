package dev.sasikanth.twine.data.util

import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDatetimeConverter {

  @TypeConverter
  fun fromString(dateTime: String?): LocalDateTime? {
    return dateTime?.let { LocalDateTime.parse(it) }
  }

  @TypeConverter
  fun toString(dateTime: LocalDateTime?): String? {
    return dateTime?.toString()
  }
}
