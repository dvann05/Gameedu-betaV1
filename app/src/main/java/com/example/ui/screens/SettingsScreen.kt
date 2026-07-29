package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.model.AppLanguage
import com.example.ui.components.MascotHeaderBanner

@Composable
fun SettingsScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onSaveSettings: (music: Boolean, sfx: Boolean, narration: Boolean, mVol: Float, sVol: Float, nVol: Float) -> Unit,
    onChangeLanguageClick: () -> Unit,
    onBack: () -> Unit
) {
    var musicEnabled by remember { mutableStateOf(profile.musicEnabled) }
    var sfxEnabled by remember { mutableStateOf(profile.sfxEnabled) }
    var narrationEnabled by remember { mutableStateOf(profile.narrationEnabled) }

    var musicVolume by remember { mutableFloatStateOf(profile.musicVolume) }
    var sfxVolume by remember { mutableFloatStateOf(profile.sfxVolume) }

    val currentLang = AppLanguage.entries.find { it.code == profile.selectedLanguage } ?: AppLanguage.INDONESIAN

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE8F5E9), Color(0xFFFFF8E1), Color(0xFFE0F7FA))
                )
            )
            .padding(16.dp)
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Pengaturan / Settings",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "🎵 Pengaturan Suara:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Musik Latar / Music", fontSize = 15.sp)
                    Switch(
                        checked = musicEnabled,
                        onCheckedChange = {
                            musicEnabled = it
                            audioEngine.isMusicEnabled = it
                            onSaveSettings(musicEnabled, sfxEnabled, narrationEnabled, musicVolume, sfxVolume, 1.0f)
                        },
                        modifier = Modifier.testTag("switch_music")
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Efek Suara / SFX", fontSize = 15.sp)
                    Switch(
                        checked = sfxEnabled,
                        onCheckedChange = {
                            sfxEnabled = it
                            audioEngine.isSfxEnabled = it
                            onSaveSettings(musicEnabled, sfxEnabled, narrationEnabled, musicVolume, sfxVolume, 1.0f)
                        },
                        modifier = Modifier.testTag("switch_sfx")
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Suara Pengucapan / Narration", fontSize = 15.sp)
                    Switch(
                        checked = narrationEnabled,
                        onCheckedChange = {
                            narrationEnabled = it
                            audioEngine.isNarrationEnabled = it
                            onSaveSettings(musicEnabled, sfxEnabled, narrationEnabled, musicVolume, sfxVolume, 1.0f)
                        },
                        modifier = Modifier.testTag("switch_narration")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        audioEngine.playSfx(SfxType.FANFARE)
                        audioEngine.speak("Kids Learning World!", profile.selectedLanguage)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("test_audio_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text("🔊 Test Suara / Audio Test", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "🌐 Bahasa Saat Ini:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${currentLang.flagEmoji} ${currentLang.nativeName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedButton(
                        onClick = onChangeLanguageClick,
                        modifier = Modifier.testTag("change_language_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Ubah / Change")
                    }
                }
            }
        }
    }
}
