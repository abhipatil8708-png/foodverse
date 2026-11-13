package com.example.foodverse.data.datasource

import com.example.foodverse.data.model.DonationStatus
import com.example.foodverse.data.model.FoodCategory
import com.example.foodverse.data.model.FoodDonation
import com.example.foodverse.utils.LocationHelper

object SampleDataProvider {

    fun getInitialDonations(): List<FoodDonation> {
        val userLat = LocationHelper.DEFAULT_USER_LAT
        val userLng = LocationHelper.DEFAULT_USER_LNG

        return listOf(
            FoodDonation(
                id = "don_01",
                title = "Biryani & Raita Family Packs",
                category = FoodCategory.COOKED_MEALS,
                quantity = "18 Portions",
                preparationTime = "Today, 12:30 PM",
                expiryTime = "Today, 8:00 PM",
                pickupAddress = "Golden Spice Banquet, 4th Block, Koramangala",
                donorName = "Golden Spice Restaurant",
                donorPhone = "+1 555-234-5678",
                donorRole = "Verified Restaurant",
                imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=800&auto=format&fit=crop&q=80",
                latitude = userLat + 0.0035, // ~0.4 km away
                longitude = userLng + 0.0028,
                status = DonationStatus.AVAILABLE,
                freshnessNotes = "Freshly prepared from afternoon banquet. Stored in insulated containers.",
                aiVerified = true,
                createdAtMillis = System.currentTimeMillis() - 3600_000
            ),
            FoodDonation(
                id = "don_02",
                title = "Artisan Sourdough & Croissants",
                category = FoodCategory.BAKERY,
                quantity = "25 Pieces",
                preparationTime = "Today, 7:00 AM",
                expiryTime = "Tomorrow, 10:00 AM",
                pickupAddress = "Crust & Crumb Bakery, 100ft Road, Indiranagar",
                donorName = "Crust & Crumb Bakery",
                donorPhone = "+1 555-876-5432",
                donorRole = "Artisan Bakery",
                imageUrl = "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=800&auto=format&fit=crop&q=80",
                latitude = userLat + 0.0082, // ~1.1 km away
                longitude = userLng - 0.0065,
                status = DonationStatus.AVAILABLE,
                freshnessNotes = "Baked fresh this morning. Sealed in food-grade bakery bags.",
                aiVerified = true,
                createdAtMillis = System.currentTimeMillis() - 7200_000
            ),
            FoodDonation(
                id = "don_03",
                title = "Organic Fruit Crates (Apples & Bananas)",
                category = FoodCategory.FRUITS_VEGGIES,
                quantity = "12 kg (3 Crates)",
                preparationTime = "Harvested Yesterday",
                expiryTime = "In 2 Days",
                pickupAddress = "Green Earth Supermarket, MG Road",
                donorName = "Green Earth Mart",
                donorPhone = "+1 555-345-6789",
                donorRole = "Grocery Partner",
                imageUrl = "https://images.unsplash.com/photo-1619566636858-adf3ef46400b?w=800&auto=format&fit=crop&q=80",
                latitude = userLat - 0.0120, // ~1.6 km away
                longitude = userLng + 0.0090,
                status = DonationStatus.AVAILABLE,
                freshnessNotes = "Ripened fruit, visually sound, washed and crated.",
                aiVerified = true,
                createdAtMillis = System.currentTimeMillis() - 10800_000
            ),
            FoodDonation(
                id = "don_04",
                title = "Steamed Rice & Lentil Dal",
                category = FoodCategory.COOKED_MEALS,
                quantity = "30 Portions",
                preparationTime = "Today, 1:15 PM",
                expiryTime = "Today, 7:30 PM",
                pickupAddress = "Harmony Community Kitchen, Richmond Circle",
                donorName = "Harmony Kitchen",
                donorPhone = "+1 555-456-7890",
                donorRole = "Community Kitchen",
                imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800&auto=format&fit=crop&q=80",
                latitude = userLat + 0.0150, // ~2.2 km away
                longitude = userLng - 0.0140,
                status = DonationStatus.RESERVED,
                freshnessNotes = "Warm cooked meals sealed in tamper-evident foil containers.",
                aiVerified = true,
                createdAtMillis = System.currentTimeMillis() - 5400_000,
                claimedByName = "Hope For All NGO"
            ),
            FoodDonation(
                id = "don_05",
                title = "Unopened Greek Yogurt & Cheddar",
                category = FoodCategory.DAIRY,
                quantity = "16 Packs",
                preparationTime = "Factory Sealed",
                expiryTime = "In 3 Days",
                pickupAddress = "Dairy Valley Outlet, Victoria Layout",
                donorName = "Dairy Valley Retail",
                donorPhone = "+1 555-567-8901",
                donorRole = "Retail Partner",
                imageUrl = "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=800&auto=format&fit=crop&q=80",
                latitude = userLat - 0.0190, // ~2.8 km away
                longitude = userLng - 0.0180,
                status = DonationStatus.AVAILABLE,
                freshnessNotes = "Maintained in walk-in cold storage at 3°C. Packaging intact.",
                aiVerified = false,
                createdAtMillis = System.currentTimeMillis() - 14400_000
            ),
            FoodDonation(
                id = "don_06",
                title = "Fresh Cold-Pressed Orange Juices",
                category = FoodCategory.BEVERAGES,
                quantity = "20 Bottles (300ml)",
                preparationTime = "Today, 9:00 AM",
                expiryTime = "Tomorrow, 6:00 PM",
                pickupAddress = "Pure Sip Juice Bar, Lavelle Road",
                donorName = "Pure Sip Juice Bar",
                donorPhone = "+1 555-678-9012",
                donorRole = "Beverage Partner",
                imageUrl = "https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=800&auto=format&fit=crop&q=80",
                latitude = userLat + 0.0240, // ~3.4 km away
                longitude = userLng + 0.0210,
                status = DonationStatus.AVAILABLE,
                freshnessNotes = "Chilled bottles with safety caps sealed. No preservatives added.",
                aiVerified = true,
                createdAtMillis = System.currentTimeMillis() - 18000_000
            )
        )
    }
}
