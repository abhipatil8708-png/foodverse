package com.example.foodverse.presentation.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Auth : Screen("auth")
    data object Home : Screen("home")
    data object AiRecognition : Screen("ai_recognition")
    data object DonationForm : Screen(
        "donation_form?imageUri={imageUri}&foodName={foodName}&category={category}&description={description}&servings={servings}&freshness={freshness}"
    ) {
        fun createRoute(
            imageUri: String = "",
            foodName: String = "",
            category: String = "",
            description: String = "",
            servings: String = "",
            freshness: String = ""
        ): String {
            val encUri = Uri.encode(imageUri)
            val encName = Uri.encode(foodName)
            val encCat = Uri.encode(category)
            val encDesc = Uri.encode(description)
            val encServ = Uri.encode(servings)
            val encFresh = Uri.encode(freshness)
            return "donation_form?imageUri=$encUri&foodName=$encName&category=$encCat&description=$encDesc&servings=$encServ&freshness=$encFresh"
        }
    }
    data object Discovery : Screen("discovery")
    data object DonationDetail : Screen("donation_detail/{donationId}") {
        fun createRoute(donationId: String): String = "donation_detail/$donationId"
    }
    data object MyDonations : Screen("my_donations")
    data object Profile : Screen("profile")
}
