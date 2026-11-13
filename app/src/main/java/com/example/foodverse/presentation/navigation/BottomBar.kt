package com.example.foodverse.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodverse.ui.theme.FoodVerseGreenDark
import com.example.foodverse.ui.theme.FoodVerseGreenLight
import com.example.foodverse.ui.theme.FoodVerseGreenPrimary
import com.example.foodverse.ui.theme.FoodVerseTextMuted
import com.example.foodverse.ui.theme.FoodVerseTextSecondary

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : BottomNavItem(
        Screen.Home.route,
        "Home",
        Icons.Filled.Home,
        Icons.Outlined.Home
    )
    data object Discovery : BottomNavItem(
        Screen.Discovery.route,
        "Discover",
        Icons.Filled.Explore,
        Icons.Outlined.Explore
    )
    data object MyDonations : BottomNavItem(
        Screen.MyDonations.route,
        "My Food",
        Icons.Filled.VolunteerActivism,
        Icons.Outlined.VolunteerActivism
    )
    data object Profile : BottomNavItem(
        Screen.Profile.route,
        "Profile",
        Icons.Filled.Person,
        Icons.Outlined.Person
    )
}

@Composable
fun FoodVerseBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onDonateClick: () -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Discovery,
        BottomNavItem.MyDonations,
        BottomNavItem.Profile
    )

    Box {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 8.dp
        ) {
            items.forEachIndexed { index, item ->
                // Insert spacer/gap in center for the floating donate action
                if (index == 2) {
                    NavigationBarItem(
                        selected = false,
                        onClick = onDonateClick,
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(FoodVerseGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Donate Food",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                "Donate",
                                color = FoodVerseGreenPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }

                val isSelected = currentRoute == item.route
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onNavigate(item.route) },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            item.label,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FoodVerseGreenPrimary,
                        selectedTextColor = FoodVerseGreenPrimary,
                        unselectedIconColor = FoodVerseTextMuted,
                        unselectedTextColor = FoodVerseTextSecondary,
                        indicatorColor = FoodVerseGreenLight
                    )
                )
            }
        }
    }
}
