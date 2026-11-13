package com.example.foodverse.data.model

import java.util.UUID

data class FoodDonation(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: FoodCategory,
    val quantity: String,
    val preparationTime: String,
    val expiryTime: String,
    val pickupAddress: String,
    val donorName: String,
    val donorPhone: String,
    val donorRole: String = "Food Donor",
    val imageUrl: String = "",
    val latitude: Double = 12.9716, // Default to a standard city coordinate
    val longitude: Double = 77.5946,
    val distanceKm: Double? = null,
    val status: DonationStatus = DonationStatus.AVAILABLE,
    val freshnessNotes: String = "",
    val aiVerified: Boolean = false,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val claimedByName: String? = null,
    val claimedAtMillis: Long? = null
)
