package dev.sasikanth.twine.common.ui.utils

import androidx.compose.runtime.staticCompositionLocalOf
import dev.sasikanth.twine.common.utils.TwineDateFormatter

val LocalTwineDateFormatter = staticCompositionLocalOf<TwineDateFormatter> {
  error("TwineDateFormatter not provided")
}
