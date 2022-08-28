package dev.sasikanth.twine.data.prefrences

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Singleton

private const val DATASTORE_NAME = "settings"
private val KEY_THEME = stringPreferencesKey("app_pref_theme")
private val KEY_DYNAMIC_COLORS = booleanPreferencesKey("app_dynamic_colors")

interface AppPreferences {
  val theme: Flow<Theme>
  val dynamicColors: Flow<Boolean>

  suspend fun setTheme(theme: Theme)
  suspend fun setDynamicColors(enabled: Boolean)
}

@Singleton
class DataStoreAppPreferences @Inject constructor(
  @ApplicationContext private val context: Context
) : AppPreferences {

  private val preferences = PreferenceDataStoreFactory
    .create { context.preferencesDataStoreFile(DATASTORE_NAME) }

  override val theme: Flow<Theme>
    get() = preferences
      .data
      .mapLatest { preferences ->
        val themeString = preferences[KEY_THEME]
        Theme.fromString(themeString)
      }

  override val dynamicColors: Flow<Boolean>
    get() = preferences
      .data
      .mapLatest { preferences ->
        preferences[KEY_DYNAMIC_COLORS] ?: true
      }

  override suspend fun setTheme(theme: Theme) {
    preferences.edit { preferences ->
      preferences[KEY_THEME] = theme.name
    }
  }

  override suspend fun setDynamicColors(enabled: Boolean) {
    preferences.edit { preferences ->
      preferences[KEY_DYNAMIC_COLORS] = enabled
    }
  }
}
