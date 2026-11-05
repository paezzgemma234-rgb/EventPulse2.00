package com.seminario2026.eventpulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seminario2026.eventpulse.data.EventRepositoryImpl
import com.seminario2026.eventpulse.model.Category
import com.seminario2026.eventpulse.ui.DateFilter
import com.seminario2026.eventpulse.ui.MainViewModel
import com.seminario2026.eventpulse.ui.components.GlassChip
import com.seminario2026.eventpulse.ui.components.NeonButton
import com.seminario2026.eventpulse.ui.theme.CyberCyan
import com.seminario2026.eventpulse.ui.theme.DarkCharcoal
import com.seminario2026.eventpulse.ui.theme.DeepSpace
import com.seminario2026.eventpulse.ui.theme.EventPulseTheme
import com.seminario2026.eventpulse.ui.theme.MutedSilver
import com.seminario2026.eventpulse.ui.theme.NeonMagenta
import com.seminario2026.eventpulse.ui.theme.SoftWhite

@Composable
fun FilterModalContent(
    viewModel: MainViewModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedDateFilter by viewModel.selectedDateFilter.collectAsState()

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = DeepSpace
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkCharcoal)
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "←", style = MaterialTheme.typography.titleLarge, color = CyberCyan)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Filtros",
                        style = MaterialTheme.typography.titleLarge,
                        color = SoftWhite,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(DarkCharcoal)
                        .clickable {
                            viewModel.onCategorySelected(Category.ALL)
                            viewModel.onDateFilterSelected(DateFilter.ALL)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🔄", style = MaterialTheme.typography.titleSmall, color = SoftWhite)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Categorías Section
            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleMedium,
                color = SoftWhite,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                viewModel.categories.chunked(2).forEach { rowCategories ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rowCategories.forEach { category ->
                            GlassChip(
                                text = category.title,
                                isSelected = selectedCategory.id == category.id,
                                onClick = { viewModel.onCategorySelected(category) },
                                accentColor = NeonMagenta
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Fecha Section
            Text(
                text = "Fecha",
                style = MaterialTheme.typography.titleMedium,
                color = SoftWhite,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                viewModel.dateFilters.chunked(3).forEach { rowDates ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rowDates.forEach { dateFilter ->
                            GlassChip(
                                text = dateFilter.label,
                                isSelected = selectedDateFilter == dateFilter,
                                onClick = { viewModel.onDateFilterSelected(dateFilter) },
                                accentColor = NeonMagenta
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(50))
                        .background(DarkCharcoal)
                        .border(1.dp, MutedSilver.copy(alpha = 0.4f), RoundedCornerShape(50))
                        .clickable {
                            viewModel.onCategorySelected(Category.ALL)
                            viewModel.onDateFilterSelected(DateFilter.ALL)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Limpiar",
                        style = MaterialTheme.typography.labelLarge,
                        color = SoftWhite,
                        fontWeight = FontWeight.Bold
                    )
                }

                NeonButton(
                    text = "Aplicar",
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    gradientColors = listOf(NeonMagenta, CyberCyan)
                )
            }
        }
    }
}

@Composable
fun FilterModalDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        containerColor = Color.Transparent,
        text = {
            FilterModalContent(viewModel = viewModel, onDismiss = onDismiss)
        }
    )
}

// ------------------- PREVIEWS -------------------

@Preview(name = "Filter Modal Dialog Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun FilterModalDialogPreview() {
    EventPulseTheme {
        val viewModel = androidx.compose.runtime.remember { MainViewModel(EventRepositoryImpl()) }
        Box(modifier = Modifier.padding(16.dp)) {
            FilterModalContent(viewModel = viewModel, onDismiss = {})
        }
    }
}
