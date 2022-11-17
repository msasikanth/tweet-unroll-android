package dev.sasikanth.twine.common.utils

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import javax.inject.Inject

class TwineDateFormatter @Inject constructor(
  zoneId: ZoneId,
  locale: Locale
) {

  private val shortDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    .withZone(zoneId)
    .withLocale(locale)

  fun getRelativeTime(instant: Instant): String {
    val millis = (System.currentTimeMillis() - instant.toEpochMilli())
    val duration = Duration.ofMillis(millis)

    return when {
      duration.toHours() < 1 -> "${duration.toMinutes()}m"
      duration.toHours() < 24 -> "${duration.toHours()}h"
      duration.toDays() < 7 -> "${duration.toDays()}d"
      else -> shortDateFormatter.format(instant)
    }
  }
}
