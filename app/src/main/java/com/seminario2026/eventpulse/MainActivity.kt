package com.seminario2026.eventpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.seminario2026.eventpulse.navigation.Screen
import com.seminario2026.eventpulse.ui.MainViewModel
import com.seminario2026.eventpulse.ui.screens.EventDetailScreen
import com.seminario2026.eventpulse.ui.screens.FavoritesScreen
import com.seminario2026.eventpulse.ui.screens.HomeScreen
import com.seminario2026.eventpulse.ui.screens.SavedTicketsScreen
import com.seminario2026.eventpulse.ui.screens.ScheduleScreen
import com.seminario2026.eventpulse.ui.screens.VenueMapScreen
import com.seminario2026.eventpulse.ui.theme.CyberCyan
import com.seminario2026.eventpulse.ui.theme.DarkCharcoal
import com.seminario2026.eventpulse.ui.theme.EventPulseTheme
import com.seminario2026.eventpulse.ui.theme.MidnightVoid
import com.seminario2026.eventpulse.ui.theme.MutedSilver
import com.seminario2026.eventpulse.ui.theme.NeonMagenta
import com.seminario2026.eventpulse.ui.theme.glassmorphic

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventPulseTheme {
                EventPulseApp()
            }
        }
    }
}

@Composable
fun EventPulseApp() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModel: MainViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(com.seminario2026.eventpulse.data.EventRepositoryImpl(context.applicationContext)) as T
            }
        }
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isDetailScreen = currentRoute?.startsWith("detail") == true

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MidnightVoid
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Navigation Host using Jetpack Navigation Compose
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        viewModel = viewModel,
                        onEventClick = { eventId ->
                            navController.navigate(Screen.EventDetail.createRoute(eventId))
                        }
                    )
                }

                composable(Screen.Schedule.route) {
                    ScheduleScreen(
                        viewModel = viewModel,
                        onEventClick = { eventId ->
                            navController.navigate(Screen.EventDetail.createRoute(eventId))
                        }
                    )
                }

                composable(Screen.VenueMap.route) {
                    VenueMapScreen()
                }

                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        viewModel = viewModel,
                        onEventClick = { eventId ->
                            navController.navigate(Screen.EventDetail.createRoute(eventId))
                        }
                    )
                }

                composable(Screen.SavedTickets.route) {
                    SavedTicketsScreen(
                        viewModel = viewModel,
                        onEventClick = { eventId ->
                            navController.navigate(Screen.EventDetail.createRoute(eventId))
                        }
                    )
                }

                composable(
                    route = Screen.EventDetail.route,
                    arguments = listOf(navArgument("eventId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                    EventDetailScreen(
                        eventId = eventId,
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            // Bottom Navigation Bar
            if (!isDetailScreen) {
                CyberBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigateToRoute = { targetRoute ->
                        navController.navigate(targetRoute) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp, start = 20.dp, end = 20.dp)
                )
            }
        }
    }
}

@Composable
fun CyberBottomNavigation(
    currentRoute: String?,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(28.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .clip(shape)
            .glassmorphic(shape = shape, backgroundColor = DarkCharcoal.copy(alpha = 0.9f))
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(CyberCyan.copy(alpha = 0.5f), NeonMagenta.copy(alpha = 0.5f))
                ),
                shape = shape
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Screen.bottomNavItems.forEach { item ->
                NavItem(
                    label = item.label,
                    iconSymbol = item.iconSymbol,
                    isSelected = currentRoute == item.route,
                    onClick = { onNavigateToRoute(item.route) }
                )
            }
        }
    }
}

@Composable
fun NavItem(
    label: String,
    iconSymbol: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = if (isSelected) CyberCyan else MutedSilver

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = iconSymbol,
                style = MaterialTheme.typography.titleLarge
            )
            if (isSelected) {
                Spacer(modifier = Modifier.padding(start = 6.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(NeonMagenta)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = activeColor,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal
        )
    }
}

// ------------------- PREVIEWS -------------------

@Preview(name = "EventPulse Full App Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun EventPulseAppPreview() {
    EventPulseTheme {
        EventPulseApp()
    }
}

@Preview(name = "Cyber Bottom Navigation Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun CyberBottomNavigationPreview() {
    EventPulseTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CyberBottomNavigation(
                currentRoute = Screen.Home.route,
                onNavigateToRoute = {}
            )
        }
    }
}

@Preview(name = "Nav Item Selected Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun NavItemPreview() {
    EventPulseTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
            NavItem(label = "EXPLORAR", iconSymbol = "⚡", isSelected = true, onClick = {})
            NavItem(label = "CRONOGRAMA", iconSymbol = "📡", isSelected = false, onClick = {})
        }
    }
}
