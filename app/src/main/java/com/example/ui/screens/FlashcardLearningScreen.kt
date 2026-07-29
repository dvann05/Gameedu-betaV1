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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioEngine
import com.example.audio.SfxType
import com.example.data.local.PlayerProfileEntity
import com.example.data.model.LearningCategory
import com.example.data.repository.FlashcardData
import com.example.ui.components.ConfettiCanvas
import com.example.ui.components.MascotHeaderBanner

@Composable
fun FlashcardLearningScreen(
    category: LearningCategory,
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onBack: () -> Unit
) {
    val items = remember(category) { FlashcardData.getItemsForCategory(category) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var isQuizMode by remember { mutableStateOf(false) }
    var quizScore by remember { mutableIntStateOf(0) }
    var triggerConfetti by remember { mutableStateOf(false) }

    val currentItem = items.getOrElse(currentIndex) { items.first() }

    val itemName = when (profile.selectedLanguage.lowercase()) {
        "id" -> currentItem.nameId
        "ar" -> currentItem.nameAr
        "ja" -> currentItem.nameJa
        else -> currentItem.nameEn
    }

    LaunchedEffect(currentIndex, isQuizMode) {
        if (!isQuizMode) {
            audioEngine.speak(itemName, profile.selectedLanguage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE0F7FA), Color(0xFFFFF8E1), Color(0xFFFCE4EC))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = category.titleKey,
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(12.dp))

        ConfettiCanvas(trigger = triggerConfetti, onFinished = { triggerConfetti = false })

        if (!isQuizMode) {
            // Flashcard View
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable {
                        audioEngine.playSfx(SfxType.CLICK)
                        audioEngine.speak(itemName, profile.selectedLanguage)
                    }
                    .testTag("flashcard_box"),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentItem.emojiOrSymbol,
                        fontSize = 110.sp,
                        modifier = Modifier.padding(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = itemName,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1F2937)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            audioEngine.speak(itemName, profile.selectedLanguage)
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081)),
                        modifier = Modifier.testTag("pronounce_button")
                    ) {
                        Text("🔊 Dengarkan Suara", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentIndex > 0) {
                            currentIndex--
                            audioEngine.playSfx(SfxType.CARD_FLIP)
                        }
                    },
                    modifier = Modifier.testTag("prev_button"),
                    enabled = currentIndex > 0,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("⬅️ Sebelum")
                }

                Text(
                    text = "${currentIndex + 1} / ${items.size}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(
                    onClick = {
                        if (currentIndex < items.size - 1) {
                            currentIndex++
                            audioEngine.playSfx(SfxType.CARD_FLIP)
                        } else {
                            triggerConfetti = true
                            audioEngine.playSfx(SfxType.FANFARE)
                        }
                    },
                    modifier = Modifier.testTag("next_button"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(if (currentIndex < items.size - 1) "Lanjut ➡️" else "Selesai 🎉")
                }
            }
        }
    }
}
