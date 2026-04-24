package com.example.expense.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val BlackColorScheme = darkColorScheme(
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

@Composable
fun ExpenseTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BlackColorScheme,
        typography = Typography,
        content = content
    )
}