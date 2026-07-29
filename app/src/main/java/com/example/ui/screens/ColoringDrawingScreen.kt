package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioEngine
import com.example.audio.SfxType
import com.example.data.local.PlayerProfileEntity
import com.example.ui.components.MascotHeaderBanner

data class DrawnLine(
    val path: Path,
    val color: Color,
    val strokeWidth: Float
)

val colorPalette = listOf(
    Color(0xFFFF4081), Color(0xFF2196F3), Color(0xFF4CAF50),
    Color(0xFFFFC107), Color(0xFF9C27B0), Color(0xFFFF9800),
    Color(0xFF795548), Color(0xFF1F2937)
)

@Composable
fun ColoringDrawingScreen(
    profile: PlayerProfileEntity,
    audioEngine: AudioEngine,
    onBack: () -> Unit
) {
    val lines = remember { mutableStateListOf<DrawnLine>() }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    var selectedColor by remember { mutableStateOf(colorPalette.first()) }
    var strokeWidth by remember { mutableStateOf(16f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE0F7FA), Color(0xFFFFF8E1), Color(0xFFFCE4EC))
                )
            )
            .padding(16.dp)
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Mewarnai & Menggambar",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Drawing Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(selectedColor, strokeWidth) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val path = Path().apply { moveTo(offset.x, offset.y) }
                                    currentPath = path
                                    audioEngine.playSfx(SfxType.CLICK)
                                },
                                onDrag = { change, _ ->
                                    currentPath?.lineTo(change.position.x, change.position.y)
                                },
                                onDragEnd = {
                                    currentPath?.let {
                                        lines.add(DrawnLine(it, selectedColor, strokeWidth))
                                    }
                                    currentPath = null
                                }
                            )
                        }
                ) {
                    lines.forEach { line ->
                        drawPath(
                            path = line.path,
                            color = line.color,
                            style = Stroke(
                                width = line.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                    currentPath?.let { path ->
                        drawPath(
                            path = path,
                            color = selectedColor,
                            style = Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Palette & Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(colorPalette) { color ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .clickable {
                                selectedColor = color
                                audioEngine.playSfx(SfxType.CLICK)
                            }
                            .testTag("color_picker_${color.hashCode()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    lines.clear()
                    audioEngine.playSfx(SfxType.CLICK)
                },
                modifier = Modifier.testTag("clear_canvas_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("🗑️ Hapus")
            }
        }
    }
}
