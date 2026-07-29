package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.components.MascotHeaderBanner

data class XylophoneKey(
    val noteName: String,
    val freq: Float,
    val color: Color
)

@Composable
fun KidsMusicScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onBack: () -> Unit
) {
    val keys = listOf(
        XylophoneKey("Do (C)", 261.63f, Color(0xFFEF5350)),
        XylophoneKey("Re (D)", 293.66f, Color(0xFFFF7043)),
        XylophoneKey("Mi (E)", 329.63f, Color(0xFFFFCA28)),
        XylophoneKey("Fa (F)", 349.23f, Color(0xFF66BB6A)),
        XylophoneKey("Sol (G)", 392.00f, Color(0xFF26A69A)),
        XylophoneKey("La (A)", 440.00f, Color(0xFF42A5F5)),
        XylophoneKey("Si (B)", 493.88f, Color(0xFFAB47BC)),
        XylophoneKey("Do (C2)", 523.25f, Color(0xFFEC407A))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFF8E1), Color(0xFFF3E5F5), Color(0xFFE0F7FA))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Musik & Xylophone Anak",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🎵 Tekan Tombol Warna Untuk Memainkan Nada!", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            keys.forEachIndexed { idx, k ->
                val keyHeightFactor = 1.0f - (idx * 0.05f)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(keyHeightFactor)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 8.dp, bottomEnd = 8.dp))
                        .background(k.color)
                        .clickable {
                            audioEngine.playSfx(SfxType.XYLOPHONE_NOTE, k.freq)
                        }
                        .testTag("key_${k.noteName}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = k.noteName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
