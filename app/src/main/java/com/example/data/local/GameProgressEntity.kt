package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_progress")
data class GameProgressEntity(
    @PrimaryKey val gameId: String,
    val currentLevel: Int = 1,
    val totalStars: Int = 0,
    val highestScore: Int = 0,
    val totalCompleted: Int = 0
)
