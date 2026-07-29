package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.ProceduralGenerator
import com.example.audio.AudioEngine
import com.example.audio.SfxType
import com.example.data.local.PlayerProfileEntity
import com.example.data.model.GameDifficulty
import com.example.ui.components.ConfettiCanvas
import com.example.ui.components.MascotHeaderBanner
import com.example.ui.components.VictoryDialog

@Composable
fun MathGameScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onLevelCompleted: (stars: Int, score: Int) -> Unit,
    onBack: () -> Unit
) {
    var level by remember { mutableIntStateOf(1) }
    var question by remember(level) {
        mutableStateOf(ProceduralGenerator.generateMathQuestion(level, GameDifficulty.EASY))
    }
    var triggerConfetti by remember { mutableStateOf(false) }
    var showVictoryDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFFFFF8E1), Color(0xFFE8F5E9))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Math Game • Level $level",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(16.dp))

        ConfettiCanvas(trigger = triggerConfetti, onFinished = { triggerConfetti = false })

        // Item counting box
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hitung & Jawab Soal Ini:",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Item visual representation
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(question.countItems.coerceAtMost(10)) {
                        Text(text = question.itemEmoji, fontSize = 32.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = question.questionText,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1565C0)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Answer choices
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(question.choices.size) { idx ->
                val choice = question.choices[idx]
                Button(
                    onClick = {
                        if (choice == question.correctAnswer) {
                            triggerConfetti = true
                            audioEngine.playSfx(SfxType.CORRECT)
                            audioEngine.speak("Pintar! Luar biasa!", profile.selectedLanguage)
                            showVictoryDialog = true
                        } else {
                            audioEngine.playSfx(SfxType.WRONG)
                            audioEngine.speak("Coba lagi ya", profile.selectedLanguage)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .testTag("math_choice_$choice"),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081))
                ) {
                    Text(
                        text = choice.toString(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }

        if (showVictoryDialog) {
            VictoryDialog(
                starsEarned = 3,
                coinsEarned = 30,
                praiseText = "Pintar Sekali!",
                onNextLevel = {
                    showVictoryDialog = false
                    onLevelCompleted(3, 100)
                    level++
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
