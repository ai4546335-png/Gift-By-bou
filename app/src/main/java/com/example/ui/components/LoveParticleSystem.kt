package com.example.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

enum class SwarmMode(val title: String) {
  HYPER_MATRIX("Matrix Rain"),
  COSMIC_SWARM("Floating Swarm"),
  HEART_VORTEX("Cyber Vortex"),
  HEART_CONSTELLATION("Love Constellation")
}

data class FloatingLoveParticle(
  var x: Float,
  var y: Float,
  var vx: Float,
  var vy: Float,
  var size: Float,
  var alpha: Float,
  var color: Int,
  var phase: Float,
  var rotation: Float,
  var vRot: Float,
  val text: String = "I Love You Bu Jan"
)

data class TouchBurstParticle(
  var x: Float,
  var y: Float,
  var vx: Float,
  var vy: Float,
  var life: Float, // 1.0 down to 0
  var maxLife: Float,
  var size: Float,
  var color: Int,
  val text: String
)

data class MatrixColumn(
  val x: Float,
  var yOffset: Float,
  val speed: Float,
  val fontSize: Float,
  val phrase: String = "I LOVE YOU BU JAN  "
)

class LoveParticleEngine(
  private val width: Float,
  private val height: Float
) {
  private val random = Random(42)

  val floatingParticles = ArrayList<FloatingLoveParticle>()
  val matrixColumns = ArrayList<MatrixColumn>()
  val touchBursts = ArrayList<TouchBurstParticle>()

  private val colors = intArrayOf(
    0xFF00FF66.toInt(), // Matrix green
    0xFFFF1E6D.toInt(), // Cyber neon pink
    0xFF00F0FF.toInt(), // Cyan
    0xFFFFD700.toInt(), // Gold
    0xFFFFFFFF.toInt(), // Bright white
    0xFFFF66B2.toInt()  // Soft pink
  )

  init {
    // 1. Initialize floating 3D love particles
    val particleCount = 100
    for (i in 0 until particleCount) {
      floatingParticles.add(
        FloatingLoveParticle(
          x = random.nextFloat() * width,
          y = random.nextFloat() * height,
          vx = (random.nextFloat() - 0.5f) * 2.5f,
          vy = -0.5f - random.nextFloat() * 2.5f,
          size = 28f + random.nextFloat() * 32f,
          alpha = 0.25f + random.nextFloat() * 0.70f,
          color = colors[random.nextInt(colors.size)],
          phase = random.nextFloat() * 6.28f,
          rotation = (random.nextFloat() - 0.5f) * 25f,
          vRot = (random.nextFloat() - 0.5f) * 0.4f
        )
      )
    }

    // 2. Initialize dense Matrix columns for the raining code effect
    val colSpacing = 36f
    val numCols = (width / colSpacing).toInt().coerceAtLeast(10)
    for (i in 0 until numCols) {
      matrixColumns.add(
        MatrixColumn(
          x = i * colSpacing + (colSpacing / 2),
          yOffset = random.nextFloat() * height,
          speed = 2.5f + random.nextFloat() * 4.5f,
          fontSize = 18f + (i % 3) * 3f
        )
      )
    }
  }

  fun spawnBurst(x: Float, y: Float) {
    val burstTexts = arrayOf(
      "I Love You Bu Jan",
      "Bu Jan ♥",
      "I Love You",
      "FOREVER BU JAN",
      "♥ 100% LOVE ♥",
      "YOU ARE MY HEART"
    )
    for (i in 0 until 35) {
      val angle = (random.nextFloat() * 6.28f)
      val speed = 3f + random.nextFloat() * 8f
      touchBursts.add(
        TouchBurstParticle(
          x = x,
          y = y,
          vx = cos(angle) * speed,
          vy = sin(angle) * speed,
          life = 1f,
          maxLife = 1f,
          size = 22f + random.nextFloat() * 20f,
          color = colors[random.nextInt(colors.size)],
          text = burstTexts[random.nextInt(burstTexts.size)]
        )
      )
    }
  }

  fun update(mode: SwarmMode, overclock: Boolean, elapsedSeconds: Float) {
    val speedMultiplier = if (overclock) 2.2f else 1.0f

    // Update Matrix columns
    for (col in matrixColumns) {
      col.yOffset = (col.yOffset + col.speed * speedMultiplier) % (height + 400f)
    }

    val centerX = width / 2f
    val centerY = height / 2f

    // Update floating particles based on mode
    for ((index, p) in floatingParticles.withIndex()) {
      p.phase += 0.04f * speedMultiplier
      p.rotation += p.vRot * speedMultiplier

      when (mode) {
        SwarmMode.HYPER_MATRIX -> {
          p.y += (p.vy * 1.5f) * speedMultiplier
          p.x += sin(p.phase) * 1.5f
          if (p.y < -100f) {
            p.y = height + 50f
            p.x = random.nextFloat() * width
          }
        }

        SwarmMode.COSMIC_SWARM -> {
          p.x += p.vx * speedMultiplier
          p.y += p.vy * speedMultiplier
          if (p.x < -150f) p.x = width + 100f
          if (p.x > width + 150f) p.x = -100f
          if (p.y < -150f) p.y = height + 100f
          if (p.y > height + 150f) p.y = -100f
        }

        SwarmMode.HEART_VORTEX -> {
          // Orbit around center
          val dx = p.x - centerX
          val dy = p.y - centerY
          val dist = kotlin.math.sqrt(dx * dx + dy * dy).coerceAtLeast(30f)
          val angle = kotlin.math.atan2(dy, dx) + (0.025f * (200f / dist)) * speedMultiplier
          val targetDist = (dist + sin(p.phase) * 3f).coerceIn(40f, width * 0.6f)
          p.x = centerX + cos(angle) * targetDist
          p.y = centerY + sin(angle) * targetDist
        }

        SwarmMode.HEART_CONSTELLATION -> {
          // Particles attract to a mathematical heart shape outline
          val t = (index.toFloat() / floatingParticles.size) * 6.28318f + p.phase * 0.05f
          // Parametric heart formula
          val heartScale = (width.coerceAtMost(height) * 0.42f) / 16f
          val targetX = centerX + (16f * sin(t) * sin(t) * sin(t)) * heartScale
          val targetY = centerY - (13f * cos(t) - 5f * cos(2 * t) - 2f * cos(3 * t) - cos(4 * t)) * heartScale

          p.x += (targetX - p.x) * 0.07f * speedMultiplier + (sin(p.phase) * 1.2f)
          p.y += (targetY - p.y) * 0.07f * speedMultiplier + (cos(p.phase) * 1.2f)
        }
      }
    }

    // Update touch bursts
    val it = touchBursts.iterator()
    while (it.hasNext()) {
      val b = it.next()
      b.x += b.vx * speedMultiplier
      b.y += b.vy * speedMultiplier
      b.life -= 0.025f * speedMultiplier
      if (b.life <= 0f) {
        it.remove()
      }
    }
  }

  fun render(
    drawScope: DrawScope,
    mode: SwarmMode,
    textPaint: Paint,
    glowPaint: Paint
  ) {
    val canvas = drawScope.drawContext.canvas.nativeCanvas

    // 1. Render dense Matrix Rain columns of "I Love You Bu Jan"
    // Each column cascades multiple chunks down the display
    if (mode == SwarmMode.HYPER_MATRIX || mode == SwarmMode.COSMIC_SWARM) {
      val stepY = 28f
      for (col in matrixColumns) {
        textPaint.textSize = col.fontSize
        textPaint.typeface = Typeface.MONOSPACE
        val totalRows = ((height + 400f) / stepY).toInt()

        for (row in 0 until totalRows) {
          val yPos = (col.yOffset + row * stepY) % (height + 300f) - 150f
          val charIndex = (row) % col.phrase.length
          val charStr = col.phrase[charIndex].toString()

          // The leading tip glows white/bright pink
          val isHead = row % 14 == 0
          if (isHead) {
            textPaint.color = 0xFFFFFFFF.toInt()
            textPaint.alpha = 240
          } else {
            // Neon matrix green with cyber fade
            val fade = ((row % 14).toFloat() / 14f)
            textPaint.color = 0xFF00FF66.toInt()
            textPaint.alpha = (50 + (fade * 170)).toInt().coerceIn(30, 220)
          }

          canvas.drawText(charStr, col.x, yPos, textPaint)
        }
      }
    }

    // 2. Render dense background repeat phrase ribbons (adds hundreds of instances)
    val ribbonStep = 110f
    val ribbonRows = (height / ribbonStep).toInt() + 2
    textPaint.textSize = 14f
    textPaint.typeface = Typeface.MONOSPACE
    textPaint.color = 0xFF00FF66.toInt()
    textPaint.alpha = 35

    val repeatingBanner = "✦ I LOVE YOU BU JAN ✦ I LOVE YOU BU JAN ✦ I LOVE YOU BU JAN ✦ I LOVE YOU BU JAN ✦ "
    for (r in 0 until ribbonRows) {
      val offsetX = if (r % 2 == 0) -40f else -120f
      canvas.drawText(repeatingBanner, offsetX, r * ribbonStep, textPaint)
    }

    // 3. Render 3D Floating Love Particles with glowing shadow
    for (p in floatingParticles) {
      textPaint.textSize = p.size
      textPaint.typeface = Typeface.DEFAULT_BOLD
      textPaint.color = p.color
      textPaint.alpha = (p.alpha * 255).toInt().coerceIn(10, 255)

      // Soft glow shadow behind text
      glowPaint.textSize = p.size
      glowPaint.typeface = Typeface.DEFAULT_BOLD
      glowPaint.color = p.color
      glowPaint.alpha = (p.alpha * 80).toInt().coerceIn(5, 120)

      canvas.save()
      canvas.translate(p.x, p.y)
      canvas.rotate(p.rotation)

      // Draw subtle glow offset
      canvas.drawText(p.text, -2f, -2f, glowPaint)
      canvas.drawText(p.text, 2f, 2f, glowPaint)
      // Draw crisp foreground text
      canvas.drawText(p.text, 0f, 0f, textPaint)

      canvas.restore()
    }

    // 4. Render interactive touch explosions
    for (b in touchBursts) {
      textPaint.textSize = b.size
      textPaint.typeface = Typeface.DEFAULT_BOLD
      textPaint.color = b.color
      textPaint.alpha = (b.life * 255).toInt().coerceIn(0, 255)

      canvas.drawText(b.text, b.x, b.y, textPaint)
    }
  }
}
