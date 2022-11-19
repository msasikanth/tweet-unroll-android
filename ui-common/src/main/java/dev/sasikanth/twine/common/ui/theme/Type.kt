@file:OptIn(ExperimentalTextApi::class)

package dev.sasikanth.twine.common.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import dev.sasikanth.twine.common.ui.R

private val AlbertSans = FontFamily(
  Font(R.font.albert_sans_medium, FontWeight.Medium),
  Font(R.font.albert_sans_semi_bold, FontWeight.SemiBold),
  Font(R.font.albert_sans_bold, FontWeight.Bold)
)
private val figmaLineHeightStyle = LineHeightStyle(
  alignment = LineHeightStyle.Alignment.Center,
  trim = LineHeightStyle.Trim.None
)

private val platformTextStyle = PlatformTextStyle(
  includeFontPadding = false
)

internal val TwineTypography = Typography(
  labelLarge = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W600,
    letterSpacing = 0.sp,
    lineHeight = 20.sp,
    fontSize = 14.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  labelMedium = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W600,
    letterSpacing = 0.10000000149011612.sp,
    lineHeight = 16.sp,
    fontSize = 12.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  labelSmall = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W600,
    letterSpacing = 0.10000000149011612.sp,
    lineHeight = 16.sp,
    fontSize = 11.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  bodyLarge = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W500,
    letterSpacing = 0.sp,
    lineHeight = 24.sp,
    fontSize = 16.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  bodyMedium = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W500,
    letterSpacing = 0.sp,
    lineHeight = 20.sp,
    fontSize = 14.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  bodySmall = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W500,
    letterSpacing = 0.10000000149011612.sp,
    lineHeight = 16.sp,
    fontSize = 12.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  headlineLarge = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W700,
    letterSpacing = 0.sp,
    lineHeight = 40.sp,
    fontSize = 32.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  headlineMedium = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W700,
    letterSpacing = 0.sp,
    lineHeight = 36.sp,
    fontSize = 28.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  headlineSmall = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W700,
    letterSpacing = 0.sp,
    lineHeight = 32.sp,
    fontSize = 24.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  displayLarge = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W700,
    letterSpacing = 0.sp,
    lineHeight = 64.sp,
    fontSize = 57.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  displayMedium = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W700,
    letterSpacing = 0.sp,
    lineHeight = 52.sp,
    fontSize = 45.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  displaySmall = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W700,
    letterSpacing = 0.sp,
    lineHeight = 44.sp,
    fontSize = 36.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  titleLarge = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W600,
    letterSpacing = 0.sp,
    lineHeight = 28.sp,
    fontSize = 22.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  titleMedium = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W600,
    letterSpacing = 0.sp,
    lineHeight = 24.sp,
    fontSize = 16.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  ),
  titleSmall = TextStyle(
    fontFamily = AlbertSans,
    fontWeight = FontWeight.W600,
    letterSpacing = 0.sp,
    lineHeight = 20.sp,
    fontSize = 14.sp,
    lineHeightStyle = figmaLineHeightStyle,
    platformStyle = platformTextStyle
  )
)
