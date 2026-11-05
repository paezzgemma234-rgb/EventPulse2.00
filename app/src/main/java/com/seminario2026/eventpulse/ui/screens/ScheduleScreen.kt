package com.seminario2026.eventpulse.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.seminario2026.eventpulse.model.DigitalEvent
import com.seminario2026.eventpulse.model.StageSchedule
import com.seminario2026.eventpulse.ui.MainViewModel
import com.seminario2026.eventpulse.ui.components.AudioPulseVisualizer
import com.seminario2026.eventpulse.ui.components.GlassChip
import com.seminario2026.eventpulse.ui.components.PulseBadge
import com.seminario2026.eventpulse.ui.theme.AcidLime
import com.seminario2026.eventpulse.ui.theme.CyberCyan
import com.seminario2026.eventpulse.ui.theme.DeepSpace
import com.seminario2026.eventpulse.ui.theme.ElectricViolet
import com.seminario2026.eventpulse.ui.theme.EventPulseTheme
import com.seminario2026.eventpulse.ui.theme.MidnightVoid
import com.seminario2026.eventpulse.ui.theme.MutedSilver
import com.seminario2026.eventpulse.ui.theme.NeonMagenta
import com.seminario2026.eventpulse.ui.theme.SoftWhite
import com.seminario2026.eventpulse.ui.theme.glassmorphic

data class StageItemWithEvent(
    val stage: StageSchedule,
    val event: DigitalEvent
)

@Composable
fun ScheduleScreen(
    viewModel: MainViewModel,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val events by viewModel.allEvents.collectAsState()
    val selectedScheduleDate by viewModel.selectedScheduleDate.collectAsState()
    val availableDates by viewModel.availableScheduleDates.collectAsState()
    var selectedStageFilter by remember { mutableStateOf("TODOS") }

    val allStageItems = events.flatMap { event ->
        event.stages.map { stage -> StageItemWithEvent(stage, event) }
    }

    // 1. Filter by Selected Date/Day
    val dateFilteredItems = if (selectedScheduleDate == "TODAS") {
        allStageItems
    } else {
        allStageItems.filter { it.event.date.equals(selectedScheduleDate, ignoreCase = true) }
    }

    // 2. Dynamic Available Stage Names
    val availableStageNames = listOf("TODOS") + dateFilteredItems.map { it.stage.stageName }.distinct()

    // 3. Filter by Selected Stage
    val stageFilteredItems = if (selectedStageFilter == "TODOS" || !availableStageNames.contains(selectedStageFilter)) {
        dateFilteredItems
    } else {
        dateFilteredItems.filter { it.stage.stageName.equals(selectedStageFilter, ignoreCase = true) }
    }

    // 4. Chronological Sort by Time Slot
    val filteredStageItems = stageFilteredItems.sortedBy { item ->
        val startTimeStr = item.stage.timeSlot.split("-").firstOrNull()?.trim() ?: "00:00"
        val parts = startTimeStr.split(":")
        val hours = parts.firstOrNull()?.toIntOrNull() ?: 0
        val minutes = parts.getOrNull(1)?.toIntOrNull() ?: 0
        hours * 60 + minutes
    }

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "MONITOR EN VIVO",
                            style = MaterialTheme.typography.labelLarge,
                            color = AcidLime
                        )
                        Text(
                            text = "Cronograma de Domos",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    PulseBadge(text = "LIVE METER", isLive = true)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Reactive Pulse Visualizer Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(DeepSpace, ElectricViolet.copy(alpha = 0.5f), MidnightVoid)
                            )
                        )
                        .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "FRECUENCIA DEL FESTIVAL",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyberCyan
                            )
                            Text(
                                text = "98.4 dB / 432 Hz Sync",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = SoftWhite
                            )
                        }

                        AudioPulseVisualizer(barCount = 10, activeColor = NeonMagenta, height = 30.dp)
                    }
                }
            }

            // Calendar Days Selector Row
            val dayCardsData = listOf(
                Triple("Jue", "23", "Oct"),
                Triple("Vie", "24", "Oct"),
                Triple("Sáb", "25", "Oct"),
                Triple("Dom", "26", "Oct")
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
            ) {
                item {
                    GlassChip(
                        text = "TODAS",
                        isSelected = selectedScheduleDate == "TODAS",
                        onClick = { viewModel.onScheduleDateSelected("TODAS") },
                        accentColor = NeonMagenta
                    )
                }
                items(items = dayCardsData, key = { it.second }) { (dayName, dayNum, month) ->
                    val fullDateStr = "$dayNum $month"
                    ScheduleDayCard(
                        dayName = dayName,
                        dayNumber = dayNum,
                        month = month,
                        isSelected = selectedScheduleDate.contains(dayNum),
                        onClick = { viewModel.onScheduleDateSelected(fullDateStr) }
                    )
                }
            }

            // Stage Filter Chips Row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)
            ) {
                items(items = availableStageNames, key = { it }) { stageName ->
                    GlassChip(
                        text = stageName,
                        isSelected = selectedStageFilter == stageName,
                        onClick = { selectedStageFilter = stageName },
                        accentColor = ElectricViolet
                    )
                }
            }

            // Content Feed
            if (filteredStageItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyStateCard(
                        title = "Sin programaciones activas",
                        subtitle = "No hay horarios registrados para el escenario seleccionado."
                    )
                }
            } else {
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
                        items(items = filteredStageItems, key = { "${it.event.id}_${it.stage.id}" }) { item ->
                            StageCard(
                                item = item,
                                onEventClick = onEventClick,
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 90.dp)
                    ) {
                        items(items = filteredStageItems, key = { "${it.event.id}_${it.stage.id}" }) { item ->
                            StageCard(
                                item = item,
                                onEventClick = onEventClick,
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

@Composable
fun ScheduleDayCard(
    dayName: String,
    dayNumber: String,
    month: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    val background = if (isSelected) NeonMagenta else DeepSpace.copy(alpha = 0.8f)
    val borderColor = if (isSelected) NeonMagenta else MutedSilver.copy(alpha = 0.3f)

    Box(
        modifier = modifier
            .width(60.dp)
            .height(80.dp)
            .clip(shape)
            .background(background)
            .border(1.dp, borderColor, shape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = dayName,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) SoftWhite else MutedSilver
            )
            Text(
                text = dayNumber,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = SoftWhite
            )
            Text(
                text = month,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) SoftWhite else MutedSilver
            )
        }
    }
}

@Composable
fun StageCard(
    item: StageItemWithEvent,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val stage = item.stage
    val event = item.event

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .glassmorphic(shape = RoundedCornerShape(20.dp))
            .clickable { onEventClick(event.id) }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberCyan.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stage.stageName.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ElectricViolet.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = event.category.title,
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricViolet,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = stage.timeSlot,
                    style = MaterialTheme.typography.titleMedium,
                    color = AcidLime,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stage.artistName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = SoftWhite
            )

            Text(
                text = "${stage.genre} • ${event.title}",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedSilver
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "📅 ${event.date}",
                style = MaterialTheme.typography.labelSmall,
                color = SoftWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Live Reactivity Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(NeonMagenta)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PULSE INTENSITY: ${stage.pulseIntensity}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonMagenta
                    )
                }

                AudioPulseVisualizer(barCount = 7, activeColor = CyberCyan, height = 18.dp)
            }
        }
    }
}

// ------------------- PREVIEWS -------------------

@Preview(name = "Schedule Screen Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun ScheduleScreenPreview() {
    EventPulseTheme {
        val viewModel = remember { MainViewModel(EventRepositoryImpl()) }
        ScheduleScreen(viewModel = viewModel, onEventClick = {})
    }
}

@Preview(name = "Stage Card Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun StageCardPreview() {
    EventPulseTheme {
        val sampleEvent = EventRepositoryImpl().getEventById("mutek-2026")
        if (sampleEvent != null && sampleEvent.stages.isNotEmpty()) {
            Box(modifier = Modifier.padding(16.dp)) {
                StageCard(
                    item = StageItemWithEvent(sampleEvent.stages[0], sampleEvent),
                    onEventClick = {}
                )
            }
        }
    }
}

@Preview(name = "Schedule Empty State Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun ScheduleEmptyStatePreview() {
    EventPulseTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            EmptyStateCard(
                title = "Sin programaciones activas",
                subtitle = "No hay horarios registrados para el escenario seleccionado."
            )
        }
    }
}
