package dev.sasikanth.twine.utils

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import javax.inject.Inject

class CrashlyticsTree @Inject constructor(
  private val firebaseCrashlytics: FirebaseCrashlytics
) : Timber.Tree() {

  override fun isLoggable(tag: String?, priority: Int): Boolean {
    return priority >= Log.ERROR
  }

  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    firebaseCrashlytics.log(message)
    t?.let {
      firebaseCrashlytics.recordException(t)
    }
  }
}
