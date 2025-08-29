package com.oliviermarteaux.shared.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material.icons.twotone.StarRate
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SharedRatingBar(
    modifier: Modifier = Modifier,
    rating: Float = 2.5f,                         // current rating
    onRatingChanged: (Float) -> Unit = {},      // callback when user clicks
    stars: Int = 5,                             // total stars
    starSize: Dp = 32.dp,                       // size of each star
    spaceBetween: Dp = 4.dp,                    // spacing between stars
    enabled: Boolean = true                     // disable touch if needed
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        for (i in 1..stars) {
            val icon = when {
                i <= rating -> Icons.Filled.Star        // full
                i - rating in 0.25..0.75 -> Icons.AutoMirrored.Filled.StarHalf// half
                else -> Icons.Outlined.Star               // empty
            }
            val tint = when {
                i <= rating -> Color(0xFFFFD700) // gold
                i - rating in 0.25..0.75 -> Color(0xFFFFD700) // gold
                else -> Color.LightGray
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(starSize)
                    .let {
                        if (enabled) {
                            it.clickable { onRatingChanged(i.toFloat()) }
                        } else it
                    },
                tint = tint
            )
        }
    }
}
