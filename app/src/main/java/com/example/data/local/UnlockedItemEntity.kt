package com.example.data.local

import androidx.room.Entity

@Entity(tableName = "unlocked_items", primaryKeys = ["itemId", "category"])
data class UnlockedItemEntity(
    val itemId: String,
    val category: String, // AVATAR, THEME, FRAME, BADGE
    val unlockedAt: Long = System.currentTimeMillis()
)
