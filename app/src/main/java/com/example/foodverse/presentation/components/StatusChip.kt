package com.example.foodverse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodverse.data.model.DonationStatus
import com.example.foodverse.ui.theme.FoodVerseGreenDark
import com.example.foodverse.ui.theme.FoodVerseGreenLight
import com.example.foodverse.ui.theme.FoodVerseRed
import com.example.foodverse.ui.theme.FoodVerseRedLight
import com.example.foodverse.ui.theme.FoodVerseWarmOrange
import com.example.foodverse.ui.theme.FoodVerseWarmOrangeLight

@Composable
fun StatusChip(
    status: DonationStatus,
    modifier: Modifier = Modifier
) {
    val (bg, textColor) = when (status) {
        DonationStatus.AVAILABLE -> FoodVerseGreenLight to FoodVerseGreenDark
        DonationStatus.RESERVED -> FoodVerseWarmOrangeLight to FoodVerseWarmOrange
        DonationStatus.COLLECTED -> Color(0xFFF1F5F9) to Color(0xFF64748B)
        DonationStatus.CANCELLED -> FoodVerseRedLight to FoodVerseRed
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
