package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioEngine
import com.example.audio.SfxType
import com.example.data.local.PlayerProfileEntity
import com.example.ui.components.ConfettiCanvas
import com.example.ui.components.MascotHeaderBanner
import com.example.ui.components.VictoryDialog
import kotlinx.coroutines.delay

data class MemoryCard(
    val id: Int,
    val symbol: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)

@Composable
fun MemoryGameScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onLevelCompleted: (stars: Int, score: Int) -> Unit,
    onBack: () -> Unit
) {
    var level by remember { mutableIntStateOf(1) }
    val symbols = remember { listOf("🐶", "🐱", "🦁", "🐼", "🐸", "🐻") }

    val cardList = remember(level) {
        val count = (2 + (level % 3) * 2).coerceAtMost(6)
        val selectedSymbols = symbols.take(count)
        val doubleList = (selectedSymbols + selectedSymbols).shuffled()
        doubleList.mapIndexed { idx, sym -> MemoryCard(idx, sym) }.toMutableList()
    }

    val cards = remember(level) { mutableStateListOf<MemoryCard>().apply { addAll(cardList) } }

    var selectedFirstIndex by remember { mutableStateOf<Int?>(null) }
    var selectedSecondIndex by remember { mutableStateOf<Int?>(null) }
    var isBusy by remember { mutableStateOf(false) }
    var showVictoryDialog by remember { mutableStateOf(false) }
    var triggerConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(selectedFirstIndex, selectedSecondIndex) {
        val idx1 = selectedFirstIndex
        val idx2 = selectedSecondIndex
        if (idx1 != null && idx2 != null) {
            isBusy = true
            delay(600)
            if (cards[idx1].symbol == cards[idx2].symbol) {
                cards[idx1] = cards[idx1].copy(isMatched = true)
                cards[idx2] = cards[idx2].copy(isMatched = true)
                audioEngine.playSfx(SfxType.CORRECT)
            } else {
                cards[idx1] = cards[idx1].copy(isFlipped = false)
                cards[idx2] = cards[idx2].copy(isFlipped = false)
                audioEngine.playSfx(SfxType.WRONG)
            }
            selectedFirstIndex = null
            selectedSecondIndex = null
            isBusy = false

            if (cards.all { it.isMatched }) {
                triggerConfetti = true
                audioEngine.playSfx(SfxType.FANFARE)
                showVictoryDialog = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE8EAF6), Color(0xFFFFF3E0), Color(0xFFE0F2F1))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Memory Cards • Level $level",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(16.dp))

        ConfettiCanvas(trigger = triggerConfetti, onFinished = { triggerConfetti = false })

        Text(
            text = "Temukan Pasangan Kartu Yang Sama!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(cards) { idx, card ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable(enabled = !card.isFlipped && !card.isMatched && !isBusy) {
                            cards[idx] = card.copy(isFlipped = true)
                            audioEngine.playSfx(SfxType.CARD_FLIP)
                            if (selectedFirstIndex == null) {
                                selectedFirstIndex = idx
                            } else if (selectedSecondIndex == null) {
                                selectedSecondIndex = idx
                            }
                        }
                        .testTag("memory_card_$idx"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (card.isFlipped || card.isMatched) Color.White else Color(0xFFFF4081)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (card.isFlipped || card.isMatched) {
                            Text(text = card.symbol, fontSize = 44.sp)
                        } else {
                            Text(text = "❓", fontSize = 32.sp)
                        }
                    }
                }
            }
        }

        if (showVictoryDialog) {
            VictoryDialog(
                starsEarned = 3,
                coinsEarned = 35,
                praiseText = "Daya Ingat Hebat!",
                onNextLevel = {
                    showVictoryDialog = false
                    onLevelCompleted(3, 120)
                    level++
                },
                onHome = {
                    showVictoryDialog = false
                    onLevelCompleted(3, 120)
                    onBack()
                }
            )
        }
    }
}
