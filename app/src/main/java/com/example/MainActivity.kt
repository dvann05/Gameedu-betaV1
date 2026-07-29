package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.audio.AudioEngine
import com.example.data.local.AppDatabase
import com.example.data.local.PlayerProfileEntity
import com.example.data.model.AppLanguage
import com.example.data.model.LearningCategory
import com.example.data.model.MiniGameType
import com.example.data.model.ThemeType
import com.example.data.repository.GameRepository
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.BubblePopScreen
import com.example.ui.screens.ColoringDrawingScreen
import com.example.ui.screens.FlashcardLearningScreen
import com.example.ui.screens.GameCategoryScreen
import com.example.ui.screens.KidsMusicScreen
import com.example.ui.screens.LanguageSelectionScreen
import com.example.ui.screens.LogicReflexScreen
import com.example.ui.screens.MainMenuScreen
import com.example.ui.screens.MathGameScreen
import com.example.ui.screens.MazeGameScreen
import com.example.ui.screens.MemoryGameScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TracingScreen
import com.example.ui.screens.WordSortingScreen
import com.example.ui.theme.KidsLearningWorldTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var audioEngine: AudioEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        audioEngine = AudioEngine(this)

        setContent {
            val context = LocalContext.current
            val db = remember { AppDatabase.getInstance(context) }
            val repository = remember { GameRepository(db) }
            val scope = rememberCoroutineScope()

            val profileState by repository.profileFlow.collectAsState(initial = null)
            val unlockedItems by repository.unlockedFlow.collectAsState(initial = emptyList())

            LaunchedEffect(Unit) {
                repository.ensureProfileExists()
            }

            val profile = profileState ?: PlayerProfileEntity()
            val activeTheme = ThemeType.entries.find { it.id == profile.selectedTheme } ?: ThemeType.RAINBOW

            KidsLearningWorldTheme(selectedTheme = activeTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "splash",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("splash") {
                            SplashScreen(
                                audioEngine = audioEngine,
                                onSplashFinished = {
                                    navController.navigate("language") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("language") {
                            LanguageSelectionScreen(
                                currentLanguageCode = profile.selectedLanguage,
                                audioEngine = audioEngine,
                                onLanguageSelected = { lang ->
                                    scope.launch { repository.updateLanguage(lang.code) }
                                },
                                onContinue = {
                                    navController.navigate("main_menu") {
                                        popUpTo("language") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("main_menu") {
                            MainMenuScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onNavigateToLearning = { navController.navigate("category/0") },
                                onNavigateToMiniGames = { navController.navigate("category/1") },
                                onNavigateToShop = { navController.navigate("shop") },
                                onNavigateToAchievements = { navController.navigate("achievements") },
                                onNavigateToProfile = { navController.navigate("profile") },
                                onNavigateToSettings = { navController.navigate("settings") }
                            )
                        }

                        composable(
                            route = "category/{tabIndex}",
                            arguments = listOf(navArgument("tabIndex") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val tabIndex = backStackEntry.arguments?.getInt("tabIndex") ?: 0
                            GameCategoryScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                initialTab = tabIndex,
                                onSelectLearningCategory = { cat ->
                                    navController.navigate("flashcard/${cat.id}")
                                },
                                onSelectMiniGame = { game ->
                                    val route = when (game) {
                                        MiniGameType.MATH -> "math_game"
                                        MiniGameType.MEMORY -> "memory_game"
                                        MiniGameType.MAZE -> "maze_game"
                                        MiniGameType.WORD_SORT -> "word_sort_game"
                                        MiniGameType.COLORING -> "coloring_game"
                                        MiniGameType.TRACING -> "tracing_game"
                                        MiniGameType.BUBBLE_POP -> "bubble_pop_game"
                                        MiniGameType.KIDS_MUSIC -> "kids_music_game"
                                        MiniGameType.LOGIC_REFLEX -> "logic_reflex_game"
                                        else -> "math_game"
                                    }
                                    navController.navigate(route)
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = "flashcard/{catId}",
                            arguments = listOf(navArgument("catId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val catId = backStackEntry.arguments?.getString("catId") ?: "letters"
                            val category = LearningCategory.entries.find { it.id == catId } ?: LearningCategory.LETTERS
                            FlashcardLearningScreen(
                                category = category,
                                profile = profile,
                                audioEngine = audioEngine,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("math_game") {
                            MathGameScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onLevelCompleted = { stars, score ->
                                    scope.launch { repository.recordGameCompletion("math", stars, score) }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("memory_game") {
                            MemoryGameScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onLevelCompleted = { stars, score ->
                                    scope.launch { repository.recordGameCompletion("memory", stars, score) }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("maze_game") {
                            MazeGameScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onLevelCompleted = { stars, score ->
                                    scope.launch { repository.recordGameCompletion("maze", stars, score) }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("word_sort_game") {
                            WordSortingScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onLevelCompleted = { stars, score ->
                                    scope.launch { repository.recordGameCompletion("word_sort", stars, score) }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("coloring_game") {
                            ColoringDrawingScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("tracing_game") {
                            TracingScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onLevelCompleted = { stars, score ->
                                    scope.launch { repository.recordGameCompletion("tracing", stars, score) }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("bubble_pop_game") {
                            BubblePopScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onLevelCompleted = { stars, score ->
                                    scope.launch { repository.recordGameCompletion("bubble_pop", stars, score) }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("kids_music_game") {
                            KidsMusicScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("logic_reflex_game") {
                            LogicReflexScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onLevelCompleted = { stars, score ->
                                    scope.launch { repository.recordGameCompletion("logic_reflex", stars, score) }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("shop") {
                            ShopScreen(
                                profile = profile,
                                unlockedItems = unlockedItems,
                                audioEngine = audioEngine,
                                onUnlockTheme = { theme ->
                                    scope.launch { repository.unlockItem(theme.id, "THEME", theme.priceCoins) }
                                },
                                onApplyTheme = { theme ->
                                    scope.launch { repository.updateTheme(theme.id) }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("achievements") {
                            AchievementsScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onClaimReward = { coins ->
                                    scope.launch { repository.addRewards(coins, 1, 50, 5) }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("profile") {
                            ProfileScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onAvatarSelected = { avatar ->
                                    scope.launch { repository.updateAvatar(avatar) }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("settings") {
                            SettingsScreen(
                                profile = profile,
                                audioEngine = audioEngine,
                                onSaveSettings = { music, sfx, narration, mVol, sVol, nVol ->
                                    scope.launch {
                                        repository.updateAudioSettings(music, sfx, narration, mVol, sVol, nVol)
                                    }
                                },
                                onChangeLanguageClick = { navController.navigate("language") },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("about") {
                            AboutScreen(
                                profile = profile,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::audioEngine.isInitialized) {
            audioEngine.release()
        }
    }
}
