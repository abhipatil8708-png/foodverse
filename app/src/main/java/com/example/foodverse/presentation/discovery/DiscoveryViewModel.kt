package com.example.foodverse.presentation.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodverse.data.model.DonationStatus
import com.example.foodverse.data.model.FoodCategory
import com.example.foodverse.data.model.FoodDonation
import com.example.foodverse.data.repository.DonationRepository
import com.example.foodverse.data.repository.LocalDonationRepository
import com.example.foodverse.utils.LocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DiscoveryUiState(
    val searchQuery: String = "",
    val selectedCategory: FoodCategory = FoodCategory.ALL,
    val showOnlyAvailable: Boolean = false,
    val isLocationPermissionGranted: Boolean = true,
    val userLat: Double = LocationHelper.DEFAULT_USER_LAT,
    val userLng: Double = LocationHelper.DEFAULT_USER_LNG,
    val isReserving: Boolean = false,
    val reserveSuccessMessage: String? = null
)

class DiscoveryViewModel(
    private val repository: DonationRepository = LocalDonationRepository.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState: StateFlow<DiscoveryUiState> = _uiState

    val allDonations: StateFlow<List<FoodDonation>> = repository.getDonations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredDonations: StateFlow<List<FoodDonation>> = combine(
        allDonations,
        _uiState
    ) { donations, state ->
        donations.filter { donation ->
            val matchesCategory = state.selectedCategory == FoodCategory.ALL ||
                    donation.category == state.selectedCategory

            val matchesSearch = state.searchQuery.isBlank() ||
                    donation.title.contains(state.searchQuery, ignoreCase = true) ||
                    donation.pickupAddress.contains(state.searchQuery, ignoreCase = true) ||
                    donation.donorName.contains(state.searchQuery, ignoreCase = true)

            val matchesAvailability = !state.showOnlyAvailable ||
                    donation.status == DonationStatus.AVAILABLE

            matchesCategory && matchesSearch && matchesAvailability
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onCategorySelected(category: FoodCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onToggleOnlyAvailable(onlyAvailable: Boolean) {
        _uiState.value = _uiState.value.copy(showOnlyAvailable = onlyAvailable)
    }

    fun setLocationPermissionGranted(granted: Boolean) {
        _uiState.value = _uiState.value.copy(isLocationPermissionGranted = granted)
    }

    fun updateUserLocation(lat: Double, lng: Double) {
        _uiState.value = _uiState.value.copy(userLat = lat, userLng = lng)
        repository.updateUserLocation(lat, lng)
    }

    fun getDonationById(id: String) = repository.getDonationById(id)

    fun requestFoodReservation(donationId: String, recipientName: String = "Community Beneficiary") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isReserving = true)
            repository.updateStatus(donationId, DonationStatus.RESERVED, recipientName)
            _uiState.value = _uiState.value.copy(
                isReserving = false,
                reserveSuccessMessage = "Food has been successfully reserved! Please contact the donor for pickup."
            )
        }
    }

    fun markFoodCollected(donationId: String) {
        viewModelScope.launch {
            repository.updateStatus(donationId, DonationStatus.COLLECTED)
        }
    }

    fun clearReservationMessage() {
        _uiState.value = _uiState.value.copy(reserveSuccessMessage = null)
    }
}
