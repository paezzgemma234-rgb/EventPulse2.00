package com.seminario2026.eventpulse.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Custom glassmorphism background effect with translucent surface and subtle border
 */
fun Modifier.glassmorphic(
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = SurfaceGlass,
    borderColor: Color = SurfaceGlassBorder,
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(backgroundColor)
    .border(borderWidth, borderColor, shape)

/**
 * Neon border glow modifier
 */
fun Modifier.neonBorder(
    glowColor: Color = CyberCyan,
    shape: Shape = RoundedCornerShape(12.dp),
    borderWidth: Dp = 1.5.dp
): Modifier = this
    .border(
        width = borderWidth,
        brush = Brush.horizontalGradient(
            colors = listOf(glowColor, glowColor.copy(alpha = 0.4f), glowColor)
        ),
        shape = shape
    )

/**
 * Pulsing animated border effect for live events or active cards
 */
@Composable
fun Modifier.pulsingGlow(
    colorPrimary: Color = CyberCyan,
    colorSecondary: Color = NeonMagenta,
    shape: Shape = RoundedCornerShape(16.dp)
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseGlow")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    return this.border(
        width = (1.5).dp,
        brush = Brush.linearGradient(
            colors = listOf(
                colorPrimary.copy(alpha = alpha),
                colorSecondary.copy(alpha = alpha)
            )
        ),
        shape = shape
    )
}
