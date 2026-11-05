package com.seminario2026.eventpulse.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seminario2026.eventpulse.ui.theme.CyberCyan
import com.seminario2026.eventpulse.ui.theme.EventPulseTheme
import com.seminario2026.eventpulse.ui.theme.MidnightVoid
import com.seminario2026.eventpulse.ui.theme.NeonMagenta
import com.seminario2026.eventpulse.ui.theme.SoftWhite

data class Category(
    val id: String,
    val title: String,
    val iconName: String = ""
) {
    companion object {
        val ALL = Category("all", "Todos")
        val IMMERSIVE = Category("immersive", "Instalaciones Inmersivas")
        val ELECTRONIC = Category("electronic", "Música Electrónica")
        val GENERATIVE = Category("generative", "Arte Generativo")
        val VR_AR = Category("vrar", "VR / AR")
        val AV_LIVE = Category("avlive", "Performances AV")

        val DEFAULT_CATEGORIES = listOf(ALL, IMMERSIVE, ELECTRONIC, GENERATIVE, VR_AR, AV_LIVE)
    }
}

data class Artist(
    val id: String,
    val name: String,
    val specialty: String,
    val bio: String,
    val avatarUrl: String = ""
)

data class StageSchedule(
    val id: String,
    val stageName: String,
    val timeSlot: String,
    val artistName: String,
    val genre: String,
    val pulseIntensity: Int = 85 // 0 to 100 for live pulse reactivity
)

data class DigitalEvent(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: Category,
    val date: String,
    val location: String,
    val imageUrl: String,
    val isLive: Boolean = false,
    val isFeatured: Boolean = false,
    val description: String,
    val price: String,
    val artists: List<Artist>,
    val stages: List<StageSchedule>,
    val isFavorite: Boolean = false,
    val hasPass: Boolean = false
)

data class DigitalTicket(
    val ticketId: String,
    val eventId: String,
    val eventTitle: String,
    val date: String,
    val location: String,
    val accessZone: String,
    val qrCodeSeed: String,
    val holderName: String = "CYBER_VISITOR_2026"
)

// ------------------- PREVIEWS -------------------

@Preview(name = "Models Overview Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun ModelsOverviewPreview() {
    EventPulseTheme {
        Column(
            modifier = Modifier
                .background(MidnightVoid)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "EVENTPULSE MODEL DATA",
                style = MaterialTheme.typography.titleMedium,
                color = CyberCyan
            )
            Text(
                text = "CATEGORÍAS DEFAULT (${Category.DEFAULT_CATEGORIES.size})",
                style = MaterialTheme.typography.labelLarge,
                color = NeonMagenta
            )
            Category.DEFAULT_CATEGORIES.forEach { category ->
                Text(
                    text = "• ${category.title} (${category.id})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SoftWhite
                )
            }
        }
    }
}
