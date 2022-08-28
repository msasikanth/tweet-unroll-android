package dev.sasikanth.twine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sasikanth.twine.data.prefrences.AppPreferences
import dev.sasikanth.twine.data.prefrences.Theme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val appPreferences: AppPreferences
) : ViewModel() {

  private val defaultUiState = MainUiState.DEFAULT
  val uiState: StateFlow<MainUiState> = combine(
    appPreferences.theme,
    appPreferences.dynamicColors
  ) { theme, useDynamicColors ->
    defaultUiState.copy(
      theme = theme,
      useDynamicColors = useDynamicColors
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = defaultUiState
  )

  fun setTheme(theme: Theme) {
    viewModelScope.launch {
      appPreferences.setTheme(theme)
    }
  }

  fun setDynamicColors(enabled: Boolean) {
    viewModelScope.launch {
      appPreferences.setDynamicColors(enabled)
    }
  }
}
