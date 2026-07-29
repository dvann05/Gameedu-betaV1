package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Junior Explorer",
    val selectedLanguage: String = "id",
    val selectedAvatar: String = "🐼",
    val selectedTheme: String = "rainbow",
    val xp: Int = 0,
    val coins: Int = 100,
    val diamonds: Int = 10,
    val stars: Int = 0,
    val level: Int = 1,
    val musicEnabled: Boolean = true,
    val sfxEnabled: Boolean = true,
    val narrationEnabled: Boolean = true,
    val musicVolume: Float = 0.8f,
    val sfxVolume: Float = 1.0f,
    val narrationVolume: Float = 1.0f,
    val difficulty: String = "EASY"
)
