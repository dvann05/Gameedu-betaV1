package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.ui.components.ConfettiCanvas
import com.example.ui.components.MascotHeaderBanner
import com.example.ui.components.VictoryDialog

@Composable
fun WordSortingScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onLevelCompleted: (stars: Int, score: Int) -> Unit,
    onBack: () -> Unit
) {
    var level by remember { mutableIntStateOf(1) }
    var wordData by remember(level) { mutableStateOf(ProceduralGenerator.generateWordSort(level)) }

    val assembled = remember(level) { mutableStateListOf<Char>() }
    val remaining = remember(level) { mutableStateListOf<Char>().apply { addAll(wordData.scrambledLetters) } }

    var triggerConfetti by remember { mutableStateOf(false) }
    var showVictoryDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFF3E0), Color(0xFFFCE4EC), Color(0xFFE0F7FA))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Susun Huruf • Level $level",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(16.dp))

        ConfettiCanvas(trigger = triggerConfetti, onFinished = { triggerConfetti = false })

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
                Text(text = wordData.iconEmoji, fontSize = 80.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = wordData.hintTranslation,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Assembled slots
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(wordData.targetWord.length) { idx ->
                val char = assembled.getOrNull(idx)
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(
                            if (char != null) Color(0xFF4CAF50) else Color.White,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            if (char != null) {
                                assembled.removeAt(idx)
                                remaining.add(char)
                                audioEngine.playSfx(SfxType.CLICK)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char?.toString() ?: "_",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (char != null) Color.White else Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Tekan huruf untuk menyusun:",
            fontSize = 15.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Available letter choices
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            remaining.forEachIndexed { idx, char ->
                Button(
                    onClick = {
                        remaining.removeAt(idx)
                        assembled.add(char)
                        audioEngine.playSfx(SfxType.CLICK)

                        val currentStr = assembled.joinToString("")
                        if (currentStr == wordData.targetWord) {
                            triggerConfetti = true
                            audioEngine.playSfx(SfxType.CORRECT)
                            audioEngine.speak(wordData.targetWord, profile.selectedLanguage)
                            showVictoryDialog = true
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081)),
                    modifier = Modifier
                        .size(56.dp)
                        .testTag("sort_letter_$char")
                ) {
                    Text(text = char.toString(), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }
        }

        if (showVictoryDialog) {
            VictoryDialog(
                starsEarned = 3,
                coinsEarned = 30,
                praiseText = "Kata Tersusun Sempurna!",
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
