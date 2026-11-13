package com.example.foodverse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodverse.data.model.FoodCategory
import com.example.foodverse.ui.theme.FoodVerseBorder
import com.example.foodverse.ui.theme.FoodVerseGreenDark
import com.example.foodverse.ui.theme.FoodVerseGreenPrimary
import com.example.foodverse.ui.theme.FoodVerseTextPrimary
import com.example.foodverse.ui.theme.FoodVerseTextSecondary

@Composable
fun CategoryFilterRow(
    selectedCategory: FoodCategory,
    onCategorySelected: (FoodCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FoodCategory.entries.forEach { category ->
            val isSelected = category == selectedCategory
            val bg = if (isSelected) FoodVerseGreenPrimary else Color.White
            val textColor = if (isSelected) Color.White else FoodVerseTextSecondary
            val borderModifier = if (isSelected) Modifier else Modifier.background(Color.White)

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .then(borderModifier)
                    .background(bg)
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "${category.emoji} ${category.displayName}",
                    color = textColor,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
