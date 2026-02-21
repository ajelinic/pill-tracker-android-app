package com.pilltracker.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pilltracker.ui.screens.HistoryScreen
import com.pilltracker.ui.screens.HomeScreen
import com.pilltracker.ui.screens.SettingsScreen

private sealed class Dest(val route: String, val label: String) {
    data object Home : Dest("home", "Home")
    data object History : Dest("history", "History")
    data object Settings : Dest("settings", "Settings")
}

@Composable
fun PillTrackerRoot(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val items = listOf(Dest.Home, Dest.History, Dest.Settings)

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { dest ->
                    val selected = currentRoute == dest.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(dest.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            when (dest) {
                                Dest.Home -> Icon(Icons.Default.Home, contentDescription = dest.label)
                                Dest.History -> Icon(Icons.Default.History, contentDescription = dest.label)
                                Dest.Settings -> Icon(Icons.Default.Settings, contentDescription = dest.label)
                            }
                        },
                        label = { androidx.compose.material3.Text(dest.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Dest.Home.route,
            modifier = Modifier
        ) {
            composable(Dest.Home.route) { HomeScreen(contentPadding = padding) }
            composable(Dest.History.route) { HistoryScreen(contentPadding = padding) }
            composable(Dest.Settings.route) { SettingsScreen(contentPadding = padding) }
        }
    }
}
