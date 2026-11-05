package com.seminario2026.eventpulse.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seminario2026.eventpulse.data.EventRepositoryImpl
import com.seminario2026.eventpulse.model.DigitalTicket
import com.seminario2026.eventpulse.ui.MainViewModel
import com.seminario2026.eventpulse.ui.components.AudioPulseVisualizer
import com.seminario2026.eventpulse.ui.components.GlassChip
import com.seminario2026.eventpulse.ui.components.NeonButton
import com.seminario2026.eventpulse.ui.components.PulseBadge
import com.seminario2026.eventpulse.ui.components.VenueMapCard
import com.seminario2026.eventpulse.ui.components.VenueMapZoomDialog
import com.seminario2026.eventpulse.ui.theme.AcidLime
import com.seminario2026.eventpulse.ui.theme.CyberCyan
import com.seminario2026.eventpulse.ui.theme.DarkCharcoal
import com.seminario2026.eventpulse.ui.theme.DeepSpace
import com.seminario2026.eventpulse.ui.theme.ElectricViolet
import com.seminario2026.eventpulse.ui.theme.EventPulseTheme
import com.seminario2026.eventpulse.ui.theme.MidnightVoid
import com.seminario2026.eventpulse.ui.theme.MutedSilver
import com.seminario2026.eventpulse.ui.theme.NeonMagenta
import com.seminario2026.eventpulse.ui.theme.SoftWhite
import com.seminario2026.eventpulse.ui.theme.SurfaceGlassBorder
import com.seminario2026.eventpulse.ui.theme.glassmorphic
import com.seminario2026.eventpulse.ui.theme.pulsingGlow

@Composable
fun EventDetailScreen(
    eventId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val event = viewModel.getEventById(eventId) ?: return
    var selectedTab by remember { mutableIntStateOf(0) }
    var showTicketModal by remember { mutableStateOf(false) }
    var showMapDialog by remember { mutableStateOf(false) }
    var generatedTicket by remember { mutableStateOf<DigitalTicket?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightVoid)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 100.dp)
        ) {
            // Hero Image / Header Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(ElectricViolet.copy(alpha = 0.8f), DeepSpace, MidnightVoid)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Navigation Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(DarkCharcoal.copy(alpha = 0.8f))
                                    .border(1.dp, CyberCyan.copy(alpha = 0.5f), CircleShape)
                                    .clickable { onBackClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "←",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = CyberCyan
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(DarkCharcoal.copy(alpha = 0.8f))
                                    .border(1.dp, NeonMagenta.copy(alpha = 0.6f), CircleShape)
                                    .clickable { viewModel.toggleFavorite(event.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (event.isFavorite) "❤️" else "🤍",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                        // Event Title & Meta
                        Column {
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.displayLarge,
                                color = SoftWhite,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "📍 ${event.location}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MutedSilver
                            )
                            Text(
                                text = "📅 ${event.date}",
                                style = MaterialTheme.typography.labelLarge,
                                color = AcidLime
                            )
                        }
                    }
                }
            }

            // Tab Selector Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassChip(
                        text = "INFORMACIÓN",
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    GlassChip(
                        text = "CRONOGRAMA",
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                    GlassChip(
                        text = "ARTISTAS",
                        isSelected = selectedTab == 2,
                        onClick = { selectedTab = 2 }
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> {
                    item {
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            Text(
                                text = "SOBRE LA EXPERIENCIA",
                                style = MaterialTheme.typography.labelLarge,
                                color = CyberCyan
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = event.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = SoftWhite,
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "ESPECIFICACIONES TÉCNICAS",
                                style = MaterialTheme.typography.labelLarge,
                                color = NeonMagenta
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            SpecCard(title = "Sonido Espacial 3D", value = "AmbiSonic 64 Canales High-Res")
                            SpecCard(title = "Pantalla & Mapeo", value = "FullDome Geodésico 360° + Laser Sync")
                            SpecCard(title = "Interactividad", value = "Captura de Frecuencia Cardíaca & IA Bio-Data")

                            Spacer(modifier = Modifier.height(24.dp))

                            VenueMapCard(
                                onExpandMapClick = { showMapDialog = true }
                            )
                        }
                    }
                }

                1 -> {
                    items(event.stages) { stage ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 6.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .glassmorphic(shape = RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stage.stageName.uppercase(),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = AcidLime
                                    )
                                    Text(
                                        text = stage.timeSlot,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = CyberCyan
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = stage.artistName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftWhite
                                )

                                Text(
                                    text = stage.genre,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MutedSilver
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Intensidad de Pulso: ${stage.pulseIntensity}%",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NeonMagenta
                                    )
                                    AudioPulseVisualizer(
                                        barCount = 6,
                                        activeColor = CyberCyan,
                                        height = 18.dp
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    items(event.artists) { artist ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .glassmorphic(shape = RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(
                                                brush = Brush.radialGradient(
                                                    listOf(ElectricViolet, DarkCharcoal)
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = artist.name.take(1),
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = SoftWhite
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column {
                                        Text(
                                            text = artist.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = SoftWhite
                                        )
                                        Text(
                                            text = artist.specialty,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = CyberCyan
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = artist.bio,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MutedSilver
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Action Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MidnightVoid.copy(alpha = 0.95f))
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(listOf(CyberCyan.copy(alpha = 0.3f), NeonMagenta.copy(alpha = 0.3f))),
                    shape = RoundedCornerShape(0.dp)
                )
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ACCESO INDIVIDUAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedSilver,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = event.price,
                        style = MaterialTheme.typography.titleLarge,
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        onClick = { viewModel.toggleFavorite(event.id) },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkCharcoal)
                            .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    ) {
                        Text(
                            text = if (event.isFavorite) "★" else "☆",
                            style = MaterialTheme.typography.titleLarge,
                            color = if (event.isFavorite) NeonMagenta else SoftWhite
                        )
                    }

                    NeonButton(
                        text = if (event.hasPass) "PASS OBTENIDO" else "CONSEGUIR PASS",
                        onClick = {
                            val ticket = viewModel.purchasePass(event.id)
                            generatedTicket = ticket
                            showTicketModal = true
                        },
                        modifier = Modifier.height(44.dp),
                        gradientColors = if (event.hasPass) listOf(AcidLime, CyberCyan) else listOf(NeonMagenta, ElectricViolet)
                    )
                }
            }
        }

        // Ticket Generation Modal
        if (showTicketModal && generatedTicket != null) {
            TicketDialog(
                ticket = generatedTicket!!,
                onDismiss = { showTicketModal = false }
            )
        }

        // Venue Map Zoom Modal
        if (showMapDialog) {
            VenueMapZoomDialog(
                onDismiss = { showMapDialog = false }
            )
        }
    }
}

@Composable
fun SpecCard(title: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(DarkCharcoal.copy(alpha = 0.7f))
            .border(1.dp, SurfaceGlassBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = CyberCyan,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = SoftWhite,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun TicketDialogContent(
    ticket: DigitalTicket,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = DeepSpace
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚡ CYBER PASS GENERADO",
                style = MaterialTheme.typography.titleLarge,
                color = CyberCyan,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = ticket.eventTitle,
                style = MaterialTheme.typography.titleMedium,
                color = SoftWhite
            )
            Text(
                text = ticket.accessZone,
                style = MaterialTheme.typography.labelLarge,
                color = AcidLime
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MidnightVoid)
                    .pulsingGlow(colorPrimary = CyberCyan, colorSecondary = NeonMagenta, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cellSize = size.width / 6f
                    for (row in 0 until 6) {
                        for (col in 0 until 6) {
                            if ((row + col) % 2 == 0) {
                                drawRect(
                                    color = if ((row + col) % 3 == 0) CyberCyan else NeonMagenta,
                                    topLeft = androidx.compose.ui.geometry.Offset(col * cellSize, row * cellSize),
                                    size = androidx.compose.ui.geometry.Size(cellSize * 0.85f, cellSize * 0.85f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ID: ${ticket.ticketId}",
                style = MaterialTheme.typography.labelSmall,
                color = MutedSilver
            )

            Spacer(modifier = Modifier.height(20.dp))

            NeonButton(
                text = "ENTENDIDO",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TicketDialog(
    ticket: DigitalTicket,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        text = {
            TicketDialogContent(ticket = ticket, onDismiss = onDismiss)
        }
    )
}

// ------------------- PREVIEWS -------------------

@Preview(name = "Event Detail Screen Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun EventDetailScreenPreview() {
    EventPulseTheme {
        val viewModel = remember { MainViewModel(EventRepositoryImpl()) }
        EventDetailScreen(
            eventId = "mutek-2026",
            viewModel = viewModel,
            onBackClick = {}
        )
    }
}

@Preview(name = "Spec Card Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun SpecCardPreview() {
    EventPulseTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SpecCard(title = "Sonido Espacial 3D", value = "AmbiSonic 64 Canales High-Res")
        }
    }
}

@Preview(name = "Ticket Dialog Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun TicketDialogPreview() {
    EventPulseTheme {
        val sampleTicket = DigitalTicket(
            ticketId = "PASS-9982-MUTEK",
            eventId = "mutek-2026",
            eventTitle = "MUTEK CyberPulse 2026",
            date = "28 OCTUBRE 2026",
            location = "Dome 360°, CABA",
            accessZone = "VIP FULLDOME ACCESS",
            qrCodeSeed = "PREVIEW_TOKEN"
        )
        Box(modifier = Modifier.padding(16.dp)) {
            TicketDialogContent(ticket = sampleTicket, onDismiss = {})
        }
    }
}
