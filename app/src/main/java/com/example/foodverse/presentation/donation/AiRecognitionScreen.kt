package com.example.foodverse.presentation.donation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodverse.presentation.components.FoodSafetyNoticeCard
import com.example.foodverse.presentation.components.FoodVersePrimaryButton
import com.example.foodverse.presentation.components.FoodVerseTopBar
import com.example.foodverse.ui.theme.FoodVerseBackground
import com.example.foodverse.ui.theme.FoodVerseGreenDark
import com.example.foodverse.ui.theme.FoodVerseGreenLight
import com.example.foodverse.ui.theme.FoodVerseGreenPrimary
import com.example.foodverse.ui.theme.FoodVerseSurfaceVariant
import com.example.foodverse.ui.theme.FoodVerseTextPrimary
import com.example.foodverse.ui.theme.FoodVerseTextSecondary

@Composable
fun AiRecognitionScreen(
    onNavigateBack: () -> Unit,
    onContinueToForm: (imageUri: String, foodName: String, category: String, description: String, servings: String, freshness: String) -> Unit,
    viewModel: DonationViewModel
) {
    val context = LocalContext.current
    val state by viewModel.aiState.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onImageSelected(context, uri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoodVerseBackground)
    ) {
        FoodVerseTopBar(
            title = "AI Food Recognition",
            subtitle = "Powered by Google Gemini Multimodal Vision",
            onBackClick = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Image Upload / Preview Area
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { galleryLauncher.launch("image/*") },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                if (state.selectedImageUri != null) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = state.selectedImageUri,
                            contentDescription = "Selected Food",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Retake / Change overlay pill
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(alpha = 0.65f))
                                .clickable { galleryLauncher.launch("image/*") }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Change Photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Change Photo", color = Color.White, fontSize = 11.sp)
                            }
                        }
                    }
                } else {
                    // Empty Upload Placeholder
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(FoodVerseGreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera",
                                tint = FoodVerseGreenPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Upload or Capture Food Photo",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = FoodVerseTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Tap to choose a photo from your gallery or camera",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = FoodVerseTextSecondary,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }

            // Quick emulator test samples
            if (state.selectedImageUri == null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Or choose a quick demo dish to analyze:",
                    style = MaterialTheme.typography.labelMedium.copy(color = FoodVerseTextSecondary)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val demoSamples = listOf(
                        "🍛 Rice & Curry" to "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800",
                        "🥐 Bakery Breads" to "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=800",
                        "🥗 Fresh Fruit" to "https://images.unsplash.com/photo-1619566636858-adf3ef46400b?w=800"
                    )
                    demoSamples.forEach { (label, url) ->
                        OutlinedButton(
                            onClick = {
                                viewModel.onImageSelected(context, Uri.parse(url))
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(label, fontSize = 11.sp, maxLines = 1)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // AI Processing Indicator
            if (state.isAnalyzing) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = FoodVerseGreenPrimary,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.analysisStepText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = FoodVerseTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Extracting dish type, category, and visual freshness...",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = FoodVerseTextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // AI Result Card
            val result = state.analysisResult
            if (result != null && !state.isAnalyzing) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = FoodVerseGreenPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "AI Recognition Result",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = FoodVerseGreenDark
                                    )
                                )
                            }

                            // Category Chip
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(FoodVerseGreenLight)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${result.category.emoji} ${result.category.displayName}",
                                    color = FoodVerseGreenDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Food Name
                        Text(
                            text = result.foodName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = FoodVerseTextPrimary,
                                fontSize = 19.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Description
                        Text(
                            text = result.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = FoodVerseTextSecondary,
                                fontSize = 13.5.sp,
                                lineHeight = 19.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Freshness Observation Row
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(FoodVerseSurfaceVariant)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "🔍 Visual Freshness Observation:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = FoodVerseGreenDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = result.freshnessObservation,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = FoodVerseTextPrimary,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Estimated Servings
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Estimated Quantity: ",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = FoodVerseTextSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                text = result.estimatedServings,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = FoodVerseGreenPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Strict Food Safety Notice Card
                FoodSafetyNoticeCard(
                    message = result.disclaimer
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Continue to Form Button
                FoodVersePrimaryButton(
                    text = "Confirm Details & Set Pickup",
                    leadingIcon = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = {
                        val imageStr = state.selectedImageUri?.toString() ?: ""
                        onContinueToForm(
                            imageStr,
                            result.foodName,
                            result.category.name,
                            result.description,
                            result.estimatedServings,
                            result.freshnessObservation
                        )
                    }
                )
            }
        }
    }
}
