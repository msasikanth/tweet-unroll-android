package dev.sasikanth.twine.data.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant

class InstantMoshiAdapter {

  @FromJson
  fun toInstant(value: String?): Instant? {
    return value?.let(Instant::parse)
  }

  @ToJson
  fun fromInstant(instant: Instant?): String? {
    return instant?.toString()
  }
}
