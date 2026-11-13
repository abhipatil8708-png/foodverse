package com.example.foodverse.presentation.discovery

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodverse.data.model.DonationStatus
import com.example.foodverse.presentation.components.FoodSafetyNoticeCard
import com.example.foodverse.presentation.components.FoodVersePrimaryButton
import com.example.foodverse.presentation.components.FoodVerseSecondaryButton
import com.example.foodverse.presentation.components.StatusChip
import com.example.foodverse.ui.theme.FoodVerseBackground
import com.example.foodverse.ui.theme.FoodVerseGreenDark
import com.example.foodverse.ui.theme.FoodVerseGreenLight
import com.example.foodverse.ui.theme.FoodVerseGreenPrimary
import com.example.foodverse.ui.theme.FoodVerseRed
import com.example.foodverse.ui.theme.FoodVerseSurfaceVariant
import com.example.foodverse.ui.theme.FoodVerseTextMuted
import com.example.foodverse.ui.theme.FoodVerseTextPrimary
import com.example.foodverse.ui.theme.FoodVerseTextSecondary
import com.example.foodverse.ui.theme.FoodVerseWarmOrange
import com.example.foodverse.utils.LocationHelper

@Composable
fun DonationDetailScreen(
    donationId: String,
    onNavigateBack: () -> Unit,
    viewModel: DiscoveryViewModel
) {
    val context = LocalContext.current
    val donationFlow = remember(donationId) { viewModel.getDonationById(donationId) }
    val donation by donationFlow.collectAsState(initial = null)
    var showReserveConfirmation by remember { mutableStateOf(false) }

    if (donation == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FoodVerseBackground),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading donation details...", color = FoodVerseTextSecondary)
        }
        return
    }

    val item = donation!!

    // Reservation Confirmation Dialog
    if (showReserveConfirmation) {
        AlertDialog(
            onDismissRequest = { showReserveConfirmation = false },
            icon = {
                Icon(
                    Icons.Default.VolunteerActivism,
                    contentDescription = null,
                    tint = FoodVerseGreenPrimary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Confirm Reservation",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to reserve ${item.title} (${item.quantity})? Please make sure you can pick it up before the expiry time (${item.expiryTime}).",
                    fontSize = 13.5.sp,
                    color = FoodVerseTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.requestFoodReservation(item.id)
                        showReserveConfirmation = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FoodVerseGreenPrimary)
                ) {
                    Text("Yes, Reserve Food", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showReserveConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoodVerseBackground)
    ) {
        // Image Header with Back Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            AsyncImage(
                model = item.imageUrl.ifBlank { "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800" },
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Back button
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(12.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            // Distance Pill
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = LocationHelper.formatDistance(item.distanceKm),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Status Pill
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                StatusChip(status = item.status)
            }
        }

        // Details Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Category & Quantity Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.category.emoji} ${item.category.displayName}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = FoodVerseGreenPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FoodVerseGreenLight)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = item.quantity,
                        color = FoodVerseGreenDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Food Title
            Text(
                text = item.title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = FoodVerseTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // AI Verified Badge Card
            if (item.aiVerified) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFECFDF5))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = FoodVerseGreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Gemini AI Recognized Dish",
                                fontWeight = FontWeight.Bold,
                                color = FoodVerseGreenDark,
                                fontSize = 12.5.sp
                            )
                            if (item.freshnessNotes.isNotBlank()) {
                                Text(
                                    text = item.freshnessNotes,
                                    color = Color(0xFF065F46),
                                    fontSize = 11.5.sp
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Food Safety Notice
            FoodSafetyNoticeCard()

            Spacer(modifier = Modifier.height(16.dp))

            // Time & Expiry Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Prepared", color = FoodVerseTextMuted, fontSize = 11.sp)
                            Text(item.preparationTime, fontWeight = FontWeight.SemiBold, color = FoodVerseTextPrimary, fontSize = 13.sp)
                        }
                        Column {
                            Text("Use-by Time", color = FoodVerseRed, fontSize = 11.sp)
                            Text(item.expiryTime, fontWeight = FontWeight.Bold, color = FoodVerseRed, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pickup Location Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = FoodVerseGreenPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pickup Address", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = FoodVerseTextPrimary)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(item.pickupAddress, color = FoodVerseTextSecondary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Open in Google Maps Intent Button
                    OutlinedButton(
                        onClick = {
                            val mapUri = Uri.parse("geo:${item.latitude},${item.longitude}?q=${Uri.encode(item.pickupAddress)}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                            context.startActivity(mapIntent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Navigate with Google Maps", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Donor Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(FoodVerseGreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Restaurant, contentDescription = null, tint = FoodVerseGreenDark, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(item.donorName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FoodVerseTextPrimary)
                                Text(item.donorRole, fontSize = 11.5.sp, color = FoodVerseTextSecondary)
                            }
                        }

                        // Call Donor Button
                        IconButton(
                            onClick = {
                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.donorPhone}"))
                                context.startActivity(dialIntent)
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(FoodVerseGreenPrimary)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call Donor", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action: Request Food / Status Actions
            when (item.status) {
                DonationStatus.AVAILABLE -> {
                    FoodVersePrimaryButton(
                        text = "Request & Reserve This Food",
                        leadingIcon = Icons.Default.VolunteerActivism,
                        onClick = { showReserveConfirmation = true }
                    )
                }
                DonationStatus.RESERVED -> {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "This food is currently RESERVED for collection. If you are the designated volunteer, you can mark it as collected after pickup.",
                                color = Color(0xFF92400E),
                                fontSize = 12.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        FoodVersePrimaryButton(
                            text = "Mark as Successfully Collected",
                            leadingIcon = Icons.Default.CheckCircle,
                            onClick = { viewModel.markFoodCollected(item.id) }
                        )
                    }
                }
                DonationStatus.COLLECTED -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(FoodVerseSurfaceVariant)
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✅ This donation has been collected and distributed.",
                            color = FoodVerseTextSecondary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
                DonationStatus.CANCELLED -> {
                    Text("This donation is no longer active.", color = FoodVerseRed)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
