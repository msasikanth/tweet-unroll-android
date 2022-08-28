package dev.sasikanth.twine.data.prefrences

enum class Theme {
  LIGHT,
  DARK,
  SYSTEM;

  companion object {

    fun fromString(theme: String?): Theme {
      return if (theme.isNullOrBlank()) {
        SYSTEM
      } else {
        valueOf(theme)
      }
    }
  }
}
