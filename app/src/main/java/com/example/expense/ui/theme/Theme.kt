package com.example.expense.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BlackPrimary,
    onPrimary = BlackOnPrimary,
    secondary = BlackSurfaceVariant,
    onSecondary = BlackOnSurface,
    tertiary = BlackSurface,
    background = BlackBackground,
    surface = BlackSurface,
    surfaceVariant = BlackSurfaceVariant,
    onBackground = BlackOnBackground,
    onSurface = BlackOnSurface,
    onSurfaceVariant = BlackOnSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    secondary = LightSurfaceVariant,
    onSecondary = LightOnSurface,
    tertiary = LightSurface,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurfaceVariant
)

@Composable
fun ExpenseTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}