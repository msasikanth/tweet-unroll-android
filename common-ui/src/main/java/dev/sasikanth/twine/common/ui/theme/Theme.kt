package dev.sasikanth.twine.common.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun TwineTheme(
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  useDynamicColors: Boolean = true,
  content: @Composable() () -> Unit
) {
  val colorScheme = when {
    useDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current

      if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    useDarkTheme -> darkColorScheme()
    else -> lightColorScheme()
  }

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDarkTheme
    }
  }

  CompositionLocalProvider(
    LocalTwineColorScheme provides colorScheme
  ) {
    MaterialTheme(
      colorScheme = TwineTheme.colorScheme.toMaterialColorScheme(),
      typography = TwineTypography,
      content = content
    )
  }
}

object TwineTheme {

  val typography: Typography
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography

  val colorScheme: TwineColorScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalTwineColorScheme.current

  val shapes: Shapes
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.shapes
}
