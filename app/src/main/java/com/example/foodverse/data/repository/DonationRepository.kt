package com.example.foodverse.data.repository

import com.example.foodverse.data.model.DonationStatus
import com.example.foodverse.data.model.FoodDonation
import com.example.foodverse.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface DonationRepository {
    fun getDonations(): Flow<List<FoodDonation>>
    fun getDonationById(id: String): Flow<FoodDonation?>
    suspend fun addDonation(donation: FoodDonation): Result<String>
    suspend fun updateStatus(id: String, status: DonationStatus, claimedByName: String? = null): Result<Unit>
    suspend fun deleteDonation(id: String): Result<Unit>
    fun getUserProfile(): Flow<UserProfile>
    suspend fun updateProfile(profile: UserProfile): Result<Unit>
    fun updateUserLocation(lat: Double, lng: Double)
}
