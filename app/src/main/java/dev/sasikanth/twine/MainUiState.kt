package dev.sasikanth.twine

import dev.sasikanth.twine.data.prefrences.Theme

data class MainUiState(
  val theme: Theme,
  val useDynamicColors: Boolean
) {

  companion object {

    val DEFAULT = MainUiState(
      theme = Theme.SYSTEM,
      useDynamicColors = true
    )
  }
}
