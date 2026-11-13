package com.example.foodverse

import com.example.foodverse.data.model.DonationStatus
import com.example.foodverse.data.model.FoodCategory
import com.example.foodverse.data.model.FoodDonation
import com.example.foodverse.data.repository.LocalDonationRepository
import com.example.foodverse.utils.LocationHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DonationRepositoryTest {

    private lateinit var repository: LocalDonationRepository

    @Before
    fun setUp() {
        repository = LocalDonationRepository.getInstance()
    }

    @Test
    fun getDonations_returnsInitialSampleData() = runBlocking {
        val donations = repository.getDonations().first()
        assertTrue("Donations list should not be empty", donations.isNotEmpty())
        assertTrue("Should have multiple initial donations", donations.size >= 5)
    }

    @Test
    fun addDonation_addsItemAndUpdatesUserImpact() = runBlocking {
        val initialDonations = repository.getDonations().first()
        val initialProfile = repository.getUserProfile().first()

        val newDonation = FoodDonation(
            title = "Fresh Organic Salads",
            category = FoodCategory.FRUITS_VEGGIES,
            quantity = "10 Servings",
            preparationTime = "Today, 2:00 PM",
            expiryTime = "Today, 9:00 PM",
            pickupAddress = "100ft Road, Indiranagar",
            donorName = "Green Leaf Cafe",
            donorPhone = "+1 555-999-8888"
        )

        val result = repository.addDonation(newDonation)
        assertTrue("Add donation result should be success", result.isSuccess)

        val updatedDonations = repository.getDonations().first()
        assertEquals(initialDonations.size + 1, updatedDonations.size)

        val updatedProfile = repository.getUserProfile().first()
        assertEquals(initialProfile.donationsCount + 1, updatedProfile.donationsCount)
        assertTrue(updatedProfile.mealsSaved >= initialProfile.mealsSaved + 10)
    }

    @Test
    fun updateStatus_changesStatusToReservedAndCollected() = runBlocking {
        val donations = repository.getDonations().first()
        val target = donations.first()

        val reserveResult = repository.updateStatus(target.id, DonationStatus.RESERVED, "Local Shelter NGO")
        assertTrue(reserveResult.isSuccess)

        val reservedItem = repository.getDonationById(target.id).first()
        assertNotNull(reservedItem)
        assertEquals(DonationStatus.RESERVED, reservedItem?.status)
        assertEquals("Local Shelter NGO", reservedItem?.claimedByName)

        val collectResult = repository.updateStatus(target.id, DonationStatus.COLLECTED)
        assertTrue(collectResult.isSuccess)

        val collectedItem = repository.getDonationById(target.id).first()
        assertEquals(DonationStatus.COLLECTED, collectedItem?.status)
    }

    @Test
    fun locationHelper_calculatesDistanceAccurately() {
        // Distance between Bangalore center (12.9716, 77.5946) and Indiranagar (12.9784, 77.6408) is ~5 km
        val distance = LocationHelper.calculateDistanceKm(
            12.9716, 77.5946,
            12.9784, 77.6408
        )
        assertTrue("Distance should be around 5.0 km", distance in 4.0..6.5)

        val formattedKm = LocationHelper.formatDistance(3.42)
        assertEquals("3.4 km away", formattedKm)

        val formattedMeters = LocationHelper.formatDistance(0.45)
        assertEquals("450m away", formattedMeters)
    }

    @Test
    fun foodCategory_parsesVariousStringsCorrectly() {
        assertEquals(FoodCategory.COOKED_MEALS, FoodCategory.fromString("COOKED_MEALS"))
        assertEquals(FoodCategory.BAKERY, FoodCategory.fromString("artisan bread & bakery"))
        assertEquals(FoodCategory.FRUITS_VEGGIES, FoodCategory.fromString("fresh vegetables"))
        assertEquals(FoodCategory.DAIRY, FoodCategory.fromString("milk & cheese"))
        assertEquals(FoodCategory.BEVERAGES, FoodCategory.fromString("cold drinks"))
        assertEquals(FoodCategory.CANNED_GOODS, FoodCategory.fromString("canned beans"))
    }
}
