package com.example.foodverse.data.model

enum class FoodCategory(val displayName: String, val emoji: String) {
    ALL("All", "🍽️"),
    COOKED_MEALS("Cooked Meals", "🍛"),
    BAKERY("Bakery", "🥐"),
    FRUITS_VEGGIES("Fruits & Veg", "🥗"),
    GROCERIES("Groceries", "📦"),
    DAIRY("Dairy", "🥛"),
    BEVERAGES("Beverages", "🧃"),
    CANNED_GOODS("Canned Goods", "🥫");

    companion object {
        fun fromString(name: String?): FoodCategory {
            if (name == null) return COOKED_MEALS
            val cleaned = name.trim().uppercase()
            return entries.find { it.name == cleaned || it.displayName.equals(name.trim(), ignoreCase = true) }
                ?: if (cleaned.contains("BAK") || cleaned.contains("BREAD")) BAKERY
                else if (cleaned.contains("FRUIT") || cleaned.contains("VEG")) FRUITS_VEGGIES
                else if (cleaned.contains("DAIRY") || cleaned.contains("MILK")) DAIRY
                else if (cleaned.contains("CAN")) CANNED_GOODS
                else if (cleaned.contains("BEV") || cleaned.contains("DRINK")) BEVERAGES
                else COOKED_MEALS
        }
    }
}
