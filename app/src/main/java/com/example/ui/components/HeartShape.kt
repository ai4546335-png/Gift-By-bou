package com.example.ui.components

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * Custom Compose Shape that carves out an elegant, symmetrical heart.
 */
class HeartShape : Shape {
  override fun createOutline(
    size: Size,
    layoutDirection: LayoutDirection,
    density: Density
  ): Outline {
    val path = createHeartPath(size.width, size.height)
    return Outline.Generic(path)
  }
}

/**
 * Generates an SVG-style mathematical heart path scaled to given width and height.
 */
fun createHeartPath(width: Float, height: Float): Path {
  return Path().apply {
    val w = width
    val h = height

    // Top cleft of the heart
    moveTo(w * 0.5f, h * 0.26f)

    // Left lobe
    cubicTo(
      w * 0.44f, h * 0.12f,
      w * 0.28f, h * 0.05f,
      w * 0.14f, h * 0.18f
    )
    cubicTo(
      w * 0.00f, h * 0.32f,
      w * 0.02f, h * 0.52f,
      w * 0.22f, h * 0.72f
    )

    // To bottom tip
    cubicTo(
      w * 0.34f, h * 0.84f,
      w * 0.46f, h * 0.94f,
      w * 0.50f, h * 1.00f
    )

    // From bottom tip up right lobe
    cubicTo(
      w * 0.54f, h * 0.94f,
      w * 0.66f, h * 0.84f,
      w * 0.78f, h * 0.72f
    )
    cubicTo(
      w * 0.98f, h * 0.52f,
      w * 1.00f, h * 0.32f,
      w * 0.86f, h * 0.18f
    )
    cubicTo(
      w * 0.72f, h * 0.05f,
      w * 0.56f, h * 0.12f,
      w * 0.50f, h * 0.26f
    )

    close()
  }
}
