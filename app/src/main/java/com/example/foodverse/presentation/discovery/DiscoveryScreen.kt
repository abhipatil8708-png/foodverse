package com.example.foodverse.presentation.discovery

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodverse.data.model.FoodCategory
import com.example.foodverse.presentation.components.CategoryFilterRow
import com.example.foodverse.presentation.components.EmptyStateView
import com.example.foodverse.presentation.components.FoodDonationCard
import com.example.foodverse.presentation.components.FoodVerseTopBar
import com.example.foodverse.ui.theme.FoodVerseBackground
import com.example.foodverse.ui.theme.FoodVerseGreenDark
import com.example.foodverse.ui.theme.FoodVerseGreenLight
import com.example.foodverse.ui.theme.FoodVerseGreenPrimary
import com.example.foodverse.ui.theme.FoodVerseSurfaceVariant
import com.example.foodverse.ui.theme.FoodVerseTextMuted
import com.example.foodverse.ui.theme.FoodVerseTextPrimary
import com.example.foodverse.ui.theme.FoodVerseTextSecondary
import com.example.foodverse.ui.theme.FoodVerseWarmOrange
import com.example.foodverse.ui.theme.FoodVerseWarmOrangeLight

@Composable
fun DiscoveryScreen(
    onDonationClick: (String) -> Unit,
    viewModel: DiscoveryViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val donations by viewModel.filteredDonations.collectAsState()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        viewModel.setLocationPermissionGranted(fineLocation || coarseLocation)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoodVerseBackground)
            .statusBarsPadding()
    ) {
        // Header
        FoodVerseTopBar(
            title = "Nearby Food Discovery",
            subtitle = "Find and rescue surplus food in your vicinity"
        )

        // Search Bar
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                placeholder = { Text("Search by food, cuisine, or street...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = FoodVerseGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (state.searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = FoodVerseTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FoodVerseGreenPrimary,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )
        }

        // Horizontal Category Filter Row
        CategoryFilterRow(
            selectedCategory = state.selectedCategory,
            onCategorySelected = viewModel::onCategorySelected
        )

        // Location & Filter Status Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Location Badge / Permission Graceful Handling
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (state.isLocationPermissionGranted) FoodVerseGreenLight else FoodVerseWarmOrangeLight)
                    .clickable {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = if (state.isLocationPermissionGranted) Icons.Default.LocationOn else Icons.Default.LocationOff,
                    contentDescription = null,
                    tint = if (state.isLocationPermissionGranted) FoodVerseGreenDark else FoodVerseWarmOrange,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (state.isLocationPermissionGranted) "Downtown (GPS Active)" else "Downtown (Tap to enable GPS)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (state.isLocationPermissionGranted) FoodVerseGreenDark else FoodVerseWarmOrange
                )
            }

            // Available Only Toggle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Available only",
                    fontSize = 12.sp,
                    color = FoodVerseTextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Switch(
                    checked = state.showOnlyAvailable,
                    onCheckedChange = viewModel::onToggleOnlyAvailable,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = FoodVerseGreenPrimary
                    ),
                    modifier = Modifier.size(width = 38.dp, height = 24.dp)
                )
            }
        }

        // Donations List or Empty State
        if (donations.isEmpty()) {
            EmptyStateView(
                title = "No Food Donations Found",
                message = if (state.searchQuery.isNotBlank() || state.selectedCategory != FoodCategory.ALL) {
                    "No listings matched your search criteria. Try selecting another category or clearing filters."
                } else {
                    "There are currently no food donations available in this area. Check back soon or donate surplus food!"
                },
                actionButtonText = "Reset All Filters",
                onActionClick = {
                    viewModel.onSearchQueryChanged("")
                    viewModel.onCategorySelected(FoodCategory.ALL)
                    viewModel.onToggleOnlyAvailable(false)
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(donations, key = { it.id }) { donation ->
                    FoodDonationCard(
                        donation = donation,
                        onClick = { onDonationClick(donation.id) }
                    )
                }
            }
        }
    }
}
