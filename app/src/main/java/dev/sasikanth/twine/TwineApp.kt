package dev.sasikanth.twine

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import dev.sasikanth.twine.utils.CrashlyticsTree
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class TwineApp : Application(), Configuration.Provider {

  @Inject
  lateinit var workerFactory: HiltWorkerFactory

  @Inject
  lateinit var firebaseCrashlytics: Provider<FirebaseCrashlytics>

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }

    try {
      Timber.plant(CrashlyticsTree(firebaseCrashlytics.get()))
    } catch (e: IllegalStateException) {
      // Ignore exception
    }
  }

  override fun getWorkManagerConfiguration() = Configuration.Builder()
    .setWorkerFactory(workerFactory)
    .build()
}
