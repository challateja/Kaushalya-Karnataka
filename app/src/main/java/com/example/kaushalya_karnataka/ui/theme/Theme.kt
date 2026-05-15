package com.example.kaushalya_karnataka.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Orange60,
    onPrimary = Color.White,
    primaryContainer = Orange95,
    onPrimaryContainer = Orange10,
    secondary = Slate20,
    onSecondary = Color.White,
    secondaryContainer = Slate90,
    onSecondaryContainer = Slate10,
    tertiary = Green50,
    onTertiary = Color.White,
    background = Slate95,
    onBackground = Slate10,
    surface = Color.White,
    onSurface = Slate10,
    surfaceVariant = Slate90,
    onSurfaceVariant = Slate40,
    outline = Slate60,
    error = Red50,
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = Orange70,
    onPrimary = Orange20,
    primaryContainer = Orange30,
    onPrimaryContainer = Orange90,
    secondary = Slate70,
    onSecondary = Slate20,
    secondaryContainer = Slate30,
    onSecondaryContainer = Slate90,
    tertiary = Green50,
    onTertiary = Color.White,
    background = Slate10,
    onBackground = Slate90,
    surface = Slate20,
    onSurface = Slate90,
    surfaceVariant = Slate30,
    onSurfaceVariant = Slate60,
    outline = Slate50,
    error = Red50,
    onError = Color.White,
)

@Composable
fun KaushalyaKarnatakaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}