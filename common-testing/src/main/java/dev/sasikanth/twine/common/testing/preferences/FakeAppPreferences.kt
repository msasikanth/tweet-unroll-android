package dev.sasikanth.twine.common.testing.preferences

import dev.sasikanth.twine.data.prefrences.AppPreferences
import dev.sasikanth.twine.data.prefrences.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAppPreferences : AppPreferences {

  private val _theme = MutableStateFlow(Theme.SYSTEM)
  private val _dynamicColors = MutableStateFlow(true)

  override val theme: Flow<Theme>
    get() = _theme

  override val dynamicColors: Flow<Boolean>
    get() = _dynamicColors

  override suspend fun setTheme(theme: Theme) {
    _theme.value = theme
  }

  override suspend fun setDynamicColors(enabled: Boolean) {
    _dynamicColors.value = enabled
  }
}
