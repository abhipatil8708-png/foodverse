package com.example.foodverse.presentation.mydonations

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodverse.data.model.DonationStatus
import com.example.foodverse.data.repository.LocalDonationRepository
import com.example.foodverse.presentation.components.EmptyStateView
import com.example.foodverse.presentation.components.FoodDonationCard
import com.example.foodverse.presentation.components.FoodVerseTopBar
import com.example.foodverse.presentation.components.StatusChip
import com.example.foodverse.ui.theme.FoodVerseBackground
import com.example.foodverse.ui.theme.FoodVerseGreenDark
import com.example.foodverse.ui.theme.FoodVerseGreenLight
import com.example.foodverse.ui.theme.FoodVerseGreenPrimary
import com.example.foodverse.ui.theme.FoodVerseRed
import com.example.foodverse.ui.theme.FoodVerseTextPrimary
import com.example.foodverse.ui.theme.FoodVerseTextSecondary
import kotlinx.coroutines.launch

@Composable
fun MyDonationsScreen(
    onNavigateToDonate: () -> Unit,
    onDonationClick: (String) -> Unit
) {
    val repository = remember { LocalDonationRepository.getInstance() }
    val donations by repository.getDonations().collectAsState(initial = emptyList())
    var selectedTab by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    val activeDonations = donations.filter {
        it.status == DonationStatus.AVAILABLE || it.status == DonationStatus.RESERVED
    }
    val completedDonations = donations.filter {
        it.status == DonationStatus.COLLECTED || it.status == DonationStatus.CANCELLED
    }

    val currentList = if (selectedTab == 0) activeDonations else completedDonations

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoodVerseBackground)
            .statusBarsPadding()
    ) {
        FoodVerseTopBar(
            title = "My Food Donations",
            subtitle = "Track active and past contributions",
            actions = {
                IconButton(onClick = onNavigateToDonate) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Donation",
                        tint = FoodVerseGreenPrimary
                    )
                }
            }
        )

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = FoodVerseGreenPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = FoodVerseGreenPrimary
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "Active (${activeDonations.size})",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "History (${completedDonations.size})",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                }
            )
        }

        if (currentList.isEmpty()) {
            EmptyStateView(
                title = if (selectedTab == 0) "No Active Donations" else "No Donation History",
                message = if (selectedTab == 0) {
                    "You haven't posted any active food donations right now. Use AI to publish excess food in seconds!"
                } else {
                    "Completed and distributed food donations will appear here."
                },
                icon = Icons.Default.VolunteerActivism,
                actionButtonText = if (selectedTab == 0) "Donate Food with AI" else null,
                onActionClick = if (selectedTab == 0) onNavigateToDonate else null
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(currentList, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusChip(status = item.status)
                                Text(
                                    text = item.quantity,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = FoodVerseGreenDark
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = FoodVerseTextPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Pickup: ${item.pickupAddress}",
                                fontSize = 12.sp,
                                color = FoodVerseTextSecondary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Action buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (item.status == DonationStatus.RESERVED) {
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                repository.updateStatus(item.id, DonationStatus.COLLECTED)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = FoodVerseGreenPrimary),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("Mark Collected", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                }

                                Button(
                                    onClick = { onDonationClick(item.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = FoodVerseGreenLight,
                                        contentColor = FoodVerseGreenDark
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("View Details", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            repository.deleteDonation(item.id)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete",
                                        tint = FoodVerseRed
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
