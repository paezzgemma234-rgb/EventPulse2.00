package com.seminario2026.eventpulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seminario2026.eventpulse.data.EventRepositoryImpl
import com.seminario2026.eventpulse.ui.MainViewModel
import com.seminario2026.eventpulse.ui.UiState
import com.seminario2026.eventpulse.ui.components.CyberErrorState
import com.seminario2026.eventpulse.ui.components.CyberLoadingState
import com.seminario2026.eventpulse.ui.theme.CyberCyan
import com.seminario2026.eventpulse.ui.theme.EventPulseTheme
import com.seminario2026.eventpulse.ui.theme.MidnightVoid

@Composable
fun FavoritesScreen(
    viewModel: MainViewModel,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteEventsUiState by viewModel.favoriteEventsUiState.collectAsState()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightVoid)
    ) {
        val isTabletOrWide = maxWidth >= 600.dp

        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "MI LISTA PERSONAL",
                    style = MaterialTheme.typography.labelLarge,
                    color = CyberCyan
                )
                Text(
                    text = "Eventos Favoritos",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

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

// ------------------- PREVIEWS -------------------

@Preview(name = "Favorites Screen Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun FavoritesScreenPreview() {
    EventPulseTheme {
        val viewModel = remember { MainViewModel(EventRepositoryImpl()) }
        FavoritesScreen(viewModel = viewModel, onEventClick = {})
    }
}
