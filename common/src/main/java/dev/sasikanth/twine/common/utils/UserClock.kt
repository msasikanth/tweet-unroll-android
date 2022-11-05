package dev.sasikanth.twine.common.utils

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

// Copied from: https://github.com/simpledotorg/simple-android/blob/4f1ab825a601520822157623f9b5f1348bf2ee7e/app/src/main/java/org/simple/clinic/util/Clocks.kt
abstract class UserClock : Clock()

open class RealUserClock(userTimeZone: ZoneId) : UserClock() {

  private val userClock = Clock.system(userTimeZone)

  override fun withZone(zone: ZoneId): Clock = userClock.withZone(zone)

  override fun getZone(): ZoneId = userClock.zone

  override fun instant(): Instant = userClock.instant()
}
