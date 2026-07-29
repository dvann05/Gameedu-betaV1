package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameProgressDao {
    @Query("SELECT * FROM game_progress")
    fun getAllProgressFlow(): Flow<List<GameProgressEntity>>

    @Query("SELECT * FROM game_progress WHERE gameId = :gameId LIMIT 1")
    suspend fun getProgressForGame(gameId: String): GameProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: GameProgressEntity)
}
