package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.ProceduralGenerator
import com.example.audio.AudioEngine
import com.example.audio.SfxType
import com.example.data.local.PlayerProfileEntity
import com.example.ui.components.ConfettiCanvas
import com.example.ui.components.MascotHeaderBanner
import com.example.ui.components.VictoryDialog

@Composable
fun MazeGameScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onLevelCompleted: (stars: Int, score: Int) -> Unit,
    onBack: () -> Unit
) {
    var level by remember { mutableIntStateOf(1) }
    var mazeData by remember(level) { mutableStateOf(ProceduralGenerator.generateMaze(level)) }

    var playerRow by remember(level) { mutableIntStateOf(0) }
    var playerCol by remember(level) { mutableIntStateOf(0) }

    var showVictoryDialog by remember { mutableStateOf(false) }
    var triggerConfetti by remember { mutableStateOf(false) }

    fun tryMove(dRow: Int, dCol: Int) {
        if (showVictoryDialog) return
        val currentCell = mazeData.grid[playerRow][playerCol]

        var canMove = false
        if (dRow == -1 && !currentCell.topWall) canMove = true
        if (dRow == 1 && !currentCell.bottomWall) canMove = true
        if (dCol == -1 && !currentCell.leftWall) canMove = true
        if (dCol == 1 && !currentCell.rightWall) canMove = true

        if (canMove) {
            playerRow += dRow
            playerCol += dCol
            audioEngine.playSfx(SfxType.CLICK)

            if (playerRow == mazeData.goalRow && playerCol == mazeData.goalCol) {
                triggerConfetti = true
                audioEngine.playSfx(SfxType.FANFARE)
                audioEngine.speak("Hebat! Berhasil keluar dari labirin!", profile.selectedLanguage)
                showVictoryDialog = true
            }
        } else {
            audioEngine.playSfx(SfxType.WRONG)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE8F5E9), Color(0xFFFFF8E1), Color(0xFFE0F7FA))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Maze Adventure • Level $level",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(12.dp))

        ConfettiCanvas(trigger = triggerConfetti, onFinished = { triggerConfetti = false })

        // Maze Canvas Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cellWidth = size.width / mazeData.cols
                    val cellHeight = size.height / mazeData.rows
                    val wallStroke = 6f

                    // Draw Walls
                    for (r in 0 until mazeData.rows) {
                        for (c in 0 until mazeData.cols) {
                            val cell = mazeData.grid[r][c]
                            val x1 = c * cellWidth
                            val y1 = r * cellHeight
                            val x2 = (c + 1) * cellWidth
                            val y2 = (r + 1) * cellHeight

                            val wallColor = Color(0xFF1B5E20)

                            if (cell.topWall) drawLine(wallColor, Offset(x1, y1), Offset(x2, y1), wallStroke)
                            if (cell.bottomWall) drawLine(wallColor, Offset(x1, y2), Offset(x2, y2), wallStroke)
                            if (cell.leftWall) drawLine(wallColor, Offset(x1, y1), Offset(x1, y2), wallStroke)
                            if (cell.rightWall) drawLine(wallColor, Offset(x2, y1), Offset(x2, y2), wallStroke)
                        }
                    }

                    // Draw Goal ⭐
                    val goalX = mazeData.goalCol * cellWidth + cellWidth / 2
                    val goalY = mazeData.goalRow * cellHeight + cellHeight / 2
                    drawCircle(Color(0xFFFFC107), radius = cellWidth * 0.35f, center = Offset(goalX, goalY))

                    // Draw Player 🐼
                    val playerX = playerCol * cellWidth + cellWidth / 2
                    val playerY = playerRow * cellHeight + cellHeight / 2
                    drawCircle(Color(0xFFFF4081), radius = cellWidth * 0.35f, center = Offset(playerX, playerY))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // D-Pad Touch Navigation Controls
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { tryMove(-1, 0) },
                modifier = Modifier
                    .size(60.dp)
                    .testTag("btn_up"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("⬆️", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Button(
                    onClick = { tryMove(0, -1) },
                    modifier = Modifier
                        .size(60.dp)
                        .testTag("btn_left"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text("⬅️", fontSize = 20.sp)
                }

                Button(
                    onClick = { tryMove(0, 1) },
                    modifier = Modifier
                        .size(60.dp)
                        .testTag("btn_right"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text("➡️", fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { tryMove(1, 0) },
                modifier = Modifier
                    .size(60.dp)
                    .testTag("btn_down"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("⬇️", fontSize = 20.sp)
            }
        }

        if (showVictoryDialog) {
            VictoryDialog(
                starsEarned = 3,
                coinsEarned = 40,
                praiseText = "Petualang Labirin!",
                onNextLevel = {
                    showVictoryDialog = false
                    onLevelCompleted(3, 150)
                    level++
                },
                onHome = {
                    showVictoryDialog = false
                    onLevelCompleted(3, 150)
                    onBack()
                }
            )
        }
    }
}
