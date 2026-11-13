package com.example.foodverse.presentation.donation

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodverse.data.datasource.GeminiFoodService
import com.example.foodverse.data.datasource.GeminiFoodServiceImpl
import com.example.foodverse.data.model.AiAnalysisResult
import com.example.foodverse.data.model.DonationStatus
import com.example.foodverse.data.model.FoodCategory
import com.example.foodverse.data.model.FoodDonation
import com.example.foodverse.data.repository.DonationRepository
import com.example.foodverse.data.repository.LocalDonationRepository
import com.example.foodverse.utils.BitmapUtils
import com.example.foodverse.utils.LocationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class AiRecognitionUiState(
    val selectedImageUri: Uri? = null,
    val isAnalyzing: Boolean = false,
    val analysisStepText: String = "",
    val analysisResult: AiAnalysisResult? = null,
    val errorMessage: String? = null
)

data class DonationFormUiState(
    val title: String = "",
    val category: FoodCategory = FoodCategory.COOKED_MEALS,
    val quantity: String = "",
    val preparationTime: String = "Today, 1:00 PM",
    val expiryTime: String = "Today, 9:00 PM",
    val pickupAddress: String = "742 Evergreen Terrace, Sector 4",
    val donorName: String = "",
    val donorPhone: String = "+1 555-019-2834",
    val freshnessNotes: String = "",
    val imageUrl: String = "",
    val isPublishing: Boolean = false,
    val publishSuccess: Boolean = false,
    val formError: String? = null
)

class DonationViewModel(
    private val geminiService: GeminiFoodService = GeminiFoodServiceImpl(),
    private val repository: DonationRepository = LocalDonationRepository.getInstance()
) : ViewModel() {

    private val _aiState = MutableStateFlow(AiRecognitionUiState())
    val aiState = _aiState.asStateFlow()

    private val _formState = MutableStateFlow(DonationFormUiState())
    val formState = _formState.asStateFlow()

    init {
        // Preload donor profile info
        viewModelScope.launch {
            val profile = repository.getUserProfile().first()
            _formState.value = _formState.value.copy(
                donorName = profile.organization.ifBlank { profile.name },
                donorPhone = profile.phone
            )
        }
    }

    fun onImageSelected(context: Context, uri: Uri) {
        _aiState.value = _aiState.value.copy(
            selectedImageUri = uri,
            analysisResult = null,
            errorMessage = null
        )
        // Automatically start AI analysis
        analyzeSelectedImage(context, uri)
    }

    fun analyzeSelectedImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            _aiState.value = _aiState.value.copy(
                isAnalyzing = true,
                analysisStepText = "Optimizing image for Gemini...",
                errorMessage = null
            )

            val bitmap: Bitmap? = BitmapUtils.decodeSampledBitmapFromUri(context, uri)

            if (bitmap == null) {
                _aiState.value = _aiState.value.copy(
                    isAnalyzing = false,
                    errorMessage = "Unable to process the selected image. Please try another photo."
                )
                return@launch
            }

            _aiState.value = _aiState.value.copy(
                analysisStepText = "Gemini AI identifying dish & freshness..."
            )

            val result = geminiService.analyzeFoodImage(bitmap)

            _aiState.value = _aiState.value.copy(
                isAnalyzing = false,
                analysisResult = result
            )

            // Pre-fill form state with AI analysis findings
            _formState.value = _formState.value.copy(
                title = result.foodName,
                category = result.category,
                quantity = result.estimatedServings,
                freshnessNotes = result.freshnessObservation,
                imageUrl = uri.toString()
            )
        }
    }

    // Form inputs
    fun onTitleChange(title: String) { _formState.value = _formState.value.copy(title = title, formError = null) }
    fun onCategoryChange(category: FoodCategory) { _formState.value = _formState.value.copy(category = category) }
    fun onQuantityChange(quantity: String) { _formState.value = _formState.value.copy(quantity = quantity, formError = null) }
    fun onPrepTimeChange(time: String) { _formState.value = _formState.value.copy(preparationTime = time) }
    fun onExpiryTimeChange(time: String) { _formState.value = _formState.value.copy(expiryTime = time, formError = null) }
    fun onPickupAddressChange(address: String) { _formState.value = _formState.value.copy(pickupAddress = address, formError = null) }
    fun onDonorNameChange(name: String) { _formState.value = _formState.value.copy(donorName = name, formError = null) }
    fun onDonorPhoneChange(phone: String) { _formState.value = _formState.value.copy(donorPhone = phone, formError = null) }
    fun onFreshnessNotesChange(notes: String) { _formState.value = _formState.value.copy(freshnessNotes = notes) }

    fun populateFromNavigationArgs(
        imageUri: String,
        foodName: String,
        categoryStr: String,
        description: String,
        servings: String,
        freshness: String
    ) {
        _formState.value = _formState.value.copy(
            imageUrl = imageUri.ifBlank { _formState.value.imageUrl },
            title = foodName.ifBlank { _formState.value.title },
            category = if (categoryStr.isNotBlank()) FoodCategory.fromString(categoryStr) else _formState.value.category,
            quantity = servings.ifBlank { _formState.value.quantity },
            freshnessNotes = freshness.ifBlank { _formState.value.freshnessNotes }
        )
    }

    fun publishDonation() {
        val state = _formState.value
        if (state.title.isBlank()) {
            _formState.value = state.copy(formError = "Please enter food title.")
            return
        }
        if (state.quantity.isBlank()) {
            _formState.value = state.copy(formError = "Please enter quantity (e.g. 10 Servings, 4 kg).")
            return
        }
        if (state.pickupAddress.isBlank()) {
            _formState.value = state.copy(formError = "Please provide pickup address.")
            return
        }
        if (state.donorPhone.isBlank()) {
            _formState.value = state.copy(formError = "Please provide contact phone number.")
            return
        }

        _formState.value = state.copy(isPublishing = true, formError = null)

        viewModelScope.launch {
            delay(800) // Simulated network publish delay

            val donation = FoodDonation(
                title = state.title.trim(),
                category = state.category,
                quantity = state.quantity.trim(),
                preparationTime = state.preparationTime.trim(),
                expiryTime = state.expiryTime.trim(),
                pickupAddress = state.pickupAddress.trim(),
                donorName = state.donorName.ifBlank { "Generous Food Donor" },
                donorPhone = state.donorPhone.trim(),
                imageUrl = state.imageUrl.ifBlank {
                    "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800&auto=format&fit=crop&q=80"
                },
                latitude = LocationHelper.DEFAULT_USER_LAT + (Math.random() - 0.5) * 0.015,
                longitude = LocationHelper.DEFAULT_USER_LNG + (Math.random() - 0.5) * 0.015,
                status = DonationStatus.AVAILABLE,
                freshnessNotes = state.freshnessNotes,
                aiVerified = true,
                createdAtMillis = System.currentTimeMillis()
            )

            repository.addDonation(donation)

            _formState.value = _formState.value.copy(
                isPublishing = false,
                publishSuccess = true
            )
        }
    }

    fun resetPublishSuccess() {
        _formState.value = _formState.value.copy(publishSuccess = false)
    }
}
