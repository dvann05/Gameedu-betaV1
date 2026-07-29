package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class ConfettiParticle(
    val xRatio: Float,
    var yRatio: Float,
    val speed: Float,
    val size: Float,
    val color: Color,
    val rotation: Float
)

@Composable
fun ConfettiCanvas(
    modifier: Modifier = Modifier,
    trigger: Boolean,
    onFinished: () -> Unit = {}
) {
    if (!trigger) return

    val progress = remember { Animatable(0f) }
    val particles = remember {
        val colors = listOf(
            Color(0xFFFF4081), Color(0xFFFFC107), Color(0xFF00E5FF),
            Color(0xFF76FF03), Color(0xFFE040FB), Color(0xFFFF9800)
        )
        List(60) {
            ConfettiParticle(
                xRatio = Random.nextFloat(),
                yRatio = Random.nextFloat() * 0.3f - 0.2f,
                speed = Random.nextFloat() * 0.8f + 0.4f,
                size = Random.nextFloat() * 20f + 12f,
                color = colors.random(),
                rotation = Random.nextFloat() * 360f
            )
        }
    }

    LaunchedEffect(trigger) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000)
        )
        onFinished()
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val t = progress.value

        particles.forEach { p ->
            val currentY = (p.yRatio + t * p.speed) * height
            val currentX = (p.xRatio + sinX(t + p.rotation) * 0.05f) * width
            drawRect(
                color = p.color.copy(alpha = (1f - t).coerceIn(0f, 1f)),
                topLeft = Offset(currentX, currentY),
                size = Size(p.size, p.size)
            )
        }
    }
}

private fun sinX(valIn: Float): Float {
    return kotlin.math.sin(valIn * Math.PI.toFloat())
}
