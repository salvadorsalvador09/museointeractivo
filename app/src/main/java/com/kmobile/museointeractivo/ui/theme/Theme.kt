package com.kmobile.museointeractivo.ui.theme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Typography

val AppTypography = Typography(
    titleLarge = Typography().titleLarge.copy(fontFamily = CinzelFont),
    titleMedium = Typography().titleMedium.copy(fontFamily = CinzelFont),
)


private val LightColors = lightColorScheme(
    primary = Gold,
    onPrimary = Ink,
    primaryContainer = Gold.copy(alpha = 0.18f),
    onPrimaryContainer = Ink,

    secondary = Lapis,
    onSecondary = Color.White,
    secondaryContainer = Lapis.copy(alpha = 0.14f),
    onSecondaryContainer = Ink,

    tertiary = DeepGold,
    onTertiary = Ink,
    tertiaryContainer = DeepGold.copy(alpha = 0.16f),
    onTertiaryContainer = Ink,

    background = Papyrus,
    onBackground = Ink,

    surface = Sand,
    onSurface = Ink,
    surfaceVariant = Papyrus.copy(alpha = 0.60f),
    onSurfaceVariant = Ink.copy(alpha = 0.80f),

    outline = DeepGold.copy(alpha = 0.55f),
    outlineVariant = DeepGold.copy(alpha = 0.25f),

    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
)


private val DarkColors = darkColorScheme(
    primary = Gold,
    onPrimary = Color(0xFF0E0E0E),
    primaryContainer = Color(0xFF3A2E12),
    onPrimaryContainer = Color(0xFFFFE6A6),

    secondary = Lapis,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1E2A4A),
    onSecondaryContainer = Color(0xFFD8E2FF),

    tertiary = DeepGold,
    onTertiary = Color(0xFF0E0E0E),
    tertiaryContainer = Color(0xFF4A3510),
    onTertiaryContainer = Color(0xFFFFE2A8),

    background = Nile,
    onBackground = Color.White,

    surface = Color(0xFF0F1B26),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF13283A),
    onSurfaceVariant = Color.White.copy(alpha = 0.80f),

    outline = Gold.copy(alpha = 0.55f),
    outlineVariant = Gold.copy(alpha = 0.25f),

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
)


@Composable
fun MuseoInteractivoTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}