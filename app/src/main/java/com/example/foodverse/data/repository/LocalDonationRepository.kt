package com.example.foodverse.data.repository

import com.example.foodverse.data.datasource.SampleDataProvider
import com.example.foodverse.data.model.DonationStatus
import com.example.foodverse.data.model.FoodDonation
import com.example.foodverse.data.model.UserProfile
import com.example.foodverse.utils.LocationHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class LocalDonationRepository private constructor() : DonationRepository {

    private var currentUserLat = LocationHelper.DEFAULT_USER_LAT
    private var currentUserLng = LocationHelper.DEFAULT_USER_LNG

    private val _donations = MutableStateFlow<List<FoodDonation>>(emptyList())
    private val _userProfile = MutableStateFlow(UserProfile())

    init {
        // Seed with sample data annotated with initial distance
        val initialList = SampleDataProvider.getInitialDonations().map { donation ->
            val dist = LocationHelper.calculateDistanceKm(
                currentUserLat, currentUserLng,
                donation.latitude, donation.longitude
            )
            donation.copy(distanceKm = dist)
        }
        _donations.value = initialList
    }

    override fun getDonations(): Flow<List<FoodDonation>> {
        return _donations.map { list ->
            list.map { donation ->
                val dist = LocationHelper.calculateDistanceKm(
                    currentUserLat, currentUserLng,
                    donation.latitude, donation.longitude
                )
                donation.copy(distanceKm = dist)
            }.sortedBy { it.distanceKm ?: Double.MAX_VALUE }
        }
    }

    override fun getDonationById(id: String): Flow<FoodDonation?> {
        return _donations.map { list ->
            list.find { it.id == id }?.let { donation ->
                val dist = LocationHelper.calculateDistanceKm(
                    currentUserLat, currentUserLng,
                    donation.latitude, donation.longitude
                )
                donation.copy(distanceKm = dist)
            }
        }
    }

    override suspend fun addDonation(donation: FoodDonation): Result<String> {
        val dist = LocationHelper.calculateDistanceKm(
            currentUserLat, currentUserLng,
            donation.latitude, donation.longitude
        )
        val newDonation = donation.copy(distanceKm = dist)
        val currentList = _donations.value.toMutableList()
        currentList.add(0, newDonation)
        _donations.value = currentList

        // Increment user impact stats
        val currentProfile = _userProfile.value
        _userProfile.value = currentProfile.copy(
            donationsCount = currentProfile.donationsCount + 1,
            mealsSaved = currentProfile.mealsSaved + (extractServingsCount(donation.quantity)),
            impactPoints = currentProfile.impactPoints + 50
        )

        return Result.success(newDonation.id)
    }

    override suspend fun updateStatus(
        id: String,
        status: DonationStatus,
        claimedByName: String?
    ): Result<Unit> {
        val currentList = _donations.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            val existing = currentList[index]
            currentList[index] = existing.copy(
                status = status,
                claimedByName = claimedByName ?: existing.claimedByName,
                claimedAtMillis = if (status == DonationStatus.RESERVED || status == DonationStatus.COLLECTED) System.currentTimeMillis() else existing.claimedAtMillis
            )
            _donations.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(IllegalArgumentException("Donation not found with id: $id"))
    }

    override suspend fun deleteDonation(id: String): Result<Unit> {
        val currentList = _donations.value.toMutableList()
        val removed = currentList.removeAll { it.id == id }
        if (removed) {
            _donations.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(IllegalArgumentException("Donation not found"))
    }

    override fun getUserProfile(): Flow<UserProfile> = _userProfile.asStateFlow()

    override suspend fun updateProfile(profile: UserProfile): Result<Unit> {
        _userProfile.value = profile
        return Result.success(Unit)
    }

    override fun updateUserLocation(lat: Double, lng: Double) {
        currentUserLat = lat
        currentUserLng = lng
        // Trigger re-calculation
        _donations.value = _donations.value.toList()
    }

    private fun extractServingsCount(quantityStr: String): Int {
        val digits = quantityStr.filter { it.isDigit() }
        return digits.toIntOrNull() ?: 5
    }

    companion object {
        @Volatile
        private var instance: LocalDonationRepository? = null

        fun getInstance(): LocalDonationRepository {
            return instance ?: synchronized(this) {
                instance ?: LocalDonationRepository().also { instance = it }
            }
        }
    }
}
