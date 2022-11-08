package dev.sasikanth.twine.common.ui.utils

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import dev.sasikanth.twine.common.ui.utils.KeyboardState.Closed
import dev.sasikanth.twine.common.ui.utils.KeyboardState.Opened

enum class KeyboardState {
  Opened, Closed
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun keyboardVisibilityAsState(): State<KeyboardState> {
  val lifecycle = LocalLifecycleOwner.current.lifecycle
  val isResumed = lifecycle.currentState == Lifecycle.State.RESUMED

  return rememberUpdatedState(if (WindowInsets.isImeVisible && isResumed) Opened else Closed)
}
