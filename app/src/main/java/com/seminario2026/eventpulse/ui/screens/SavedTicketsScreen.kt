package com.seminario2026.eventpulse.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.seminario2026.eventpulse.data.EventRepositoryImpl
import com.seminario2026.eventpulse.model.DigitalTicket
import com.seminario2026.eventpulse.ui.MainViewModel
import com.seminario2026.eventpulse.ui.UiState
import com.seminario2026.eventpulse.ui.components.AudioPulseVisualizer
import com.seminario2026.eventpulse.ui.components.CyberCard
import com.seminario2026.eventpulse.ui.components.CyberErrorState
import com.seminario2026.eventpulse.ui.components.CyberLoadingState
import com.seminario2026.eventpulse.ui.components.GlassChip
import com.seminario2026.eventpulse.ui.components.NeonButton
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
import com.seminario2026.eventpulse.ui.theme.pulsingGlow

@Composable
fun SavedTicketsScreen(
    viewModel: MainViewModel,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val savedTicketsUiState by viewModel.savedTicketsUiState.collectAsState()
    val favoriteEventsUiState by viewModel.favoriteEventsUiState.collectAsState()
    var selectedSection by remember { mutableIntStateOf(0) }
    var activeTicketModal by remember { mutableStateOf<DigitalTicket?>(null) }

    val passCount = (savedTicketsUiState as? UiState.Success)?.data?.size ?: 0
    val favCount = (favoriteEventsUiState as? UiState.Success)?.data?.size ?: 0

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightVoid)
    ) {
        val isTabletOrWide = maxWidth >= 600.dp

        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "MI EVENTPULSE",
                    style = MaterialTheme.typography.labelLarge,
                    color = CyberCyan
                )
                Text(
                    text = "Pases CyberPass & Favoritos",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section Selector Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlassChip(
                        text = "MIS PASES ($passCount)",
                        isSelected = selectedSection == 0,
                        onClick = { selectedSection = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    GlassChip(
                        text = "FAVORITOS ($favCount)",
                        isSelected = selectedSection == 1,
                        onClick = { selectedSection = 1 },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Content Area with UiState
            if (selectedSection == 0) {
                when (val state = savedTicketsUiState) {
                    is UiState.Loading -> CyberLoadingState()
                    is UiState.Error -> CyberErrorState(message = state.message, onRetry = state.onRetry)
                    is UiState.Empty -> EmptyStateCard(
                        title = "No tienes pases activos",
                        subtitle = state.message
                    )
                    is UiState.Success -> {
                        val savedTickets = state.data
                        if (isTabletOrWide) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 80.dp)
                            ) {
                                items(items = savedTickets, key = { it.ticketId }) { ticket ->
                                    CyberPassCard(
                                        ticket = ticket,
                                        onClick = { activeTicketModal = ticket },
                                        onViewEvent = { onEventClick(ticket.eventId) },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 90.dp)
                            ) {
                                items(items = savedTickets, key = { it.ticketId }) { ticket ->
                                    CyberPassCard(
                                        ticket = ticket,
                                        onClick = { activeTicketModal = ticket },
                                        onViewEvent = { onEventClick(ticket.eventId) },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                when (val state = favoriteEventsUiState) {
                    is UiState.Loading -> CyberLoadingState()
                    is UiState.Error -> CyberErrorState(message = state.message, onRetry = state.onRetry)
                    is UiState.Empty -> EmptyStateCard(
                        title = "No hay eventos favoritos",
                        subtitle = "Toca la estrella ★ en cualquier festival para guardarlo en tu lista personal."
                    )
                    is UiState.Success -> {
                        val favoriteEvents = state.data
                        if (isTabletOrWide) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 80.dp)
                            ) {
                                items(items = favoriteEvents, key = { it.id }) { event ->
                                    EventFeedCard(
                                        event = event,
                                        onEventClick = { onEventClick(event.id) },
                                        onFavoriteToggle = { viewModel.toggleFavorite(event.id) },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 90.dp)
                            ) {
                                items(items = favoriteEvents, key = { it.id }) { event ->
                                    EventFeedCard(
                                        event = event,
                                        onEventClick = { onEventClick(event.id) },
                                        onFavoriteToggle = { viewModel.toggleFavorite(event.id) },
                                        modifier = Modifier
                                            .animateItem()
                                            .padding(horizontal = 20.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal
    if (activeTicketModal != null) {
        TicketDialog(
            ticket = activeTicketModal!!,
            onDismiss = { activeTicketModal = null }
        )
    }
}

@Composable
fun CyberPassCard(
    ticket: DigitalTicket,
    onClick: () -> Unit,
    onViewEvent: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(20.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .pulsingGlow(colorPrimary = NeonMagenta, colorSecondary = CyberCyan, shape = cardShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(DeepSpace, DarkCharcoal, MidnightVoid)
                )
            )
            .clickable { onClick() }
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AudioPulseVisualizer(barCount = 4, activeColor = CyberCyan, height = 14.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "EVENTPULSE",
                        style = MaterialTheme.typography.labelMedium,
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(NeonMagenta)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ACTIVO",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoftWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ticket.eventTitle,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SoftWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "📍 ${ticket.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedSilver
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📅 ${ticket.date}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoftWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "#${ticket.ticketId}",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightVoid)
                        .border(1.dp, CyberCyan, RoundedCornerShape(12.dp))
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cellSize = size.width / 4f
                        for (r in 0 until 4) {
                            for (c in 0 until 4) {
                                if ((r + c) % 2 == 0) {
                                    drawRect(
                                        color = if ((r * c) % 2 == 0) CyberCyan else NeonMagenta,
                                        topLeft = androidx.compose.ui.geometry.Offset(c * cellSize, r * cellSize),
                                        size = androidx.compose.ui.geometry.Size(cellSize * 0.8f, cellSize * 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Info Card with Detail Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DeepSpace.copy(alpha = 0.9f))
                    .border(1.dp, MutedSilver.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💎 Tus pases están guardados en tu dispositivo.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedSilver,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    NeonButton(
                        text = "Ver detalle",
                        onClick = onViewEvent,
                        modifier = Modifier
                            .height(34.dp)
                            .width(100.dp),
                        gradientColors = listOf(CyberCyan, ElectricViolet)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateCard(title: String, subtitle: String) {
    CyberCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚡",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = SoftWhite
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedSilver,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

// ------------------- PREVIEWS -------------------

@Preview(name = "Saved Tickets Screen Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun SavedTicketsScreenPreview() {
    EventPulseTheme {
        val viewModel = remember { MainViewModel(EventRepositoryImpl()) }
        SavedTicketsScreen(viewModel = viewModel, onEventClick = {})
    }
}

@Preview(name = "Cyber Pass Card Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun CyberPassCardPreview() {
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
            CyberPassCard(ticket = sampleTicket, onClick = {}, onViewEvent = {})
        }
    }
}

@Preview(name = "Empty State Card Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun EmptyStateCardPreview() {
    EventPulseTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            EmptyStateCard(
                title = "No tienes pases activos",
                subtitle = "Explora los festivales e instalaciones para conseguir tu acceso CyberPass."
            )
        }
    }
}
