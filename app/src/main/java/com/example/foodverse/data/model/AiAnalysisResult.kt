package com.example.foodverse.data.model

data class AiAnalysisResult(
    val foodName: String = "",
    val category: FoodCategory = FoodCategory.COOKED_MEALS,
    val description: String = "",
    val freshnessObservation: String = "",
    val estimatedServings: String = "",
    val storageRecommendation: String = "Keep refrigerated until pickup",
    val disclaimer: String = "AI observations are based solely on visual appearance. AI cannot certify food safety or detect spoilage. Please inspect food manually before consumption.",
    val confidenceNote: String? = null,
    val isSuccess: Boolean = true,
    val errorMessage: String? = null
)
