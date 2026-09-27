package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.sound.SoundSynth
import com.example.ui.screens.HackingScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme

enum class ScreenState {
  WELCOME,
  HACKING_TERMINAL
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = Color(0xFF030705)
        ) {
          LoveAppNavigation()
        }
      }
    }
  }
}

@Composable
fun LoveAppNavigation() {
  var currentScreen by remember { mutableStateOf(ScreenState.WELCOME) }
  val soundSynth = remember { SoundSynth() }

  AnimatedContent(
    targetState = currentScreen,
    transitionSpec = {
      if (targetState == ScreenState.HACKING_TERMINAL) {
        (scaleIn(initialScale = 0.85f) + fadeIn()).togetherWith(
          scaleOut(targetScale = 1.15f) + fadeOut()
        )
      } else {
        (scaleIn(initialScale = 1.1f) + fadeIn()).togetherWith(
          scaleOut(targetScale = 0.9f) + fadeOut()
        )
      }
    },
    label = "screen_transition"
  ) { screen ->
    when (screen) {
      ScreenState.WELCOME -> {
        WelcomeScreen(
          soundSynth = soundSynth,
          onStartClicked = {
            currentScreen = ScreenState.HACKING_TERMINAL
          }
        )
      }
      ScreenState.HACKING_TERMINAL -> {
        HackingScreen(
          soundSynth = soundSynth,
          onNavigateBack = {
            currentScreen = ScreenState.WELCOME
          }
        )
      }
    }
  }
}
