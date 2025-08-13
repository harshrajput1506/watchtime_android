package com.app.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    secondary = AccentColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary
)

@Composable
fun WatchTimeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        //typography = Typography,
       // shapes = Shapes,
        content = content
    )
}
