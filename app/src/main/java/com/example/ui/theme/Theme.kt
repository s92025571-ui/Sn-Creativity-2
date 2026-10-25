package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.data.AppThemeMode

@Immutable
data class StudioExtendedColors(
    val isDark: Boolean,
    val glowPrimary: Color,
    val glowSecondary: Color,
    val cardBorder: Color,
    val elevatedCardSurface: Color,
    val accentGradientStart: Color,
    val accentGradientEnd: Color,
    val badgeSurface: Color
)

val LocalStudioColors = staticCompositionLocalOf {
    StudioExtendedColors(
        isDark = true,
        glowPrimary = Color(0x66C06CFF),
        glowSecondary = Color(0x44FF59C7),
        cardBorder = NeonDarkCardBorder,
        elevatedCardSurface = Color(0xFF171129),
        accentGradientStart = Color(0xFF9E47FF),
        accentGradientEnd = Color(0xFFE648B8),
        badgeSurface = Color(0xFF281B47)
    )
}

private val NeonDarkColorScheme = darkColorScheme(
    primary = NeonPurplePrimary,
    onPrimary = NeonPurpleOnPrimary,
    primaryContainer = NeonPurpleContainer,
    onPrimaryContainer = NeonPurpleOnContainer,
    secondary = NeonVioletSecondary,
    onSecondary = Color(0xFF140736),
    secondaryContainer = Color(0xFF2A1D4D),
    onSecondaryContainer = Color(0xFFE5DEFF),
    tertiary = NeonMagentaTertiary,
    onTertiary = Color(0xFF380025),
    tertiaryContainer = Color(0xFF52123E),
    onTertiaryContainer = Color(0xFFFFD8EC),
    background = NeonDarkBackground,
    onBackground = NeonDarkOnBackground,
    surface = NeonDarkSurface,
    onSurface = NeonDarkOnSurface,
    surfaceVariant = NeonDarkSurfaceVariant,
    onSurfaceVariant = NeonDarkOnSurfaceVariant,
    outline = NeonDarkCardBorder,
    outlineVariant = Color(0xFF281D45),
    error = NeonDarkError,
    onError = Color(0xFF3B000B)
)

private val CreativeLightColorScheme = lightColorScheme(
    primary = CreativePurplePrimary,
    onPrimary = CreativePurpleOnPrimary,
    primaryContainer = CreativePurpleContainer,
    onPrimaryContainer = CreativePurpleOnContainer,
    secondary = CreativeVioletSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE6DEFF),
    onSecondaryContainer = Color(0xFF1D1148),
    tertiary = CreativeMagentaTertiary,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD8EC),
    onTertiaryContainer = Color(0xFF3B002B),
    background = CreativeLightBackground,
    onBackground = CreativeLightOnBackground,
    surface = CreativeLightSurface,
    onSurface = CreativeLightOnSurface,
    surfaceVariant = CreativeLightSurfaceVariant,
    onSurfaceVariant = CreativeLightOnSurfaceVariant,
    outline = CreativeLightCardBorder,
    outlineVariant = Color(0xFFE3D9F7),
    error = CreativeLightError,
    onError = Color.White
)

private val NeonDarkExtended = StudioExtendedColors(
    isDark = true,
    glowPrimary = Color(0x59C06CFF),
    glowSecondary = Color(0x3DFF59C7),
    cardBorder = NeonDarkCardBorder,
    elevatedCardSurface = Color(0xFF161028),
    accentGradientStart = Color(0xFF9B42FF),
    accentGradientEnd = Color(0xFFE342B8),
    badgeSurface = Color(0xFF261942)
)

private val CreativeLightExtended = StudioExtendedColors(
    isDark = false,
    glowPrimary = Color(0x287B3FE4),
    glowSecondary = Color(0x1EC8429E),
    cardBorder = CreativeLightCardBorder,
    elevatedCardSurface = Color(0xFFFFFFFF),
    accentGradientStart = Color(0xFF6B2FD9),
    accentGradientEnd = Color(0xFFA9338B),
    badgeSurface = Color(0xFFEDE4FF)
)

val StudioShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun SNCreativityTheme(
    themeMode: AppThemeMode = AppThemeMode.NEON_DARK,
    content: @Composable () -> Unit
) {
    val isDark = themeMode == AppThemeMode.NEON_DARK
    val colorScheme = if (isDark) NeonDarkColorScheme else CreativeLightColorScheme
    val extendedColors = if (isDark) NeonDarkExtended else CreativeLightExtended

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !isDark
                controller.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    CompositionLocalProvider(LocalStudioColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = StudioShapes,
            content = content
        )
    }
}
