package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioEngine
import com.example.audio.SfxType
import com.example.data.local.PlayerProfileEntity
import com.example.ui.components.ConfettiCanvas
import com.example.ui.components.MascotHeaderBanner
import com.example.ui.components.VictoryDialog
import kotlin.random.Random

data class FloatingBubble(
    val id: Int,
    val text: String,
    val color: Color,
    val xOffset: Int,
    val yAnim: Animatable<Float, *>
)

@Composable
fun BubblePopScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onLevelCompleted: (stars: Int, score: Int) -> Unit,
    onBack: () -> Unit
) {
    var score by remember { mutableIntStateOf(0) }
    var targetScore by remember { mutableIntStateOf(10) }

    val bubbleItems = remember { listOf("A", "B", "C", "1", "2", "3", "🍎", "⭐", "🎈", "🐶") }
    val bubbleColors = remember {
        listOf(
            Color(0xFFFF4081), Color(0xFF2196F3), Color(0xFF4CAF50),
            Color(0xFFFFC107), Color(0xFF9C27B0), Color(0xFFFF9800)
        )
    }

    val bubbles = remember { mutableStateListOf<FloatingBubble>() }
    var showVictoryDialog by remember { mutableStateOf(false) }
    var triggerConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        repeat(8) { idx ->
            val anim = Animatable(800f)
            val bubble = FloatingBubble(
                id = idx,
                text = bubbleItems.random(),
                color = bubbleColors.random(),
                xOffset = Random.nextInt(20, 280),
                yAnim = anim
            )
            bubbles.add(bubble)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE0F7FA), Color(0xFFFFF8E1), Color(0xFFE8F5E9))
                )
            )
            .padding(16.dp)
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Bubble Pop Game",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(12.dp))

        ConfettiCanvas(trigger = triggerConfetti, onFinished = { triggerConfetti = false })

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🫧 Gelembung Meletus: $score / $targetScore", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
        ) {
            bubbles.forEachIndexed { idx, bubble ->
                LaunchedEffect(bubble.id) {
                    bubble.yAnim.animateTo(
                        targetValue = -100f,
                        animationSpec = tween(
                            durationMillis = Random.nextInt(3000, 6000),
                            easing = LinearEasing
                        )
                    )
                    // Reset to bottom
                    bubble.yAnim.snapTo(800f)
                }

                Box(
                    modifier = Modifier
                        .offset { IntOffset(bubble.xOffset * 3, bubble.yAnim.value.toInt() * 2) }
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(bubble.color.copy(alpha = 0.85f))
                        .clickable {
                            audioEngine.playSfx(SfxType.BUBBLE_POP)
                            audioEngine.speak(bubble.text, profile.selectedLanguage)
                            score++
                            if (score >= targetScore) {
                                triggerConfetti = true
                                audioEngine.playSfx(SfxType.FANFARE)
                                showVictoryDialog = true
                            }
                        }
                        .testTag("bubble_$idx"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = bubble.text, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        if (showVictoryDialog) {
            VictoryDialog(
                starsEarned = 3,
                coinsEarned = 25,
                praiseText = "Hebat Sekali!",
                onNextLevel = {
                    showVictoryDialog = false
                    score = 0
                    targetScore += 5
                    onLevelCompleted(3, 100)
                },
                onHome = {
                    showVictoryDialog = false
                    onLevelCompleted(3, 100)
                    onBack()
                }
            )
        }
    }
}
