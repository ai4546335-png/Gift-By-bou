package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.sound.SoundSynth
import com.example.ui.components.HeartShape
import com.example.ui.components.InstallAppDialog
import com.example.ui.components.createHeartPath
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun WelcomeScreen(
  soundSynth: SoundSynth,
  onStartClicked: () -> Unit
) {
  var showInstallDialog by remember { mutableStateOf(false) }
  var isMuted by remember { mutableStateOf(soundSynth.isMuted) }

  // Heartbeat pulse animation
  val heartbeatTransition = rememberInfiniteTransition(label = "heartbeat")
  val heartScale by heartbeatTransition.animateFloat(
    initialValue = 1.0f,
    targetValue = 1.07f,
    animationSpec = infiniteRepeatable(
      animation = keyframes {
        durationMillis = 1100
        1.0f at 0 using FastOutSlowInEasing
        1.08f at 160 using FastOutSlowInEasing
        1.02f at 300 using FastOutSlowInEasing
        1.06f at 450 using FastOutSlowInEasing
        1.0f at 650 using FastOutSlowInEasing
        1.0f at 1100 using FastOutSlowInEasing
      },
      repeatMode = RepeatMode.Restart
    ),
    label = "heartScale"
  )

  // Glow aura pulse animation
  val auraAlpha by heartbeatTransition.animateFloat(
    initialValue = 0.35f,
    targetValue = 0.85f,
    animationSpec = infiniteRepeatable(
      animation = tween(1100, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "auraAlpha"
  )

  // Floating background stars animation
  val floatAnim by heartbeatTransition.animateFloat(
    initialValue = 0f,
    targetValue = 6.28318f,
    animationSpec = infiniteRepeatable(
      animation = tween(12000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "floatAnim"
  )

  // Subtle heartbeat audio pulse every cycle if unmuted
  LaunchedEffect(isMuted) {
    while (!isMuted) {
      soundSynth.playHeartbeat()
      kotlinx.coroutines.delay(1100)
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.radialGradient(
          colors = listOf(
            Color(0xFF280718),
            Color(0xFF14030D),
            Color(0xFF080106)
          ),
          center = Offset.Unspecified,
          radius = 1200f
        )
      )
  ) {
    // Ambient floating star dust particles
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height
      val random = Random(1234)
      for (i in 0 until 40) {
        val baseX = random.nextFloat() * w
        val baseY = random.nextFloat() * h
        val radius = 1.5f + random.nextFloat() * 3f
        val driftX = sin(floatAnim + i) * 15f
        val driftY = cos(floatAnim + i * 0.7f) * 15f
        val particleColor = if (i % 3 == 0) Color(0xFFFF4081) else Color(0xFFFFD1DC)

        drawCircle(
          color = particleColor.copy(alpha = 0.25f + sin(floatAnim + i).coerceAtLeast(0f) * 0.45f),
          radius = radius,
          center = Offset((baseX + driftX).coerceIn(0f, w), (baseY + driftY).coerceIn(0f, h))
        )
      }
    }

    // Top action bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 20.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(Color(0x33FF1E6D))
          .border(1.dp, Color(0x66FF1E6D), RoundedCornerShape(20.dp))
          .padding(horizontal = 12.dp, vertical = 6.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Favorite,
          contentDescription = null,
          tint = Color(0xFFFF1E6D),
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "BU JAN's VAULT",
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp,
          letterSpacing = 1.sp,
          color = Color(0xFFFF8DA1)
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        // Sound toggle button
        IconButton(
          onClick = {
            isMuted = !isMuted
            soundSynth.isMuted = isMuted
            if (!isMuted) soundSynth.playBeep(600f, 60)
          },
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0x22FFFFFF))
        ) {
          Icon(
            imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
            contentDescription = if (isMuted) "Unmute" else "Mute",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Install App Option
        Button(
          onClick = {
            soundSynth.playBeep(700f, 60)
            showInstallDialog = true
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0x33FFFFFF)
          ),
          shape = RoundedCornerShape(20.dp),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
          modifier = Modifier
            .border(1.dp, Color(0x55FFFFFF), RoundedCornerShape(20.dp))
            .testTag("welcome_install_button")
        ) {
          Icon(
            imageVector = Icons.Default.Download,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Install",
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }

    // Centerpiece: The Heart Shape greeting
    BoxWithConstraints(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp),
      contentAlignment = Alignment.Center
    ) {
      val maxW = maxWidth
      val heartWidth = (maxW * 0.95f).coerceAtMost(360.dp)
      val heartHeight = heartWidth * 0.96f

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        // Glowing animated Heart Box
        Box(
          modifier = Modifier
            .size(heartWidth, heartHeight)
            .scale(heartScale),
          contentAlignment = Alignment.Center
        ) {
          // Canvas rendering the glowing heartbeat outline & subtle inner vignette
          Canvas(modifier = Modifier.fillMaxSize()) {
            val path = createHeartPath(size.width, size.height)

            // Outer neon glow shadow
            drawPath(
              path = path,
              color = Color(0xFFFF1E6D).copy(alpha = auraAlpha * 0.45f),
              style = Stroke(width = 24f, cap = StrokeCap.Round)
            )

            // Crisp neon border line
            drawPath(
              path = path,
              brush = Brush.linearGradient(
                listOf(
                  Color(0xFFFF2A7A),
                  Color(0xFFFF7597),
                  Color(0xFFFF1E6D),
                  Color(0xFFFFB6C1)
                )
              ),
              style = Stroke(width = 5f, cap = StrokeCap.Round)
            )
          }

          // Content strictly clipped inside the Heart Shape
          Box(
            modifier = Modifier
              .fillMaxSize()
              .clip(HeartShape())
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    Color(0xF03B0A20),
                    Color(0xFA240514),
                    Color(0xFF16020C)
                  )
                )
              )
              .padding(horizontal = 28.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center,
              modifier = Modifier.padding(top = 18.dp)
            ) {
              // Decorative Sparkle & Heart Badge
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = null,
                  tint = Color(0xFFFFD700),
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "FOR MY FAVORITE PERSON",
                  fontFamily = FontFamily.Monospace,
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp,
                  letterSpacing = 1.5.sp,
                  color = Color(0xFFFFB3C6)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = null,
                  tint = Color(0xFFFFD700),
                  modifier = Modifier.size(16.dp)
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Greeting title
              Text(
                text = stringResource(R.string.welcome_greeting),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp
              )

              Spacer(modifier = Modifier.height(10.dp))

              // The inviting message
              Text(
                text = stringResource(R.string.welcome_body),
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFFFE0E8),
                textAlign = TextAlign.Center,
                lineHeight = 18.5.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
              )

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = stringResource(R.string.welcome_invitation),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF8DA1),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // The "START" Button
        Box(
          contentAlignment = Alignment.Center
        ) {
          // Button glowing underlay
          Box(
            modifier = Modifier
              .width(220.dp)
              .height(56.dp)
              .scale(1.04f)
              .clip(RoundedCornerShape(30.dp))
              .background(
                Brush.horizontalGradient(
                  listOf(Color(0xFFFF1E6D), Color(0xFFFF528E), Color(0xFFFF1E6D))
                )
              )
              .blurEffect(auraAlpha)
          )

          Button(
            onClick = {
              soundSynth.playCyberBreach()
              onStartClicked()
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
              .width(220.dp)
              .height(56.dp)
              .background(
                Brush.horizontalGradient(
                  listOf(
                    Color(0xFFFF0055),
                    Color(0xFFFF2E7A),
                    Color(0xFFFF0055)
                  )
                ),
                RoundedCornerShape(30.dp)
              )
              .border(
                width = 2.dp,
                brush = Brush.horizontalGradient(
                  listOf(Color(0xFFFFFFFF), Color(0xFFFFB6C1), Color(0xFFFFFFFF))
                ),
                shape = RoundedCornerShape(30.dp)
              )
              .testTag("start_button")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.LockOpen,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = stringResource(R.string.btn_start).uppercase(),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                color = Color.White
              )
              Spacer(modifier = Modifier.width(8.dp))
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Romantic status indicator below start
        Text(
          text = "● 100% ENCRYPTED WITH LOVE",
          fontFamily = FontFamily.Monospace,
          fontSize = 11.sp,
          color = Color(0x99FFFFFF),
          letterSpacing = 1.sp
        )
      }
    }

    // Bottom install helper footer
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .navigationBarsPadding()
        .padding(bottom = 16.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(16.dp))
          .clickable {
            soundSynth.playBeep(800f, 60)
            showInstallDialog = true
          }
          .padding(horizontal = 14.dp, vertical = 8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Download,
          contentDescription = null,
          tint = Color(0xFFFF8DA1),
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Want to keep this forever? Tap to Install App",
          fontSize = 12.sp,
          color = Color(0xFFFFB6C1),
          fontWeight = FontWeight.Medium
        )
      }
    }

    if (showInstallDialog) {
      InstallAppDialog(
        onDismiss = { showInstallDialog = false },
        onActionComplete = { _ -> }
      )
    }
  }
}

// Extension function for soft blur simulation
private fun Modifier.blurEffect(alpha: Float): Modifier = this.then(
  Modifier.border(
    width = (6 * alpha).dp,
    brush = Brush.radialGradient(
      listOf(Color(0x66FF1E6D), Color.Transparent)
    ),
    shape = RoundedCornerShape(30.dp)
  )
)
