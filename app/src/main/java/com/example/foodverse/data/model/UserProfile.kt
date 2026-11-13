package com.example.foodverse.data.model

data class UserProfile(
    val id: String = "user_demo_01",
    val name: String = "Alex Chen",
    val email: String = "alex.chen@foodverse.org",
    val phone: String = "+1 555-019-2834",
    val role: String = "Community Donor & Volunteer",
    val organization: String = "Green Hope Initiative",
    val donationsCount: Int = 14,
    val mealsSaved: Int = 186,
    val impactPoints: Int = 420
)
