package dev.sasikanth.twine.common.ui.theme

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DoNotInline
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.dynamicDarkColorScheme as materialDynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme as materialDynamicLightColorScheme

@RequiresApi(Build.VERSION_CODES.S)
fun dynamicLightColorScheme(context: Context): TwineColorScheme {
  val materialDynamicLightColorScheme = materialDynamicLightColorScheme(context)
  return lightColorScheme(
    brand = ColorResourceHelper.getColor(context, android.R.color.system_accent1_200),
    onBrand = ColorResourceHelper.getColor(context, android.R.color.system_accent1_800),
    primary = materialDynamicLightColorScheme.primary,
    onPrimary = materialDynamicLightColorScheme.onPrimary,
    primaryContainer = materialDynamicLightColorScheme.primaryContainer,
    onPrimaryContainer = materialDynamicLightColorScheme.onPrimaryContainer,
    secondary = materialDynamicLightColorScheme.secondary,
    onSecondary = materialDynamicLightColorScheme.onSecondary,
    secondaryContainer = materialDynamicLightColorScheme.secondaryContainer,
    onSecondaryContainer = materialDynamicLightColorScheme.onSecondaryContainer,
    tertiary = materialDynamicLightColorScheme.tertiary,
    onTertiary = materialDynamicLightColorScheme.onTertiary,
    tertiaryContainer = materialDynamicLightColorScheme.tertiaryContainer,
    onTertiaryContainer = materialDynamicLightColorScheme.onTertiaryContainer,
    background = materialDynamicLightColorScheme.background,
    onBackground = materialDynamicLightColorScheme.onBackground,
    surface = materialDynamicLightColorScheme.surface,
    onSurface = materialDynamicLightColorScheme.onSurface,
    surfaceVariant = materialDynamicLightColorScheme.surfaceVariant,
    onSurfaceVariant = materialDynamicLightColorScheme.onSurfaceVariant,
    outline = materialDynamicLightColorScheme.outline,
    inverseOnSurface = materialDynamicLightColorScheme.inverseOnSurface,
    inverseSurface = materialDynamicLightColorScheme.inverseSurface,
    inversePrimary = materialDynamicLightColorScheme.inversePrimary,
    surfaceTint = materialDynamicLightColorScheme.surfaceTint,
  )
}

@RequiresApi(Build.VERSION_CODES.S)
fun dynamicDarkColorScheme(context: Context): TwineColorScheme {
  val materialDynamicDarkColorScheme = materialDynamicDarkColorScheme(context)
  return darkColorScheme(
    brand = ColorResourceHelper.getColor(context, android.R.color.system_accent1_200),
    onBrand = ColorResourceHelper.getColor(context, android.R.color.system_accent1_800),
    primary = materialDynamicDarkColorScheme.primary,
    onPrimary = materialDynamicDarkColorScheme.onPrimary,
    primaryContainer = materialDynamicDarkColorScheme.primaryContainer,
    onPrimaryContainer = materialDynamicDarkColorScheme.onPrimaryContainer,
    secondary = materialDynamicDarkColorScheme.secondary,
    onSecondary = materialDynamicDarkColorScheme.onSecondary,
    secondaryContainer = materialDynamicDarkColorScheme.secondaryContainer,
    onSecondaryContainer = materialDynamicDarkColorScheme.onSecondaryContainer,
    tertiary = materialDynamicDarkColorScheme.tertiary,
    onTertiary = materialDynamicDarkColorScheme.onTertiary,
    tertiaryContainer = materialDynamicDarkColorScheme.tertiaryContainer,
    onTertiaryContainer = materialDynamicDarkColorScheme.onTertiaryContainer,
    background = materialDynamicDarkColorScheme.background,
    onBackground = materialDynamicDarkColorScheme.onBackground,
    surface = materialDynamicDarkColorScheme.surface,
    onSurface = materialDynamicDarkColorScheme.onSurface,
    surfaceVariant = materialDynamicDarkColorScheme.surfaceVariant,
    onSurfaceVariant = materialDynamicDarkColorScheme.onSurfaceVariant,
    outline = materialDynamicDarkColorScheme.outline,
    inverseOnSurface = materialDynamicDarkColorScheme.inverseOnSurface,
    inverseSurface = materialDynamicDarkColorScheme.inverseSurface,
    inversePrimary = materialDynamicDarkColorScheme.inversePrimary,
    surfaceTint = materialDynamicDarkColorScheme.surfaceTint,
  )
}

private object ColorResourceHelper {
  @DoNotInline
  fun getColor(context: Context, @ColorRes id: Int): Color {
    return Color(context.resources.getColor(id, context.theme))
  }
}
