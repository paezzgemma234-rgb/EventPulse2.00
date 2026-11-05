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
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seminario2026.eventpulse.data.EventRepositoryImpl
import com.seminario2026.eventpulse.model.Category
import com.seminario2026.eventpulse.model.DigitalEvent
import com.seminario2026.eventpulse.ui.DateFilter
import com.seminario2026.eventpulse.ui.MainViewModel
import com.seminario2026.eventpulse.ui.UiState
import com.seminario2026.eventpulse.ui.components.AudioPulseVisualizer
import com.seminario2026.eventpulse.ui.components.CyberErrorState
import com.seminario2026.eventpulse.ui.components.CyberLoadingState
import com.seminario2026.eventpulse.ui.components.CyberSearchBar
import com.seminario2026.eventpulse.ui.components.DateBadge
import com.seminario2026.eventpulse.ui.components.EventPulseLogoHeader
import com.seminario2026.eventpulse.ui.components.GlassChip
import com.seminario2026.eventpulse.ui.components.NeonButton
import com.seminario2026.eventpulse.ui.components.PulseBadge
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
import com.seminario2026.eventpulse.ui.theme.glassmorphic
import com.seminario2026.eventpulse.ui.theme.pulsingGlow

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedDateFilter by viewModel.selectedDateFilter.collectAsState()
    val homeUiState by viewModel.homeUiState.collectAsState()
    val featuredEvents by viewModel.featuredEvents.collectAsState()
    var showFilterModal by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightVoid)
    ) {
        val isTabletOrWide = maxWidth >= 600.dp

        Column(modifier = Modifier.fillMaxSize()) {
            // Header Section
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
                    EventPulseLogoHeader()

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(DarkCharcoal)
                                .border(1.dp, CyberCyan.copy(alpha = 0.5f), CircleShape)
                                .clickable { showFilterModal = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🎛️", style = MaterialTheme.typography.titleSmall)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        PulseBadge(text = "LIVE", isLive = true)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                CyberSearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChanged(it) }
                )
            }

            // Categories Filter Row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                items(items = viewModel.categories, key = { it.id }) { category ->
                    GlassChip(
                        text = category.title,
                        isSelected = selectedCategory.id == category.id,
                        onClick = { viewModel.onCategorySelected(category) }
                    )
                }
            }

            // Date Filters Row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                items(items = viewModel.dateFilters, key = { it.name }) { dateFilter ->
                    GlassChip(
                        text = dateFilter.label,
                        isSelected = selectedDateFilter == dateFilter,
                        onClick = { viewModel.onDateFilterSelected(dateFilter) },
                        accentColor = ElectricViolet
                    )
                }
            }

            // Content Area (Adaptive Feed with UiState)
            when (val state = homeUiState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CyberLoadingState()
                    }
                }

                is UiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CyberErrorState(
                            message = state.message,
                            onRetry = state.onRetry
                        )
                    }
                }

                is UiState.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyStateCard(
                            title = "Sin experiencias encontradas",
                            subtitle = state.message
                        )
                    }
                }

                is UiState.Success -> {
                    val events = state.data
                    if (isTabletOrWide) {
                        // Grid Layout for Tablets & Wide screens
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 80.dp)
                        ) {
                            items(items = events, key = { it.id }) { event ->
                                EventFeedCard(
                                    event = event,
                                    onEventClick = { onEventClick(event.id) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(event.id) },
                                    modifier = Modifier.animateItem()
                                )
                            }
                        }
                    } else {
                        // Standard Vertical List for Phones
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 90.dp)
                        ) {
                            // Hero Carousel on Phone
                            if (featuredEvents.isNotEmpty() && searchQuery.isEmpty() && selectedCategory.id == Category.ALL.id && selectedDateFilter == DateFilter.ALL) {
                                item {
                                    Column(modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)) {
                                        Text(
                                            text = "DESTACADOS",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MutedSilver,
                                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                                        )

                                        LazyRow(
                                            contentPadding = PaddingValues(horizontal = 20.dp),
                                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            items(items = featuredEvents, key = { it.id }) { event ->
                                                HeroEventCard(
                                                    event = event,
                                                    onClick = { onEventClick(event.id) },
                                                    modifier = Modifier.animateItem()
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Section Title
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "EXPERIENCIAS & FESTIVALES",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "${events.size} disponibles",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MutedSilver
                                    )
                                }
                            }

                            // Feed List
                            items(items = events, key = { it.id }) { event ->
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

    if (showFilterModal) {
        FilterModalDialog(
            viewModel = viewModel,
            onDismiss = { showFilterModal = false }
        )
    }
}

@Composable
fun HeroEventCard(
    event: DigitalEvent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .width(300.dp)
            .height(190.dp)
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(DeepSpace, ElectricViolet.copy(alpha = 0.6f), MidnightVoid)
                )
            )
            .glassmorphic(shape = shape)
            .clickable { onClick() }
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassChip(
                    text = event.category.title,
                    isSelected = true,
                    onClick = {},
                    accentColor = AcidLime
                )

                if (event.isLive) {
                    PulseBadge(text = "EN VIVO", isLive = true)
                }
            }

            Column {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SoftWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = event.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedSilver
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.price,
                    style = MaterialTheme.typography.titleMedium,
                    color = CyberCyan,
                    fontWeight = FontWeight.ExtraBold
                )

                NeonButton(
                    text = "EXPLORAR",
                    onClick = onClick,
                    modifier = Modifier.height(32.dp)
                        .width(100.dp)
                )
            }
        }
    }
}

@Composable
fun EventFeedCard(
    event: DigitalEvent,
    onEventClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(20.dp)
    val borderModifier = if (event.isLive) {
        modifier.pulsingGlow(colorPrimary = NeonMagenta, colorSecondary = CyberCyan, shape = cardShape)
    } else {
        modifier
    }

    Box(
        modifier = borderModifier
            .fillMaxWidth()
            .clip(cardShape)
            .glassmorphic(shape = cardShape, backgroundColor = DarkCharcoal.copy(alpha = 0.7f))
            .clickable { onEventClick() }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (event.isLive) {
                        PulseBadge(text = "EN VIVO", isLive = true)
                        Spacer(modifier = Modifier.width(8.dp))
                        AudioPulseVisualizer(barCount = 5, activeColor = NeonMagenta, height = 16.dp)
                    } else {
                        GlassChip(
                            text = event.category.title,
                            isSelected = false,
                            onClick = {},
                            accentColor = CyberCyan
                        )
                    }
                }

                IconButton(onClick = onFavoriteToggle) {
                    Text(
                        text = if (event.isFavorite) "★" else "☆",
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (event.isFavorite) NeonMagenta else MutedSilver
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                DateBadge(
                    month = event.date.split(" ").getOrNull(3) ?: "OCT",
                    day = event.date.split(" ").firstOrNull() ?: "28"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SoftWhite
                    )
                    Text(
                        text = event.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedSilver,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "📅 ${event.date}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SoftWhite
                    )
                    Text(
                        text = "📍 ${event.location}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedSilver,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = event.price,
                    style = MaterialTheme.typography.labelLarge,
                    color = CyberCyan,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}

// ------------------- PREVIEWS -------------------

@Preview(name = "Home Screen Phone Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun HomeScreenPreview() {
    EventPulseTheme {
        val viewModel = androidx.compose.runtime.remember { MainViewModel(EventRepositoryImpl()) }
        HomeScreen(
            viewModel = viewModel,
            onEventClick = {}
        )
    }
}

@Preview(name = "Hero Event Card Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun HeroEventCardPreview() {
    EventPulseTheme {
        val sampleEvent = EventRepositoryImpl().getEventById("mutek-2026")
        if (sampleEvent != null) {
            Box(modifier = Modifier.padding(16.dp)) {
                HeroEventCard(event = sampleEvent, onClick = {})
            }
        }
    }
}

@Preview(name = "Event Feed Card Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun EventFeedCardPreview() {
    EventPulseTheme {
        val sampleEvent = EventRepositoryImpl().getEventById("mutek-2026")
        if (sampleEvent != null) {
            Box(modifier = Modifier.padding(16.dp)) {
                EventFeedCard(event = sampleEvent, onEventClick = {}, onFavoriteToggle = {})
            }
        }
    }
}
