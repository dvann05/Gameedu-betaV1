package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UnlockedItemDao {
    @Query("SELECT * FROM unlocked_items")
    fun getAllUnlockedItemsFlow(): Flow<List<UnlockedItemEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM unlocked_items WHERE itemId = :itemId AND category = :category)")
    suspend fun isItemUnlocked(itemId: String, category: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockItem(item: UnlockedItemEntity)
}
