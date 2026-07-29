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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.data.local.UnlockedItemEntity
import com.example.data.model.ThemeType
import com.example.ui.components.MascotHeaderBanner

@Composable
fun ShopScreen(
    profile: PlayerProfileEntity,
    unlockedItems: List<UnlockedItemEntity>,
    audioEngine: AudioEngine,
    onUnlockTheme: (ThemeType) -> Unit,
    onApplyTheme: (ThemeType) -> Unit,
    onBack: () -> Unit
) {
    val unlockedThemeIds = unlockedItems.filter { it.category == "THEME" }.map { it.itemId }.toSet() + "rainbow"

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
            title = "Toko Tema / Theme Shop",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(ThemeType.entries) { theme ->
                val isUnlocked = unlockedThemeIds.contains(theme.id)
                val isCurrent = profile.selectedTheme == theme.id

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(134.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .border(
                            BorderStroke(2.dp, if (isCurrent) Color(0xFF0284C7) else Color(0xFFE2E8F0)),
                            RoundedCornerShape(24.dp)
                        )
                        .padding(12.dp)
                        .testTag("shop_theme_${theme.id}")
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
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF1F5F9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = theme.iconEmoji, fontSize = 24.sp)
                            }

                            if (isCurrent) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFDCFCE7))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "✅ Active",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF15803D)
                                    )
                                }
                            }
                        }

                        Text(
                            text = theme.displayName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )

                        if (isUnlocked) {
                            Button(
                                onClick = {
                                    audioEngine.playSfx(SfxType.CLICK)
                                    onApplyTheme(theme)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp)
                                    .testTag("apply_theme_${theme.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCurrent) Color(0xFF94A3B8) else Color(0xFF0284C7)
                                )
                            ) {
                                Text(
                                    text = if (isCurrent) "Aktif" else "Gunakan",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    if (profile.coins >= theme.priceCoins) {
                                        audioEngine.playSfx(SfxType.COIN_COLLECT)
                                        onUnlockTheme(theme)
                                    } else {
                                        audioEngine.playSfx(SfxType.WRONG)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp)
                                    .testTag("unlock_theme_${theme.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97316))
                            ) {
                                Text(
                                    text = "🪙 ${theme.priceCoins}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

