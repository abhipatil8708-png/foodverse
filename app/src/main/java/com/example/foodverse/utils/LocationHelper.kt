package com.example.foodverse.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import java.util.Locale

object LocationHelper {
    // Reference location for demo/discovery (Center of downtown)
    const val DEFAULT_USER_LAT = 12.9716
    const val DEFAULT_USER_LNG = 77.5946

    /**
     * Calculates distance between two coordinates in kilometers using Haversine Formula.
     */
    fun calculateDistanceKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusKm * c
    }

    /**
     * Formats distance into a clean readable string (e.g., "1.2 km away", "450 m away").
     */
    fun formatDistance(distanceKm: Double?): String {
        if (distanceKm == null) return "Nearby"
        return if (distanceKm < 1.0) {
            val meters = (distanceKm * 1000).toInt()
            "${meters}m away"
        } else {
            String.format(Locale.US, "%.1f km away", distanceKm)
        }
    }
}
