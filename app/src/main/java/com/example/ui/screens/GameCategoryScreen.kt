package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.MiniGameType
import com.example.ui.components.MascotHeaderBanner

// Theme palette map helper for category cards
private data class CardThemeStyle(
    val bg: Color,
    val border: Color,
    val text: Color
)

private val vibrantCardStyles = listOf(
    CardThemeStyle(Color(0xFFFFF7ED), Color(0xFFFED7AA), Color(0xFFC2410C)), // Orange
    CardThemeStyle(Color(0xFFF0F9FF), Color(0xFFBAE6FD), Color(0xFF0369A1)), // Sky
    CardThemeStyle(Color(0xFFFAF5FF), Color(0xFFE9D5FF), Color(0xFF7E22CE)), // Purple
    CardThemeStyle(Color(0xFFF0FDF4), Color(0xFFBBF7D0), Color(0xFF15803D)), // Emerald
    CardThemeStyle(Color(0xFFFFF1F2), Color(0xFFFECDD3), Color(0xFFBE123C)), // Rose
    CardThemeStyle(Color(0xFFFEFCE8), Color(0xFFFEF08A), Color(0xFFA16207))  // Yellow
)

@Composable
fun GameCategoryScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    initialTab: Int = 0, // 0: Learning, 1: MiniGames
    onSelectLearningCategory: (LearningCategory) -> Unit,
    onSelectMiniGame: (MiniGameType) -> Unit,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }

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
            title = if (selectedTab == 0) "Belajar / Learning" else "Permainan / Mini Games",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Custom Vibrant Pill Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selectedTab == 0) Color(0xFF0284C7) else Color.Transparent)
                    .clickable {
                        selectedTab = 0
                        audioEngine.playSfx(SfxType.CLICK)
                    }
                    .padding(vertical = 10.dp)
                    .testTag("tab_learning"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📚 Belajar (16)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedTab == 0) Color.White else Color(0xFF64748B)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selectedTab == 1) Color(0xFF0284C7) else Color.Transparent)
                    .clickable {
                        selectedTab = 1
                        audioEngine.playSfx(SfxType.CLICK)
                    }
                    .padding(vertical = 10.dp)
                    .testTag("tab_games"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🎮 Mini Games (10)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedTab == 1) Color.White else Color(0xFF64748B)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedTab == 0) {
            // Learning Categories Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(LearningCategory.entries.size) { idx ->
                    val cat = LearningCategory.entries[idx]
                    val style = vibrantCardStyles[idx % vibrantCardStyles.size]

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(104.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(style.bg)
                            .border(BorderStroke(2.dp, style.border), RoundedCornerShape(24.dp))
                            .clickable {
                                audioEngine.playSfx(SfxType.CLICK)
                                audioEngine.speak(cat.titleKey, profile.selectedLanguage)
                                onSelectLearningCategory(cat)
                            }
                            .testTag("cat_${cat.id}")
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(1.dp, style.border, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = cat.iconEmoji, fontSize = 26.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = cat.titleKey,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = style.text
                            )
                        }
                    }
                }
            }
        } else {
            // Mini Games Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(MiniGameType.entries.size) { idx ->
                    val game = MiniGameType.entries[idx]
                    val style = vibrantCardStyles[idx % vibrantCardStyles.size]

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(116.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(style.bg)
                            .border(BorderStroke(2.dp, style.border), RoundedCornerShape(24.dp))
                            .clickable {
                                audioEngine.playSfx(SfxType.CLICK)
                                audioEngine.speak(game.titleKey, profile.selectedLanguage)
                                onSelectMiniGame(game)
                            }
                            .testTag("game_${game.id}")
                            .padding(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .border(1.dp, style.border, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = game.iconEmoji, fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = game.titleKey,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = style.text
                                )
                            }
                            Text(
                                text = game.description,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = style.text.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

