package dev.sasikanth.twine.common.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeMoshiAdapter {

  companion object {
    private val dateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
  }

  @FromJson
  fun toLocalDateTime(value: String?): LocalDateTime? {
    return value?.let {
      return dateTimeFormatter.parse(value, LocalDateTime::from)
    }
  }

  @ToJson
  fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
    return dateTime?.format(dateTimeFormatter)
  }
}
