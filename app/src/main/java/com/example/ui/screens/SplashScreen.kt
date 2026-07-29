package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.AudioEngine
import com.example.audio.SfxType

@Composable
fun SplashScreen(
    audioEngine: AudioEngine,
    onSplashFinished: () -> Unit
) {
    val scale = remember { Animatable(0.5f) }
    val loadingProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        audioEngine.playSfx(SfxType.FANFARE)
        scale.animateTo(
            targetValue = 1.05f,
            animationSpec = tween(durationMillis = 800)
        )
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 300)
        )
        loadingProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200)
        )
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF81D4FA),
                        Color(0xFFFF80AB),
                        Color(0xFFFFF59D)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_app_icon),
                contentDescription = "Kids Learning World Logo",
                modifier = Modifier
                    .size(160.dp)
                    .scale(scale.value)
                    .clip(RoundedCornerShape(32.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Kids Learning World",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Text(
                text = "Learn • Play • Explore",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(36.dp))

            LinearProgressIndicator(
                progress = { loadingProgress.value },
                modifier = Modifier
                    .size(width = 200.dp, height = 12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = Color(0xFFFF4081),
                trackColor = Color.White.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Loading World...",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
