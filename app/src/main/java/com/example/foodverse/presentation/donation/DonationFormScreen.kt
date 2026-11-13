package com.example.foodverse.presentation.donation

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodverse.data.model.FoodCategory
import com.example.foodverse.presentation.components.CategoryFilterRow
import com.example.foodverse.presentation.components.FoodSafetyNoticeCard
import com.example.foodverse.presentation.components.FoodVersePrimaryButton
import com.example.foodverse.presentation.components.FoodVerseTopBar
import com.example.foodverse.ui.theme.FoodVerseBackground
import com.example.foodverse.ui.theme.FoodVerseGreenDark
import com.example.foodverse.ui.theme.FoodVerseGreenLight
import com.example.foodverse.ui.theme.FoodVerseGreenPrimary
import com.example.foodverse.ui.theme.FoodVerseRed
import com.example.foodverse.ui.theme.FoodVerseTextPrimary
import com.example.foodverse.ui.theme.FoodVerseTextSecondary

@Composable
fun DonationFormScreen(
    onNavigateBack: () -> Unit,
    onPublishSuccess: () -> Unit,
    viewModel: DonationViewModel
) {
    val state by viewModel.formState.collectAsState()
    var showSuccessDialog by remember { mutableStateOf(false) }

    if (state.publishSuccess) {
        showSuccessDialog = true
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.resetPublishSuccess()
                showSuccessDialog = false
                onPublishSuccess()
            },
            icon = {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(FoodVerseGreenLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = FoodVerseGreenPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Donation Published!",
                    fontWeight = FontWeight.Bold,
                    color = FoodVerseTextPrimary,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Your food donation is now live on FoodVerse. Nearby NGOs and community volunteers can view the details and arrange pickup.",
                    color = FoodVerseTextSecondary,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                FoodVersePrimaryButton(
                    text = "View in My Donations",
                    onClick = {
                        viewModel.resetPublishSuccess()
                        showSuccessDialog = false
                        onPublishSuccess()
                    }
                )
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoodVerseBackground)
    ) {
        FoodVerseTopBar(
            title = "Food Donation Details",
            subtitle = "Step 2 of 2: Review and Publish",
            onBackClick = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Food Thumbnail Preview
            if (state.imageUrl.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = state.imageUrl,
                            contentDescription = "Food Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(FoodVerseGreenDark.copy(alpha = 0.85f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "✨ Gemini AI Pre-filled",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Error Notice
            if (state.formError != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFEE2E2))
                        .padding(12.dp)
                ) {
                    Text(
                        text = state.formError ?: "",
                        color = FoodVerseRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Food Title Field
            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Food Title / Dish Name *") },
                leadingIcon = { Icon(Icons.Default.Fastfood, contentDescription = null, tint = FoodVerseGreenPrimary) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FoodVerseGreenPrimary,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Food Category Selection
            Text(
                text = "Food Category *",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = FoodVerseTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            CategoryFilterRow(
                selectedCategory = state.category,
                onCategorySelected = viewModel::onCategoryChange
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quantity Field
            OutlinedTextField(
                value = state.quantity,
                onValueChange = viewModel::onQuantityChange,
                label = { Text("Quantity / Servings * (e.g., 15 Servings, 5 kg)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FoodVerseGreenPrimary,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Prep Time & Expiry Time Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = state.preparationTime,
                    onValueChange = viewModel::onPrepTimeChange,
                    label = { Text("Prepared At") },
                    leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null, tint = FoodVerseGreenPrimary) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FoodVerseGreenPrimary,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.expiryTime,
                    onValueChange = viewModel::onExpiryTimeChange,
                    label = { Text("Use-by Time *") },
                    leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null, tint = FoodVerseRed) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FoodVerseGreenPrimary,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pickup Address Field
            OutlinedTextField(
                value = state.pickupAddress,
                onValueChange = viewModel::onPickupAddressChange,
                label = { Text("Pickup Location / Address *") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = FoodVerseGreenPrimary) },
                trailingIcon = {
                    IconButton(onClick = { viewModel.onPickupAddressChange("42 Green Valley Park, Koramangala 5th Block") }) {
                        Icon(Icons.Default.MyLocation, contentDescription = "Use GPS Location", tint = FoodVerseGreenDark)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FoodVerseGreenPrimary,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Contact Name & Phone Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = state.donorName,
                    onValueChange = viewModel::onDonorNameChange,
                    label = { Text("Donor Name *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = FoodVerseGreenPrimary) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FoodVerseGreenPrimary,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.donorPhone,
                    onValueChange = viewModel::onDonorPhoneChange,
                    label = { Text("Phone *") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = FoodVerseGreenPrimary) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FoodVerseGreenPrimary,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Freshness Observation / Notes Field
            OutlinedTextField(
                value = state.freshnessNotes,
                onValueChange = viewModel::onFreshnessNotesChange,
                label = { Text("Freshness & Storage Instructions") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FoodVerseGreenPrimary,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Food Safety Warning
            FoodSafetyNoticeCard()

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            FoodVersePrimaryButton(
                text = "Publish Food Donation",
                leadingIcon = Icons.Default.VolunteerActivism,
                onClick = viewModel::publishDonation,
                isLoading = state.isPublishing
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
