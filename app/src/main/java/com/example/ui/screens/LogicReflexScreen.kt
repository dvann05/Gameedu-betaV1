package com.example.ui.screens

import androidx.compose.foundation.background
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
import com.example.ui.components.ConfettiCanvas
import com.example.ui.components.MascotHeaderBanner
import com.example.ui.components.VictoryDialog

@Composable
fun LogicReflexScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onLevelCompleted: (stars: Int, score: Int) -> Unit,
    onBack: () -> Unit
) {
    var level by remember { mutableIntStateOf(1) }
    var patternQuestion by remember(level) { mutableStateOf(ProceduralGenerator.generatePatternQuestion(level)) }

    var triggerConfetti by remember { mutableStateOf(false) }
    var showVictoryDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE1F5FE), Color(0xFFFFF8E1), Color(0xFFF3E5F5))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Logika & Refleks • Level $level",
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
                Text(text = "Lengkapi Pola Urutan Ini:", fontSize = 16.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    patternQuestion.sequenceEmojis.forEach { emoji ->
                        Text(text = emoji, fontSize = 36.sp)
                    }
                    Text(text = "❓", fontSize = 40.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(patternQuestion.choices.size) { idx ->
                val choice = patternQuestion.choices[idx]
                Button(
                    onClick = {
                        if (choice == patternQuestion.correctMissingEmoji) {
                            triggerConfetti = true
                            audioEngine.playSfx(SfxType.CORRECT)
                            audioEngine.speak("Hebat! Pola benar!", profile.selectedLanguage)
                            showVictoryDialog = true
                        } else {
                            audioEngine.playSfx(SfxType.WRONG)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .testTag("logic_choice_$idx"),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081))
                ) {
                    Text(text = choice, fontSize = 40.sp)
                }
            }
        }

        if (showVictoryDialog) {
            VictoryDialog(
                starsEarned = 3,
                coinsEarned = 30,
                praiseText = "Logika Sangat Tajam!",
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
