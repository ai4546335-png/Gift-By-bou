package com.example.sound

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Procedural retro sound synthesizer for cyberpunk hacking soundscapes and heartbeats.
 * Generates audio on the fly with no external mp3 files required.
 */
class SoundSynth {
  private val scope = CoroutineScope(Dispatchers.Default + Job())
  var isMuted: Boolean = false

  fun playBeep(frequency: Float = 880f, durationMs: Int = 50) {
    if (isMuted) return
    scope.launch {
      try {
        generateTone(frequency, durationMs, 0.25f)
      } catch (_: Exception) {}
    }
  }

  fun playHeartbeat() {
    if (isMuted) return
    scope.launch {
      try {
        // Deep double thump "lub-dub"
        generateTone(75f, 90, 0.45f)
        Thread.sleep(120)
        generateTone(65f, 130, 0.35f)
      } catch (_: Exception) {}
    }
  }

  fun playCyberBreach() {
    if (isMuted) return
    scope.launch {
      try {
        // Frequency sweep up
        var freq = 300f
        while (freq < 1400f) {
          generateTone(freq, 25, 0.3f)
          freq += 150f
        }
        generateTone(1200f, 150, 0.4f)
      } catch (_: Exception) {}
    }
  }

  fun playTerminalKeystroke() {
    if (isMuted) return
    scope.launch {
      try {
        val randomFreq = 600f + (Math.random().toFloat() * 400f)
        generateTone(randomFreq, 20, 0.15f)
      } catch (_: Exception) {}
    }
  }

  private fun generateTone(frequency: Float, durationMs: Int, volume: Float) {
    val sampleRate = 22050
    val numSamples = (sampleRate * (durationMs / 1000f)).toInt().coerceAtLeast(1)
    val buffer = ShortArray(numSamples)

    val angularFreq = 2.0 * Math.PI * frequency / sampleRate
    for (i in 0 until numSamples) {
      // Apply linear fade-in and fade-out envelope to avoid audio clicks
      val envelope = when {
        i < numSamples / 8 -> i.toFloat() / (numSamples / 8)
        i > numSamples * 7 / 8 -> (numSamples - i).toFloat() / (numSamples / 8)
        else -> 1.0f
      }
      val sample = (sin(i * angularFreq) * Short.MAX_VALUE * volume * envelope).toInt()
      buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
    }

    val audioTrack = AudioTrack.Builder()
      .setAudioAttributes(
        AudioAttributes.Builder()
          .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
          .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
          .build()
      )
      .setAudioFormat(
        AudioFormat.Builder()
          .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
          .setSampleRate(sampleRate)
          .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
          .build()
      )
      .setBufferSizeInBytes(buffer.size * 2)
      .setTransferMode(AudioTrack.MODE_STATIC)
      .build()

    audioTrack.write(buffer, 0, buffer.size)
    audioTrack.play()
    Thread.sleep(durationMs.toLong() + 30)
    audioTrack.stop()
    audioTrack.release()
  }
}
