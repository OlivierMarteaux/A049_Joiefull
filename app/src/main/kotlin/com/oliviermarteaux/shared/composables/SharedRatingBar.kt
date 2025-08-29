package com.oliviermarteaux.shared.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun sharedRatingBar(
    //info SharedIcon parameters
    iconChecked: ImageVector,
    iconUnchecked: ImageVector,
    modifier : Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
    //info  IconToggleButton Parameters
    checked: Boolean  = false,
    onCheckedChange: (Boolean) -> Unit = {},
    enabled: Boolean = true,
    colors: IconToggleButtonColors = IconButtonDefaults.iconToggleButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    iconToggleModifier: Modifier = Modifier,
    //info  Row Parameters
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    //info  RatingBar Parameters
    stars: Int = 5,                             // total stars
    iconModifier: Modifier = Modifier,          // icon modifier,
    rating: Int = 0,                            // current rating
):Int {
    var rating by remember { mutableIntStateOf(rating) }
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        for (i in 1..stars) {
            SharedIconToggle(
                iconChecked = iconChecked,
                iconUnchecked = iconUnchecked,
                tint = tint,
                modifier = iconToggleModifier,
                enabled = enabled,
                onCheckedChange = { rating = if (it) i else i-1 },
                checked = i <= rating,
                contentDescription = contentDescription,
                interactionSource = interactionSource,
                colors = colors,
                iconModifier = iconModifier
            )
        }
    }
    return rating
}