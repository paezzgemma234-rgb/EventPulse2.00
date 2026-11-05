package com.seminario2026.eventpulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seminario2026.eventpulse.ui.components.VenueMapCard
import com.seminario2026.eventpulse.ui.components.VenueMapZoomDialog
import com.seminario2026.eventpulse.ui.theme.CyberCyan
import com.seminario2026.eventpulse.ui.theme.EventPulseTheme
import com.seminario2026.eventpulse.ui.theme.MidnightVoid

@Composable
fun VenueMapScreen(
    modifier: Modifier = Modifier
) {
    var showMapDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightVoid)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .padding(bottom = 90.dp)
        ) {
            Text(
                text = "UBICACIÓN Y DISTRIBUCIÓN",
                style = MaterialTheme.typography.labelLarge,
                color = CyberCyan
            )
            Text(
                text = "Mapa de la Sede",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            VenueMapCard(
                onExpandMapClick = { showMapDialog = true }
            )
        }

        if (showMapDialog) {
            VenueMapZoomDialog(
                onDismiss = { showMapDialog = false }
            )
        }
    }
}

// ------------------- PREVIEWS -------------------

@Preview(name = "Venue Map Screen Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun VenueMapScreenPreview() {
    EventPulseTheme {
        VenueMapScreen()
    }
}
