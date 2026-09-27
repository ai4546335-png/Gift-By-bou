package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = CyberPink,
  onPrimary = Color.White,
  secondary = CyberGreen,
  onSecondary = Color.Black,
  tertiary = CyberCyan,
  background = CyberBlack,
  onBackground = Color(0xFFE2E8F0),
  surface = CyberDarkSurface,
  onSurface = Color(0xFFE2E8F0),
  surfaceVariant = Color(0xFF142118),
  onSurfaceVariant = Color(0xFFA6FFCE)
)

private val LightColorScheme = DarkColorScheme // Always use immersive dark cyberpunk aesthetic

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep intentional cyber palette
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content
  )
}
