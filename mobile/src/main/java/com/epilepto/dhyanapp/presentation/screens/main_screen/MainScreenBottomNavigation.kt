package com.epilepto.dhyanapp.presentation.screens.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class Destinations(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    data object HomeScreen : Destinations(
        route = "home_screen",
        title = "Home",
        icon = Icons.Outlined.Home
    )

    data object Favourite : Destinations(
        route = "meditation_screen",
        title = "Meditation",
        icon = Icons.Outlined.FavoriteBorder
    )

    data object Notification : Destinations(
        route = "pranayam_screen",
        title = "Pranayam",
        icon = Icons.Outlined.Notifications
    )

}

@Composable
fun MainScreenBottomNavigation(
    navController: NavHostController
) {
    val screens = listOf(
        Destinations.HomeScreen, Destinations.Favourite, Destinations.Notification
    )

    Box(
        modifier = Modifier.fillMaxWidth()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Black),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
            //  containerColor = Color.Black,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            screens.forEach { screen ->
                BottomNavItem(
                    isSelected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = "",
                            tint = if (currentRoute == screen.route) {
                                Color.Black
                            } else Color.White,
                            modifier = Modifier
                                .scale(1.3f)
                                .padding(20.dp)
                        )
                    }
                )
                /*            NavigationBarItem(
                                label = null,
                                icon = {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = "",
                                        tint = if (currentRoute == screen.route) {
                                            Color.Black
                                        } else Color.White
                                    )
                                },
                                selected = currentRoute == screen.route,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    unselectedTextColor = Color.Gray, selectedTextColor = Color.White
                                ),
                                alwaysShowLabel = false
                            )*/
            }
//            Spacer(modifier = Modifier.width(20.dp))
        }
    }

}

@Composable
fun BottomNavItem(
    isSelected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) Color.White
                else Color.Black
            )
            .clickable { onClick() }
    ) {
        icon()
    }
}
