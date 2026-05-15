package com.example.kaushalya_karnataka.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StarRating(
    rating: Double,
    modifier: Modifier = Modifier,
    starCount: Int = 5,
    starSize: Dp = 16.dp,
    activeColor: Color = Color(0xFFFACC15), // Improved Gold color
    inactiveColor: Color = Color(0xFFE2E8F0)
) {
    Row(modifier = modifier) {
        for (i in 1..starCount) {
            val icon = when {
                i <= rating -> Icons.Filled.Star
                i - 0.5 <= rating -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Outlined.StarOutline
            }
            val tint = if (i <= rating || (i - 0.5 <= rating)) activeColor else inactiveColor
            Icon(
                imageVector = icon,
                contentDescription = "Star $i",
                tint = tint,
                modifier = Modifier.size(starSize)
            )
        }
    }
}
