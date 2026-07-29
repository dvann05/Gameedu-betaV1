package com.example.data.model

import androidx.compose.ui.graphics.Color

// Available Languages
enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val flagEmoji: String,
    val ttsLocaleCode: String
) {
    INDONESIAN("id", "Bahasa Indonesia", "🇮🇩", "id"),
    ENGLISH("en", "English", "🇺🇸", "en"),
    ARABIC("ar", "العربية", "🇸🇦", "ar"),
    JAPANESE("ja", "日本語", "🇯🇵", "ja"),
    KOREAN("ko", "한국어", "🇰🇷", "ko"),
    CHINESE("zh", "中文", "🇨🇳", "zh"),
    FRENCH("fr", "Français", "🇫🇷", "fr"),
    GERMAN("de", "Deutsch", "🇩🇪", "de"),
    SPANISH("es", "Español", "🇪🇸", "es"),
    PORTUGUESE("pt", "Português", "🇧🇷", "pt"),
    RUSSIAN("ru", "Русский", "🇷🇺", "ru"),
    HINDI("hi", "हिन्दी", "🇮🇳", "hi")
}

// Game Difficulty Levels
enum class GameDifficulty(val label: String, val multiplier: Float) {
    EASY("Easy", 1.0f),
    NORMAL("Normal", 1.5f),
    HARD("Hard", 2.0f),
    EXPERT("Expert", 3.0f)
}

// App Theme Variations
enum class ThemeType(
    val id: String,
    val displayName: String,
    val priceCoins: Int,
    val primaryColor: Color,
    val secondaryColor: Color,
    val backgroundColor: Color,
    val iconEmoji: String
) {
    RAINBOW("rainbow", "Rainbow World", 0, Color(0xFFFF4081), Color(0xFFFFC107), Color(0xFFFFF8E1), "🌈"),
    FOREST("forest", "Magic Forest", 100, Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFA5D6A7), "🌲"),
    OCEAN("ocean", "Deep Ocean", 150, Color(0xFF0288D1), Color(0xFF00BCD4), Color(0xFFE0F7FA), "🌊"),
    SPACE("space", "Cosmic Space", 200, Color(0xFF7B1FA2), Color(0xFFE040FB), Color(0xFFF3E5F5), "🚀"),
    CANDY("candy", "Candy Kingdom", 250, Color(0xFFE91E63), Color(0xFFFF80AB), Color(0xFFFCE4EC), "🍬"),
    JUNGLE("jungle", "Safari Jungle", 300, Color(0xFF388E3C), Color(0xFFFBC02D), Color(0xFFFFF9C4), "🦁"),
    PRINCESS("princess", "Fairy Princess", 350, Color(0xFFD81B60), Color(0xFFFF4081), Color(0xFFF8BBD0), "👑"),
    ROBOT("robot", "Robot City", 400, Color(0xFF1976D2), Color(0xFF00E5FF), Color(0xFFE1F5FE), "🤖"),
    ANIMAL("animal", "Cute Animals", 450, Color(0xFFFF9800), Color(0xFFFFC107), Color(0xFFFFF3E0), "🐶"),
    DINOSAUR("dinosaur", "Dino Land", 500, Color(0xFF558B2F), Color(0xFF8D6E63), Color(0xFFF1F8E9), "🦖"),
    MAGIC("magic", "Magic Castle", 600, Color(0xFF8E24AA), Color(0xFFFFD54F), Color(0xFFEDE7F6), "🪄")
}

// Learning Categories
enum class LearningCategory(
    val id: String,
    val titleKey: String,
    val iconEmoji: String,
    val defaultColor: Color
) {
    LETTERS("letters", "Letters", "🔤", Color(0xFFFF5722)),
    NUMBERS("numbers", "Numbers", "🔢", Color(0xFF3F51B5)),
    COLORS("colors", "Colors", "🎨", Color(0xFFE91E63)),
    SHAPES("shapes", "Shapes", "🔷", Color(0xFF00BCD4)),
    FRUITS("fruits", "Fruits", "🍎", Color(0xFF4CAF50)),
    VEGETABLES("vegetables", "Vegetables", "🥕", Color(0xFFFF9800)),
    ANIMALS("animals", "Animals", "🦁", Color(0xFFFFC107)),
    INSECTS("insects", "Insects", "🐞", Color(0xFF8BC34A)),
    BIRDS("birds", "Birds", "🦜", Color(0xFF009688)),
    SEA("sea", "Sea Creatures", "🐬", Color(0xFF0288D1)),
    PLANETS("planets", "Planets", "🪐", Color(0xFF673AB7)),
    SOLAR("solar", "Solar System", "☀️", Color(0xFFFFC107)),
    COUNTRIES("countries", "Countries", "🗺️", Color(0xFF795548)),
    FLAGS("flags", "Flags", "🚩", Color(0xFFE91E63)),
    PROFESSIONS("professions", "Professions", "👨‍🚒", Color(0xFF607D8B)),
    VEHICLES("vehicles", "Vehicles", "🚗", Color(0xFFFF5722)),
    BODY("body", "Human Body", "🖐️", Color(0xFFFF80AB)),
    FOOD("food", "Food", "🍕", Color(0xFFFF9800)),
    DRINKS("drinks", "Drinks", "🧃", Color(0xFF2196F3))
}

// Mini Game Modes
enum class MiniGameType(
    val id: String,
    val titleKey: String,
    val iconEmoji: String,
    val description: String
) {
    MATH("math", "Math Game", "➕", "Fun arithmetic with counting & numbers"),
    MEMORY("memory", "Memory Cards", "🃏", "Flip cards to find matching pairs"),
    MATCHING("matching", "Matching Game", "🧩", "Match shadows, names & objects"),
    MAZE("maze", "Maze Adventure", "🌀", "Guide character through procedurally generated mazes"),
    WORD_SORT("word_sort", "Word & Letter Sort", "🔠", "Arrange letters to form words"),
    COLORING("coloring", "Coloring & Drawing", "🖍️", "Express creativity with canvas tools"),
    TRACING("tracing", "Tracing Letters & Numbers", "✍️", "Practice writing letters and numbers"),
    BUBBLE_POP("bubble_pop", "Bubble Pop", "🫧", "Pop floating bubbles with letters and shapes"),
    KIDS_MUSIC("kids_music", "Kids Musical Xylophone", "🎹", "Play interactive colorful musical keys"),
    LOGIC_REFLEX("logic_reflex", "Logic & Reflex", "⚡", "Speed tap and pattern memory games")
}

// Item for Learning Category Flashcards
data class FlashcardItem(
    val id: String,
    val nameEn: String,
    val nameId: String,
    val nameAr: String,
    val nameJa: String,
    val emojiOrSymbol: String,
    val detail: String = "",
    val cardColor: Color = Color(0xFFFFD54F)
)

// Avatar Preset Model
data class AvatarPreset(
    val id: String,
    val name: String,
    val priceCoins: Int,
    val emoji: String,
    val isDefaultUnlocked: Boolean = false
)

// Shop item categories
enum class ShopCategory {
    AVATAR, THEME, FRAME, BADGE
}

// Achievement item
data class AchievementItem(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val rewardCoins: Int,
    val rewardDiamonds: Int,
    val isUnlocked: Boolean = false
)
