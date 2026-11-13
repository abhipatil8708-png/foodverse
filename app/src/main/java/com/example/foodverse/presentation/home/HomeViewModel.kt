package com.example.foodverse.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodverse.data.model.DonationStatus
import com.example.foodverse.data.model.FoodDonation
import com.example.foodverse.data.model.UserProfile
import com.example.foodverse.data.repository.DonationRepository
import com.example.foodverse.data.repository.LocalDonationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val userProfile: UserProfile = UserProfile(),
    val nearbyDonations: List<FoodDonation> = emptyList(),
    val urgentDonations: List<FoodDonation> = emptyList(),
    val totalAvailableCount: Int = 0,
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val repository: DonationRepository = LocalDonationRepository.getInstance()
) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    val donations: StateFlow<List<FoodDonation>> = repository.getDonations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableDonations: StateFlow<List<FoodDonation>> = donations.map { list ->
        list.filter { it.status == DonationStatus.AVAILABLE }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
