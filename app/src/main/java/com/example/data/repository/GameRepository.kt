package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.GameProgressEntity
import com.example.data.local.PlayerProfileEntity
import com.example.data.local.UnlockedItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameRepository(private val db: AppDatabase) {

    private val profileDao = db.playerProfileDao()
    private val progressDao = db.gameProgressDao()
    private val unlockedDao = db.unlockedItemDao()

    val profileFlow: Flow<PlayerProfileEntity?> = profileDao.getProfileFlow()
    val progressFlow: Flow<List<GameProgressEntity>> = progressDao.getAllProgressFlow()
    val unlockedFlow: Flow<List<UnlockedItemEntity>> = unlockedDao.getAllUnlockedItemsFlow()

    suspend fun ensureProfileExists() {
        withContext(Dispatchers.IO) {
            val existing = profileDao.getProfileDirect()
            if (existing == null) {
                val defaultProfile = PlayerProfileEntity()
                profileDao.insertOrUpdateProfile(defaultProfile)
                // Default unlocks
                unlockedDao.unlockItem(UnlockedItemEntity("rainbow", "THEME"))
                unlockedDao.unlockItem(UnlockedItemEntity("🐼", "AVATAR"))
            }
        }
    }

    suspend fun updateLanguage(langCode: String) {
        withContext(Dispatchers.IO) {
            val current = profileDao.getProfileDirect() ?: PlayerProfileEntity()
            profileDao.insertOrUpdateProfile(current.copy(selectedLanguage = langCode))
        }
    }

    suspend fun updateAvatar(avatarEmoji: String) {
        withContext(Dispatchers.IO) {
            val current = profileDao.getProfileDirect() ?: PlayerProfileEntity()
            profileDao.insertOrUpdateProfile(current.copy(selectedAvatar = avatarEmoji))
        }
    }

    suspend fun updateTheme(themeId: String) {
        withContext(Dispatchers.IO) {
            val current = profileDao.getProfileDirect() ?: PlayerProfileEntity()
            profileDao.insertOrUpdateProfile(current.copy(selectedTheme = themeId))
        }
    }

    suspend fun updateAudioSettings(
        music: Boolean,
        sfx: Boolean,
        narration: Boolean,
        musicVol: Float,
        sfxVol: Float,
        narrationVol: Float
    ) {
        withContext(Dispatchers.IO) {
            val current = profileDao.getProfileDirect() ?: PlayerProfileEntity()
            profileDao.insertOrUpdateProfile(
                current.copy(
                    musicEnabled = music,
                    sfxEnabled = sfx,
                    narrationEnabled = narration,
                    musicVolume = musicVol,
                    sfxVolume = sfxVol,
                    narrationVolume = narrationVol
                )
            )
        }
    }

    suspend fun addRewards(coinsEarned: Int, diamondsEarned: Int, xpEarned: Int, starsEarned: Int) {
        withContext(Dispatchers.IO) {
            val current = profileDao.getProfileDirect() ?: PlayerProfileEntity()
            val newXp = current.xp + xpEarned
            val newCoins = current.coins + coinsEarned
            val newDiamonds = current.diamonds + diamondsEarned
            val newStars = current.stars + starsEarned
            val newLevel = (newXp / 100) + 1

            profileDao.insertOrUpdateProfile(
                current.copy(
                    xp = newXp,
                    coins = newCoins,
                    diamonds = newDiamonds,
                    stars = newStars,
                    level = newLevel
                )
            )
        }
    }

    suspend fun recordGameCompletion(gameId: String, starsEarned: Int, score: Int) {
        withContext(Dispatchers.IO) {
            val existing = progressDao.getProgressForGame(gameId) ?: GameProgressEntity(gameId = gameId)
            val newLevel = existing.currentLevel + 1
            val newTotalStars = existing.totalStars + starsEarned
            val newHighScore = maxOf(existing.highestScore, score)
            val newCompleted = existing.totalCompleted + 1

            progressDao.saveProgress(
                existing.copy(
                    currentLevel = newLevel,
                    totalStars = newTotalStars,
                    highestScore = newHighScore,
                    totalCompleted = newCompleted
                )
            )

            // Reward completion
            addRewards(
                coinsEarned = 15 * starsEarned,
                diamondsEarned = if (starsEarned == 3) 2 else 0,
                xpEarned = 25 * starsEarned,
                starsEarned = starsEarned
            )
        }
    }

    suspend fun unlockItem(itemId: String, category: String, priceCoins: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val current = profileDao.getProfileDirect() ?: PlayerProfileEntity()
            if (current.coins >= priceCoins) {
                profileDao.insertOrUpdateProfile(current.copy(coins = current.coins - priceCoins))
                unlockedDao.unlockItem(UnlockedItemEntity(itemId = itemId, category = category))
                true
            } else {
                false
            }
        }
    }
}
