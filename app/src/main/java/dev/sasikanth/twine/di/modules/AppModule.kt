package dev.sasikanth.twine.di.modules

import android.content.Context
import androidx.core.os.ConfigurationCompat
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.BuildConfig
import java.util.Locale
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Named("auth_client_id")
  fun providesTwineClientId(): String = BuildConfig.CLIENT_ID

  @Provides
  @Named("auth_client_secret")
  fun providesTwineClientSecret(): String = BuildConfig.CLIENT_SECRET

  @Provides
  fun providesWorkManager(
    @ApplicationContext context: Context
  ) = WorkManager.getInstance(context)

  @Provides
  fun providesAppLocale(
    @ApplicationContext context: Context
  ): Locale {
    return ConfigurationCompat.getLocales(context.resources.configuration)
      .get(0) ?: Locale.getDefault()
  }

  @Provides
  fun providesFirebaseCrashlytics(): FirebaseCrashlytics {
    return FirebaseCrashlytics.getInstance()
  }

  @Provides
  fun providesFirebaseAnalytics(
    @ApplicationContext context: Context
  ): FirebaseAnalytics {
    return FirebaseAnalytics.getInstance(context)
  }
}
