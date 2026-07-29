package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.AudioEngine
import com.example.audio.SfxType
import com.example.data.local.PlayerProfileEntity
import com.example.ui.components.MascotHeaderBanner

data class MenuTile(
    val id: String,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val backgroundColor: Color,
    val borderColor: Color,
    val textColor: Color
)

@Composable
fun MainMenuScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onNavigateToLearning: () -> Unit,
    onNavigateToMiniGames: () -> Unit,
    onNavigateToShop: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val tiles = listOf(
        MenuTile("learning", "Belajar / Learn", "16 Categories", "📚", Color(0xFFFFF7ED), Color(0xFFFED7AA), Color(0xFFC2410C)),
        MenuTile("minigames", "Main Game / Play", "10 Game Modes", "🎮", Color(0xFFF0F9FF), Color(0xFFBAE6FD), Color(0xFF0369A1)),
        MenuTile("shop", "Toko / Shop", "Unlocks & Themes", "🛍️", Color(0xFFFAF5FF), Color(0xFFE9D5FF), Color(0xFF7E22CE)),
        MenuTile("achievements", "Prestasi / Rewards", "Piala & Badges", "🏆", Color(0xFFFEFCE8), Color(0xFFFEF08A), Color(0xFFA16207)),
        MenuTile("profile", "Profil Pemain", "Avatar & Stats", "👤", Color(0xFFF0FDF4), Color(0xFFBBF7D0), Color(0xFF15803D)),
        MenuTile("settings", "Pengaturan", "Suara & Bahasa", "⚙️", Color(0xFFEEF2FF), Color(0xFFC7D2FE), Color(0xFF4338CA))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF0F9FF),
                        Color(0xFFE0F2FE),
                        Color(0xFFF0F9FF)
                    )
                )
            )
            .padding(12.dp)
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Kids Learning World",
            onProfileClick = onNavigateToProfile
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Vibrant Hero Banner Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(136.dp)
                .padding(horizontal = 4.dp),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            border = BorderStroke(1.dp, Color(0xFFBAE6FD))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF38BDF8),
                                Color(0xFF2563EB)
                            )
                        )
                    )
            ) {
                // Background decorative image
                Image(
                    painter = painterResource(id = R.drawable.img_kids_banner),
                    contentDescription = "Kids Learning World Banner",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(28.dp)),
                    contentScale = ContentScale.Crop,
                    alpha = 0.35f
                )

                // Soft Radial glow circles
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.TopEnd)
                        .background(Color.White.copy(alpha = 0.15f), CircleShape)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "✨ DUKUNG TUMBUH KEMBANG ANAK",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFEF08A)
                        )
                    }

                    Column {
                        Text(
                            text = "Selamat Datang! 👋",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Ayo Belajar & Bermain Bersama!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE0F2FE)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(tiles.size) { idx ->
                val tile = tiles[idx]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(124.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(tile.backgroundColor)
                        .border(
                            BorderStroke(2.dp, tile.borderColor),
                            shape = RoundedCornerShape(26.dp)
                        )
                        .clickable {
                            audioEngine.playSfx(SfxType.CLICK)
                            when (tile.id) {
                                "learning" -> onNavigateToLearning()
                                "minigames" -> onNavigateToMiniGames()
                                "shop" -> onNavigateToShop()
                                "achievements" -> onNavigateToAchievements()
                                "profile" -> onNavigateToProfile()
                                "settings" -> onNavigateToSettings()
                            }
                        }
                        .testTag("menu_tile_${tile.id}")
                        .padding(14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(1.dp, tile.borderColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = tile.emoji, fontSize = 26.sp)
                            }

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(tile.borderColor.copy(alpha = 0.4f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "➔", fontSize = 12.sp, color = tile.textColor, fontWeight = FontWeight.Bold)
                            }
                        }

                        Column {
                            Text(
                                text = tile.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = tile.textColor
                            )
                            Text(
                                text = tile.subtitle,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = tile.textColor.copy(alpha = 0.75f)
                            )
                        }
                    }
                }
            }
        }
    }
}

