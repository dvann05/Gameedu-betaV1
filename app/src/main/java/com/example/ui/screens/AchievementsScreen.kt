package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.audio.AudioEngine
import com.example.audio.SfxType
import com.example.data.local.PlayerProfileEntity
import com.example.ui.components.MascotHeaderBanner
import kotlin.random.Random

data class BadgeTask(
    val title: String,
    val description: String,
    val iconEmoji: String,
    val rewardCoins: Int,
    val isClaimed: Boolean
)

@Composable
fun AchievementsScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onClaimReward: (Int) -> Unit,
    onBack: () -> Unit
) {
    var spinResultMsg by remember { mutableStateOf<String?>(null) }

    val badges = listOf(
        BadgeTask("Master Reader", "Selesaikan 10 pelajaran huruf", "📖", 50, profile.level >= 2),
        BadgeTask("Math Wizard", "Selesaikan 10 soal matematika", "🔢", 100, profile.level >= 3),
        BadgeTask("Puzzle Genius", "Selesaikan 5 permainan puzzle", "🧩", 150, profile.level >= 4),
        BadgeTask("Explorer Pro", "Buka 3 tema permainan", "🌍", 200, profile.level >= 5)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFF8E1), Color(0xFFFFF3E0), Color(0xFFF3E5F5))
                )
            )
            .padding(16.dp)
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Prestasi & Lucky Spin",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Lucky Spin Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFF4081)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🎡 Lucky Spin Wheel", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = "Putar Roda untuk Mendapatkan Hadiah Gratis!", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))

                Spacer(modifier = Modifier.height(12.dp))

                if (spinResultMsg != null) {
                    Text(
                        text = spinResultMsg!!,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFEB3B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        val reward = listOf(20, 50, 100, 200, 500).random()
                        spinResultMsg = "🎉 Kamu Dapat 🪙 $reward Coins!"
                        audioEngine.playSfx(SfxType.FANFARE)
                        onClaimReward(reward)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .testTag("lucky_spin_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                ) {
                    Text(text = "🎲 PUTAR RODA", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "🏆 Daftar Achievement:", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(badges) { task ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = task.iconEmoji, fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                Text(text = task.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(text = task.description, fontSize = 12.sp, color = Color.Gray)
                            }
                        }

                        if (task.isClaimed) {
                            Text(text = "✅ Claimed", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF388E3C))
                        } else {
                            Text(text = "🪙 +${task.rewardCoins}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
                        }
                    }
                }
            }
        }
    }
}
