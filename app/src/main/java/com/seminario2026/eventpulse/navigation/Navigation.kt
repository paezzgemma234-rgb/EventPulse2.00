package com.seminario2026.eventpulse.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seminario2026.eventpulse.ui.theme.CyberCyan
import com.seminario2026.eventpulse.ui.theme.EventPulseTheme
import com.seminario2026.eventpulse.ui.theme.MidnightVoid
import com.seminario2026.eventpulse.ui.theme.SoftWhite

sealed class Screen(val route: String, val label: String, val iconSymbol: String) {
    object Home : Screen("home", "Inicio", "🏠")
    object Schedule : Screen("schedule", "Cronograma", "📅")
    object VenueMap : Screen("map", "Mapa", "🗺️")
    object Favorites : Screen("favorites", "Favoritos", "❤️")
    object SavedTickets : Screen("saved_tickets", "Pases", "🎟️")
    object EventDetail : Screen("detail/{eventId}", "Detalle", "") {
        fun createRoute(eventId: String) = "detail/$eventId"
    }

    companion object {
        val bottomNavItems: List<Screen>
            get() = listOf(Home, Schedule, VenueMap, Favorites, SavedTickets)
    }
}

// ------------------- PREVIEWS -------------------

@Preview(name = "Navigation Screens Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun ScreenNavigationPreview() {
    EventPulseTheme {
        Column(
            modifier = Modifier
                .background(MidnightVoid)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "RUTAS DE NAVEGACIÓN",
                style = MaterialTheme.typography.titleMedium,
                color = CyberCyan
            )
            Screen.bottomNavItems.forEach { screen ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = screen.iconSymbol, style = MaterialTheme.typography.titleLarge)
                    Text(text = screen.label, style = MaterialTheme.typography.bodyLarge, color = SoftWhite)
                    Text(text = "(${screen.route})", style = MaterialTheme.typography.labelSmall, color = CyberCyan)
                }
            }
        }
    }
}
