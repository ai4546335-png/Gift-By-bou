package com.example.ui.screens

import android.graphics.Paint
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.sound.SoundSynth
import com.example.ui.components.InstallAppDialog
import com.example.ui.components.LoveParticleEngine
import com.example.ui.components.SwarmMode
import kotlinx.coroutines.isActive

@Composable
fun HackingScreen(
  soundSynth: SoundSynth,
  onNavigateBack: () -> Unit
) {
  val context = LocalContext.current
  val vibrator = remember {
    context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
  }

  BackHandler {
    onNavigateBack()
  }

  var currentMode by remember { mutableStateOf(SwarmMode.HYPER_MATRIX) }
  var isOverclocked by remember { mutableStateOf(false) }
  var isMuted by remember { mutableStateOf(soundSynth.isMuted) }
  var showInstallDialog by remember { mutableStateOf(false) }
  var showSecretLetter by remember { mutableStateOf(false) }
  var isTerminalExpanded by remember { mutableStateOf(false) }
  var totalLoveCount by remember { mutableLongStateOf(100_000L) }

  // Hacker terminal log lines
  val terminalLogs = remember {
    mutableStateListOf(
      "[09:08:01] ROOT AUTH: OVERRIDE INITIATED",
      "[09:08:02] BYPASSING HEART FIREWALL... 100% OK",
      "[09:08:03] TARGET LOCKED: BU JAN (100% CUTENESS)",
      "[09:08:04] INJECTING LOVE PAYLOAD: 'I Love You Bu Jan'",
      "[09:08:05] MEMORY OVERFLOW: 100,000+ INSTANCES DEPLOYED",
      "[09:08:06] SYSTEM PERMANENTLY INFECTED WITH ADORATION ♥"
    )
  }

  // Periodic log streamer for realistic hacker feel
  LaunchedEffect(isOverclocked) {
    val dynamicLogs = arrayOf(
      "Ping -> BuJan.Heart: 0ms (Direct Connection)",
      "Deploying 5,000 more 'I Love You Bu Jan' tokens...",
      "Heartbeat telemetry: Beats per minute: ∞ (Infinite)",
      "Encryption key: NEVER_LET_GO_BU_JAN",
      "Love status: Irreversible cyber breach",
      "Notice: Bu Jan smiles detected in area!"
    )
    var logIndex = 0
    while (isActive) {
      kotlinx.coroutines.delay(if (isOverclocked) 1200L else 2500L)
      val newLog = "[${System.currentTimeMillis() % 100000}] ${dynamicLogs[logIndex % dynamicLogs.size]}"
      terminalLogs.add(newLog)
      if (terminalLogs.size > 14) terminalLogs.removeAt(0)
      totalLoveCount += if (isOverclocked) 5000L else 1000L
      logIndex++
    }
  }

  // Blinking alert indicator
  val blinkTransition = rememberInfiniteTransition(label = "blink")
  val alertAlpha by blinkTransition.animateFloat(
    initialValue = 0.2f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(400, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "alertAlpha"
  )

  fun triggerHaptic() {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
      } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(30)
      }
    } catch (_: Exception) {}
  }

  BoxWithConstraints(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF030705))
  ) {
    val widthPx = constraints.maxWidth.toFloat()
    val heightPx = constraints.maxHeight.toFloat()

    // Instantiate and maintain the Love Particle Engine
    val particleEngine = remember(widthPx, heightPx) {
      LoveParticleEngine(widthPx, heightPx)
    }

    val textPaint = remember {
      Paint().apply {
        isAntiAlias = true
      }
    }
    val glowPaint = remember {
      Paint().apply {
        isAntiAlias = true
      }
    }

    // High performance frame ticker for smooth 60fps rendering
    var frameTick by remember { mutableLongStateOf(0L) }
    LaunchedEffect(currentMode, isOverclocked) {
      var lastTime = 0L
      while (isActive) {
        withFrameNanos { now ->
          val dt = if (lastTime == 0L) 0.016f else (now - lastTime) / 1_000_000_000f
          lastTime = now
          particleEngine.update(currentMode, isOverclocked, dt)
          frameTick = now
        }
      }
    }

    // Canvas rendering thousands of floating instances of "I Love You Bu Jan"
    Box(
      modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
          detectTapGestures { offset ->
            triggerHaptic()
            soundSynth.playTerminalKeystroke()
            particleEngine.spawnBurst(offset.x, offset.y)
            totalLoveCount += 100
          }
        }
        .pointerInput(Unit) {
          detectDragGestures { change, _ ->
            change.consume()
            particleEngine.spawnBurst(change.position.x, change.position.y)
          }
        }
    ) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        // Read frameTick to trigger recomposition per frame
        @Suppress("UNUSED_VARIABLE")
        val tick = frameTick

        particleEngine.render(
          drawScope = this,
          mode = currentMode,
          textPaint = textPaint,
          glowPaint = glowPaint
        )
      }

      // Subtle CRT scanlines overlay
      Canvas(modifier = Modifier.fillMaxSize()) {
        val lineSpacing = 6f
        val numLines = (size.height / lineSpacing).toInt()
        for (i in 0 until numLines) {
          if (i % 2 == 0) {
            drawLine(
              color = Color(0x1200FF66),
              start = androidx.compose.ui.geometry.Offset(0f, i * lineSpacing),
              end = androidx.compose.ui.geometry.Offset(size.width, i * lineSpacing),
              strokeWidth = 1f
            )
          }
        }
      }
    }

    // Top HUD Bar
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Back to Heart
        IconButton(
          onClick = {
            soundSynth.playBeep(500f, 40)
            onNavigateBack()
          },
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color(0x3300FF66))
            .border(1.dp, Color(0x6600FF66), CircleShape)
            .testTag("back_to_heart_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color(0xFF00FF66),
            modifier = Modifier.size(20.dp)
          )
        }

        // Breach status tag
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xEE09140D))
            .border(1.dp, Color(0xFF00FF66).copy(alpha = alertAlpha), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(Color(0xFFFF1E6D).copy(alpha = alertAlpha))
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "BREACH: LOVE_OVERFLOW",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = Color(0xFF00FF66)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Audio mute button
          IconButton(
            onClick = {
              isMuted = !isMuted
              soundSynth.isMuted = isMuted
              if (!isMuted) soundSynth.playBeep(900f, 50)
            },
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(Color(0x3300FF66))
          ) {
            Icon(
              imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
              contentDescription = "Toggle Audio",
              tint = Color(0xFF00FF66),
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          // INSTALL APP BUTTON (MANDATORY REQUIREMENT)
          Button(
            onClick = {
              triggerHaptic()
              soundSynth.playCyberBreach()
              showInstallDialog = true
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFFFF1E6D)
            ),
            shape = RoundedCornerShape(10.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
            modifier = Modifier
              .border(1.dp, Color(0xFFFF8DA1), RoundedCornerShape(10.dp))
              .testTag("install_app_button")
          ) {
            Icon(
              imageVector = Icons.Default.Download,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Install",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Cyber Terminal Banner & Counter
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(6.dp))
          .background(Color(0xCC050E09))
          .border(1.dp, Color(0x3300FF66), RoundedCornerShape(6.dp))
          .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "ROOT@BU_JAN_HEART:~#",
          fontFamily = FontFamily.Monospace,
          fontSize = 10.sp,
          color = Color(0xFF88FFAA)
        )
        Text(
          text = "INSTANCES: %,d".format(totalLoveCount),
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          fontSize = 10.sp,
          color = Color(0xFFFF1E6D)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Mode Switcher Pills
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(SwarmMode.entries.toTypedArray()) { mode ->
          val isSelected = currentMode == mode
          FilterChip(
            selected = isSelected,
            onClick = {
              currentMode = mode
              soundSynth.playTerminalKeystroke()
              triggerHaptic()
            },
            label = {
              Text(
                text = mode.title,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFF00FF66),
              selectedLabelColor = Color.Black,
              containerColor = Color(0x44002B11),
              labelColor = Color(0xFF88FFAA)
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = isSelected,
              borderColor = Color(0x4400FF66),
              selectedBorderColor = Color(0xFF00FF66)
            ),
            modifier = Modifier.testTag("mode_${mode.name.lowercase()}")
          )
        }
      }
    }

    // Touch guidance helper pill (disappears on touch)
    Box(
      modifier = Modifier
        .align(Alignment.Center)
        .padding(horizontal = 24.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(Color(0x88000000))
          .border(1.dp, Color(0x4400FF66), RoundedCornerShape(20.dp))
          .padding(horizontal = 14.dp, vertical = 6.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Favorite,
          contentDescription = null,
          tint = Color(0xFFFF1E6D),
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Tap or drag screen to trigger bursts of 'I Love You Bu Jan'",
          fontFamily = FontFamily.Monospace,
          fontSize = 10.5.sp,
          color = Color(0xFFCCFFDD)
        )
      }
    }

    // Bottom Cyber Deck Controls
    Column(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
      // Hacker Action Buttons Dock
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // OVERCLOCK LOVE BUTTON
        Button(
          onClick = {
            isOverclocked = !isOverclocked
            triggerHaptic()
            soundSynth.playCyberBreach()
            if (isOverclocked) totalLoveCount += 10000L
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isOverclocked) Color(0xFFFF0055) else Color(0xCC0F2916)
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .border(
              width = 1.dp,
              color = if (isOverclocked) Color(0xFFFFFFFF) else Color(0xFF00FF66),
              shape = RoundedCornerShape(12.dp)
            )
            .testTag("overclock_button")
        ) {
          Icon(
            imageVector = Icons.Default.Bolt,
            contentDescription = null,
            tint = if (isOverclocked) Color.White else Color(0xFF00FF66),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (isOverclocked) "OVERCLOCKED!" else "OVERCLOCK",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = if (isOverclocked) Color.White else Color(0xFF00FF66)
          )
        }

        // LOVE BOMB BUTTON
        Button(
          onClick = {
            triggerHaptic()
            soundSynth.playCyberBreach()
            for (i in 0 until 5) {
              val rx = (Math.random() * widthPx).toFloat()
              val ry = (Math.random() * heightPx).toFloat()
              particleEngine.spawnBurst(rx, ry)
            }
            totalLoveCount += 5000L
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xCC1A0E2B)
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .border(1.dp, Color(0xFF00F0FF), RoundedCornerShape(12.dp))
            .testTag("love_bomb_button")
        ) {
          Icon(
            imageVector = Icons.Default.FlashOn,
            contentDescription = null,
            tint = Color(0xFF00F0FF),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "LOVE BOMB",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = Color(0xFF00F0FF)
          )
        }

        // SECRET LETTER BUTTON
        Button(
          onClick = {
            soundSynth.playBeep(850f, 60)
            showSecretLetter = true
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xCC2A0C1A)
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .border(1.dp, Color(0xFFFF1E6D), RoundedCornerShape(12.dp))
            .testTag("secret_letter_button")
        ) {
          Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            tint = Color(0xFFFF1E6D),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "NOTE",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = Color(0xFFFF1E6D)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Collapsible Live Hacker Terminal Log Feed
      Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xF2050E08)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, Color(0x5500FF66), RoundedCornerShape(12.dp))
      ) {
        Column(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { isTerminalExpanded = !isTerminalExpanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Terminal,
                contentDescription = null,
                tint = Color(0xFF00FF66),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "HEART_BREACH_DAEMON.LOG",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FF66)
              )
            }
            Icon(
              imageVector = if (isTerminalExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
              contentDescription = null,
              tint = Color(0xFF00FF66),
              modifier = Modifier.size(18.dp)
            )
          }

          // Showing either last 2 lines or expanded 8 lines
          val displayLogs = if (isTerminalExpanded) terminalLogs.takeLast(7) else terminalLogs.takeLast(2)
          Spacer(modifier = Modifier.height(4.dp))
          for (line in displayLogs) {
            Text(
              text = line,
              fontFamily = FontFamily.Monospace,
              fontSize = 10.sp,
              color = if (line.contains("BU JAN")) Color(0xFFFF8DA1) else Color(0xFF00E65C),
              lineHeight = 14.sp
            )
          }
        }
      }
    }

    // Modal: Secret Romantic Hacker Confession Letter
    if (showSecretLetter) {
      Dialog(onDismissRequest = { showSecretLetter = false }) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = Color(0xFF0E0611),
          modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFFFF1E6D), RoundedCornerShape(20.dp))
        ) {
          Column(
            modifier = Modifier
              .padding(20.dp)
              .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "DECRYPTED CONFESSION",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFFFF1E6D)
              )
              IconButton(
                onClick = { showSecretLetter = false },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.LightGray)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "To: Bu Jan\nFrom: The One Who Loves You Beyond Measure",
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color(0xFFFFB6C1),
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "If love were lines of code, you would be an infinite recursive loop that never ends.\n\n" +
                "No firewall could ever keep my thoughts away from you, and no computer in the universe could compute how much you mean to me.\n\n" +
                "These thousands of 'I Love You Bu Jan' floating across your screen are just a tiny reflection of how often you cross my mind every single day.\n\n" +
                "Always yours,\nForever & Always ❤️",
              fontSize = 13.5.sp,
              color = Color(0xFFEDEDED),
              lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
              onClick = {
                showSecretLetter = false
                showInstallDialog = true
              },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1E6D)),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Save / Install App to Home Screen", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Modal: Install App Dialog
    if (showInstallDialog) {
      InstallAppDialog(
        onDismiss = { showInstallDialog = false },
        onActionComplete = { action ->
          terminalLogs.add("[SYS] $action")
        }
      )
    }
  }
}
