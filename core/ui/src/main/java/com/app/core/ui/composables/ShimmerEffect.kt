package com.app.core.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged

fun Modifier.shimmer(
    baseColor: Color? = null,
    highlightColor: Color? = null,
    durationMillis: Int = 1200,
    tiltDegrees: Float = 20f
): Modifier = composed {
    val themeBaseColor = baseColor ?: MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    val themeHighlightColor =
        highlightColor ?: MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    var widthPx by remember { mutableFloatStateOf(0f) }
    var heightPx by remember { mutableFloatStateOf(0f) }

    val transition = rememberInfiniteTransition(label = "shimmer")
    val anim by transition.animateFloat(
        initialValue = -2f * widthPx,
        targetValue = 2f * widthPx,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    onSizeChanged {
        widthPx = it.width.toFloat()
        heightPx = it.height.toFloat()
    }.drawWithContent {
        // base layer
        drawContent()
        // moving highlight
        val tilt = kotlin.math.tan(Math.toRadians(tiltDegrees.toDouble())).toFloat()
        val start = Offset(anim, 0f)
        val end = Offset(anim + widthPx * 0.5f, heightPx * tilt)

        val brush = Brush.linearGradient(
            colors = listOf(themeBaseColor, themeHighlightColor, themeBaseColor),
            start = start,
            end = end
        )
        drawRect(brush = brush)
    }
}
