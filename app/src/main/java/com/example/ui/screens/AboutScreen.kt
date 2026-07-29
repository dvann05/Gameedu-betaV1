package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.PlayerProfileEntity
import com.example.ui.components.MascotHeaderBanner

@Composable
fun AboutScreen(
    profile: PlayerProfileEntity,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE0F7FA), Color(0xFFFFF8E1), Color(0xFFFCE4EC))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MascotHeaderBanner(
            profile = profile,
            title = "Tentang Aplikasi",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.img_app_icon),
            contentDescription = "Kids Learning World",
            modifier = Modifier
                .size(110.dp)
                .clip(RoundedCornerShape(24.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Kids Learning World",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1F2937)
        )

        Text(
            text = "Versi 1.0.0 • Premium Kids Educational Game",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "🌟 Aman & Edukatif untuk Anak Usia 2-10 Tahun",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "• Bebas Iklan & Konten Tidak Sesuai\n• Dukungan 12 Bahasa Internasional\n• Generasi Level Otomatis (10.000+ Level)\n• Efek Suara Narasi Pengucapan Jelas\n• Dapat Dimainkan Tanpa Internet (Offline)",
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFF374151)
                )
            }
        }
    }
}
