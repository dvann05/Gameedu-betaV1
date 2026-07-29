package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioEngine
import com.example.audio.SfxType
import com.example.data.local.PlayerProfileEntity
import com.example.ui.components.ConfettiCanvas
import com.example.ui.components.MascotHeaderBanner
import com.example.ui.components.VictoryDialog

@Composable
fun TracingScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onLevelCompleted: (stars: Int, score: Int) -> Unit,
    onBack: () -> Unit
) {
    val characters = remember { listOf("A", "B", "C", "1", "2", "3") }
    var charIndex by remember { mutableIntStateOf(0) }
    val targetChar = characters[charIndex]

    val userPaths = remember(targetChar) { mutableStateListOf<Path>() }
    var currentPath by remember { mutableStateOf<Path?>(null) }

    var triggerConfetti by remember { mutableStateOf(false) }
    var showVictoryDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE8F5E9), Color(0xFFFFF3E0), Color(0xFFE0F7FA))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Tracing Huruf & Angka",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(12.dp))

        ConfettiCanvas(trigger = triggerConfetti, onFinished = { triggerConfetti = false })

        Text(
            text = "Ikuti Garis Untuk Menulis: $targetChar",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Background character guide
                Text(
                    text = targetChar,
                    fontSize = 200.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(targetChar) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val path = Path().apply { moveTo(offset.x, offset.y) }
                                    currentPath = path
                                    audioEngine.playSfx(SfxType.CLICK)
                                },
                                onDrag = { change, _ ->
                                    currentPath?.lineTo(change.position.x, change.position.y)
                                },
                                onDragEnd = {
                                    currentPath?.let { userPaths.add(it) }
                                    currentPath = null
                                    if (userPaths.size >= 2) {
                                        triggerConfetti = true
                                        audioEngine.playSfx(SfxType.CORRECT)
                                        audioEngine.speak(targetChar, profile.selectedLanguage)
                                        showVictoryDialog = true
                                    }
                                }
                            )
                        }
                ) {
                    userPaths.forEach { p ->
                        drawPath(
                            path = p,
                            color = Color(0xFFFF4081),
                            style = Stroke(width = 28f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                    }
                    currentPath?.let { p ->
                        drawPath(
                            path = p,
                            color = Color(0xFFFF4081),
                            style = Stroke(width = 28f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                userPaths.clear()
                audioEngine.playSfx(SfxType.CLICK)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("reset_trace_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
        ) {
            Text("🔄 Hapus & Tulis Ulang", fontSize = 16.sp, color = Color.White)
        }

        if (showVictoryDialog) {
            VictoryDialog(
                starsEarned = 3,
                coinsEarned = 30,
                praiseText = "Tulisan Sangat Rapi!",
                onNextLevel = {
                    showVictoryDialog = false
                    userPaths.clear()
                    onLevelCompleted(3, 100)
                    charIndex = (charIndex + 1) % characters.size
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
