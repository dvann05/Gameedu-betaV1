package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.model.ThemeType

@Composable
fun KidsLearningWorldTheme(
    selectedTheme: ThemeType = ThemeType.RAINBOW,
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = selectedTheme.primaryColor,
        secondary = selectedTheme.secondaryColor,
        background = selectedTheme.backgroundColor,
        surface = Color.White,
        primaryContainer = Color.White,
        onPrimaryContainer = Color(0xFF1E293B),
        secondaryContainer = Color(0xFFF1F5F9),
        onSecondaryContainer = Color(0xFF0F172A),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color(0xFF1E293B),
        onSurface = Color(0xFF1E293B)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

