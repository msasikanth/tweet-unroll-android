package dev.sasikanth.twine.common.ui.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ln
import androidx.compose.material3.ColorScheme as MaterialColorScheme

@Immutable
class TwineColorScheme(
  brand: Color,
  onBrand: Color,
  primary: Color,
  onPrimary: Color,
  primaryContainer: Color,
  onPrimaryContainer: Color,
  inversePrimary: Color,
  secondary: Color,
  onSecondary: Color,
  secondaryContainer: Color,
  onSecondaryContainer: Color,
  tertiary: Color,
  onTertiary: Color,
  tertiaryContainer: Color,
  onTertiaryContainer: Color,
  background: Color,
  onBackground: Color,
  surface: Color,
  onSurface: Color,
  surfaceVariant: Color,
  onSurfaceVariant: Color,
  surfaceTint: Color,
  inverseSurface: Color,
  inverseOnSurface: Color,
  error: Color,
  onError: Color,
  errorContainer: Color,
  onErrorContainer: Color,
  outline: Color,
  outlineVariant: Color,
  deleteButton: Color,
  onDeleteButton: Color,
  scrim: Color,
) {
  var brand by mutableStateOf(brand, structuralEqualityPolicy())
    internal set
  var onBrand by mutableStateOf(onBrand, structuralEqualityPolicy())
    internal set
  var primary by mutableStateOf(primary, structuralEqualityPolicy())
    internal set
  var onPrimary by mutableStateOf(onPrimary, structuralEqualityPolicy())
    internal set
  var primaryContainer by mutableStateOf(primaryContainer, structuralEqualityPolicy())
    internal set
  var onPrimaryContainer by mutableStateOf(onPrimaryContainer, structuralEqualityPolicy())
    internal set
  var inversePrimary by mutableStateOf(inversePrimary, structuralEqualityPolicy())
    internal set
  var secondary by mutableStateOf(secondary, structuralEqualityPolicy())
    internal set
  var onSecondary by mutableStateOf(onSecondary, structuralEqualityPolicy())
    internal set
  var secondaryContainer by mutableStateOf(secondaryContainer, structuralEqualityPolicy())
    internal set
  var onSecondaryContainer by mutableStateOf(onSecondaryContainer, structuralEqualityPolicy())
    internal set
  var tertiary by mutableStateOf(tertiary, structuralEqualityPolicy())
    internal set
  var onTertiary by mutableStateOf(onTertiary, structuralEqualityPolicy())
    internal set
  var tertiaryContainer by mutableStateOf(tertiaryContainer, structuralEqualityPolicy())
    internal set
  var onTertiaryContainer by mutableStateOf(onTertiaryContainer, structuralEqualityPolicy())
    internal set
  var background by mutableStateOf(background, structuralEqualityPolicy())
    internal set
  var onBackground by mutableStateOf(onBackground, structuralEqualityPolicy())
    internal set
  var surface by mutableStateOf(surface, structuralEqualityPolicy())
    internal set
  var onSurface by mutableStateOf(onSurface, structuralEqualityPolicy())
    internal set
  var surfaceVariant by mutableStateOf(surfaceVariant, structuralEqualityPolicy())
    internal set
  var onSurfaceVariant by mutableStateOf(onSurfaceVariant, structuralEqualityPolicy())
    internal set
  var surfaceTint by mutableStateOf(surfaceTint, structuralEqualityPolicy())
    internal set
  var inverseSurface by mutableStateOf(inverseSurface, structuralEqualityPolicy())
    internal set
  var inverseOnSurface by mutableStateOf(inverseOnSurface, structuralEqualityPolicy())
    internal set
  var error by mutableStateOf(error, structuralEqualityPolicy())
    internal set
  var onError by mutableStateOf(onError, structuralEqualityPolicy())
    internal set
  var errorContainer by mutableStateOf(errorContainer, structuralEqualityPolicy())
    internal set
  var onErrorContainer by mutableStateOf(onErrorContainer, structuralEqualityPolicy())
    internal set
  var outline by mutableStateOf(outline, structuralEqualityPolicy())
    internal set
  var outlineVariant by mutableStateOf(outlineVariant, structuralEqualityPolicy())
    internal set
  var deleteButton by mutableStateOf(deleteButton, structuralEqualityPolicy())
    internal set
  var onDeleteButton by mutableStateOf(onDeleteButton, structuralEqualityPolicy())
    internal set
  var scrim by mutableStateOf(scrim, structuralEqualityPolicy())
    internal set

  fun copy(
    brand: Color = this.brand,
    onBrand: Color = this.onBrand,
    primary: Color = this.primary,
    onPrimary: Color = this.onPrimary,
    primaryContainer: Color = this.primaryContainer,
    onPrimaryContainer: Color = this.onPrimaryContainer,
    inversePrimary: Color = this.inversePrimary,
    secondary: Color = this.secondary,
    onSecondary: Color = this.onSecondary,
    secondaryContainer: Color = this.secondaryContainer,
    onSecondaryContainer: Color = this.onSecondaryContainer,
    tertiary: Color = this.tertiary,
    onTertiary: Color = this.onTertiary,
    tertiaryContainer: Color = this.tertiaryContainer,
    onTertiaryContainer: Color = this.onTertiaryContainer,
    background: Color = this.background,
    onBackground: Color = this.onBackground,
    surface: Color = this.surface,
    onSurface: Color = this.onSurface,
    surfaceVariant: Color = this.surfaceVariant,
    onSurfaceVariant: Color = this.onSurfaceVariant,
    surfaceTint: Color = this.surfaceTint,
    inverseSurface: Color = this.inverseSurface,
    inverseOnSurface: Color = this.inverseOnSurface,
    error: Color = this.error,
    onError: Color = this.onError,
    errorContainer: Color = this.errorContainer,
    onErrorContainer: Color = this.onErrorContainer,
    outline: Color = this.outline,
    outlineVariant: Color = this.outlineVariant,
    deleteButton: Color = this.deleteButton,
    onDeleteButton: Color = this.onDeleteButton,
    scrim: Color = this.scrim,
  ): TwineColorScheme =
    TwineColorScheme(
      brand = brand,
      onBrand = onBrand,
      primary = primary,
      onPrimary = onPrimary,
      primaryContainer = primaryContainer,
      onPrimaryContainer = onPrimaryContainer,
      inversePrimary = inversePrimary,
      secondary = secondary,
      onSecondary = onSecondary,
      secondaryContainer = secondaryContainer,
      onSecondaryContainer = onSecondaryContainer,
      tertiary = tertiary,
      onTertiary = onTertiary,
      tertiaryContainer = tertiaryContainer,
      onTertiaryContainer = onTertiaryContainer,
      background = background,
      onBackground = onBackground,
      surface = surface,
      onSurface = onSurface,
      surfaceVariant = surfaceVariant,
      onSurfaceVariant = onSurfaceVariant,
      surfaceTint = surfaceTint,
      inverseSurface = inverseSurface,
      inverseOnSurface = inverseOnSurface,
      error = error,
      onError = onError,
      errorContainer = errorContainer,
      onErrorContainer = onErrorContainer,
      outline = outline,
      outlineVariant = outlineVariant,
      deleteButton = deleteButton,
      onDeleteButton = onDeleteButton,
      scrim = scrim,
    )

  fun toMaterialColorScheme(): MaterialColorScheme =
    MaterialColorScheme(
      primary = primary,
      onPrimary = onPrimary,
      primaryContainer = primaryContainer,
      onPrimaryContainer = onPrimaryContainer,
      inversePrimary = inversePrimary,
      secondary = secondary,
      onSecondary = onSecondary,
      secondaryContainer = secondaryContainer,
      onSecondaryContainer = onSecondaryContainer,
      tertiary = tertiary,
      onTertiary = onTertiary,
      tertiaryContainer = tertiaryContainer,
      onTertiaryContainer = onTertiaryContainer,
      background = background,
      onBackground = onBackground,
      surface = surface,
      onSurface = onSurface,
      surfaceVariant = surfaceVariant,
      onSurfaceVariant = onSurfaceVariant,
      surfaceTint = surfaceTint,
      inverseSurface = inverseSurface,
      inverseOnSurface = inverseOnSurface,
      error = error,
      onError = onError,
      errorContainer = errorContainer,
      onErrorContainer = onErrorContainer,
      outline = outline,
      outlineVariant = outlineVariant,
      scrim = scrim,
    )
}

internal fun lightColorScheme(
  brand: Color = md_theme_primary_80,
  onBrand: Color = md_theme_primary_20,
  primary: Color = md_theme_light_primary,
  onPrimary: Color = md_theme_light_onPrimary,
  primaryContainer: Color = md_theme_light_primaryContainer,
  onPrimaryContainer: Color = md_theme_light_onPrimaryContainer,
  secondary: Color = md_theme_light_secondary,
  onSecondary: Color = md_theme_light_onSecondary,
  secondaryContainer: Color = md_theme_light_secondaryContainer,
  onSecondaryContainer: Color = md_theme_light_onSecondaryContainer,
  tertiary: Color = md_theme_light_tertiary,
  onTertiary: Color = md_theme_light_onTertiary,
  tertiaryContainer: Color = md_theme_light_tertiaryContainer,
  onTertiaryContainer: Color = md_theme_light_onTertiaryContainer,
  error: Color = md_theme_light_error,
  errorContainer: Color = md_theme_light_errorContainer,
  onError: Color = md_theme_light_onError,
  onErrorContainer: Color = md_theme_light_onErrorContainer,
  background: Color = md_theme_light_background,
  onBackground: Color = md_theme_light_onBackground,
  surface: Color = md_theme_light_surface,
  onSurface: Color = md_theme_light_onSurface,
  surfaceVariant: Color = md_theme_light_surfaceVariant,
  onSurfaceVariant: Color = md_theme_light_onSurfaceVariant,
  outline: Color = md_theme_light_outline,
  outlineVariant: Color = md_theme_light_outline_variant,
  inverseOnSurface: Color = md_theme_light_inverseOnSurface,
  inverseSurface: Color = md_theme_light_inverseSurface,
  inversePrimary: Color = md_theme_light_inversePrimary,
  surfaceTint: Color = md_theme_light_surfaceTint,
  deleteButton: Color = md_theme_error_20,
  onDeleteButton: Color = md_theme_error_20,
  scrim: Color = md_theme_light_scrim,
): TwineColorScheme = TwineColorScheme(
  brand = brand,
  onBrand = onBrand,
  primary = primary,
  onPrimary = onPrimary,
  primaryContainer = primaryContainer,
  onPrimaryContainer = onPrimaryContainer,
  inversePrimary = inversePrimary,
  secondary = secondary,
  onSecondary = onSecondary,
  secondaryContainer = secondaryContainer,
  onSecondaryContainer = onSecondaryContainer,
  tertiary = tertiary,
  onTertiary = onTertiary,
  tertiaryContainer = tertiaryContainer,
  onTertiaryContainer = onTertiaryContainer,
  background = background,
  onBackground = onBackground,
  surface = surface,
  onSurface = onSurface,
  surfaceVariant = surfaceVariant,
  onSurfaceVariant = onSurfaceVariant,
  surfaceTint = surfaceTint,
  inverseSurface = inverseSurface,
  inverseOnSurface = inverseOnSurface,
  error = error,
  onError = onError,
  errorContainer = errorContainer,
  onErrorContainer = onErrorContainer,
  outline = outline,
  outlineVariant = outlineVariant,
  deleteButton = deleteButton,
  onDeleteButton = onDeleteButton,
  scrim = scrim
)

internal fun darkColorScheme(
  brand: Color = md_theme_primary_80,
  onBrand: Color = md_theme_primary_20,
  primary: Color = md_theme_dark_primary,
  onPrimary: Color = md_theme_dark_onPrimary,
  primaryContainer: Color = md_theme_dark_primaryContainer,
  onPrimaryContainer: Color = md_theme_dark_onPrimaryContainer,
  secondary: Color = md_theme_dark_secondary,
  onSecondary: Color = md_theme_dark_onSecondary,
  secondaryContainer: Color = md_theme_dark_secondaryContainer,
  onSecondaryContainer: Color = md_theme_dark_onSecondaryContainer,
  tertiary: Color = md_theme_dark_tertiary,
  onTertiary: Color = md_theme_dark_onTertiary,
  tertiaryContainer: Color = md_theme_dark_tertiaryContainer,
  onTertiaryContainer: Color = md_theme_dark_onTertiaryContainer,
  error: Color = md_theme_dark_error,
  errorContainer: Color = md_theme_dark_errorContainer,
  onError: Color = md_theme_dark_onError,
  onErrorContainer: Color = md_theme_dark_onErrorContainer,
  background: Color = md_theme_dark_background,
  onBackground: Color = md_theme_dark_onBackground,
  surface: Color = md_theme_dark_surface,
  onSurface: Color = md_theme_dark_onSurface,
  surfaceVariant: Color = md_theme_dark_surfaceVariant,
  onSurfaceVariant: Color = md_theme_dark_onSurfaceVariant,
  outline: Color = md_theme_dark_outline,
  outlineVariant: Color = md_theme_dark_outline_variant,
  inverseOnSurface: Color = md_theme_dark_inverseOnSurface,
  inverseSurface: Color = md_theme_dark_inverseSurface,
  inversePrimary: Color = md_theme_dark_inversePrimary,
  surfaceTint: Color = md_theme_dark_surfaceTint,
  deleteButton: Color = md_theme_error_80,
  onDeleteButton: Color = md_theme_error_20,
  scrim: Color = md_theme_dark_scrim,
): TwineColorScheme = TwineColorScheme(
  brand = brand,
  onBrand = onBrand,
  primary = primary,
  onPrimary = onPrimary,
  primaryContainer = primaryContainer,
  onPrimaryContainer = onPrimaryContainer,
  inversePrimary = inversePrimary,
  secondary = secondary,
  onSecondary = onSecondary,
  secondaryContainer = secondaryContainer,
  onSecondaryContainer = onSecondaryContainer,
  tertiary = tertiary,
  onTertiary = onTertiary,
  tertiaryContainer = tertiaryContainer,
  onTertiaryContainer = onTertiaryContainer,
  background = background,
  onBackground = onBackground,
  surface = surface,
  onSurface = onSurface,
  surfaceVariant = surfaceVariant,
  onSurfaceVariant = onSurfaceVariant,
  surfaceTint = surfaceTint,
  inverseSurface = inverseSurface,
  inverseOnSurface = inverseOnSurface,
  error = error,
  onError = onError,
  errorContainer = errorContainer,
  onErrorContainer = onErrorContainer,
  outline = outline,
  outlineVariant = outlineVariant,
  deleteButton = deleteButton,
  onDeleteButton = onDeleteButton,
  scrim = scrim,
)

fun TwineColorScheme.contentColorFor(backgroundColor: Color): Color = when (backgroundColor) {
  primary -> onPrimary
  brand -> onBrand
  secondary -> onSecondary
  tertiary -> onTertiary
  background -> onBackground
  error -> onError
  surface -> onSurface
  surfaceVariant -> onSurfaceVariant
  primaryContainer -> onPrimaryContainer
  secondaryContainer -> onSecondaryContainer
  tertiaryContainer -> onTertiaryContainer
  errorContainer -> onErrorContainer
  inverseSurface -> inverseOnSurface
  else -> Color.Unspecified
}

/**
 * Computes the surface tonal color at different elevation levels e.g. surface1 through surface5.
 *
 * @param elevation Elevation value used to compute alpha of the color overlay layer.
 *
 * @return the [TwineColorScheme.surface] color with an alpha of the [TwineColorScheme.surfaceTint] color
 * overlaid on top of it.

 */
fun TwineColorScheme.surfaceColorAtElevation(
  elevation: Dp,
): Color {
  if (elevation == 0.dp) return surface
  val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
  return surfaceTint.copy(alpha = alpha).compositeOver(surface)
}

@Composable
@ReadOnlyComposable
fun contentColorFor(backgroundColor: Color) =
  TwineTheme.colorScheme.contentColorFor(backgroundColor).takeOrElse {
    LocalContentColor.current
  }

internal val LocalTwineColorScheme = staticCompositionLocalOf { lightColorScheme() }
