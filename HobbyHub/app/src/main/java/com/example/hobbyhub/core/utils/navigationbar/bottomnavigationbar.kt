package com.example.hobbyhub.core.utils.navigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.hobbyhub.core.navigations.Screen
import com.example.hobbyhub.ui.theme.EventHubPrimary

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Explore,
        BottomNavItem.Groups,
        BottomNavItem.Map,
        BottomNavItem.Workshops,
        BottomNavItem.Profile
    )
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = EventHubPrimary,
                    selectedTextColor = EventHubPrimary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

sealed class BottomNavItem(var title: String, var icon: ImageVector, var screen: Screen) {
    object Explore : BottomNavItem("Explore", Icons.Default.Explore, Screen.ExploreScreen)
    object Groups : BottomNavItem("Groups", Icons.Default.Groups, Screen.GroupsScreen)
    object Map : BottomNavItem("Map", Icons.Default.Map, Screen.MapScreen)
    object Workshops : BottomNavItem("Workshops", Icons.Default.CalendarToday, Screen.WorkshopsScreen)
    object Profile : BottomNavItem("Profile", Icons.Default.Person, Screen.ProfileScreen)
}