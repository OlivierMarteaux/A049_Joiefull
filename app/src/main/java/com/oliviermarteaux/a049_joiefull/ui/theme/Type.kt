package com.oliviermarteaux.a049_joiefull.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oliviermarteaux.a049_joiefull.R

val opensans = FontFamily(
    Font(R.font.opensans_regular, FontWeight.Normal),
    Font(R.font.opensans_bold, FontWeight.Bold),
    Font(R.font.opensans_medium, FontWeight.Medium),
    Font(R.font.opensans_light, FontWeight.Light),
    Font(R.font.opensans_extrabold, FontWeight.ExtraBold),
    Font(R.font.opensans_semibold, FontWeight.SemiBold),
)

val Typography = Typography(
    labelLarge = TextStyle(
        fontFamily = opensans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = opensans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),
    // prices on home screen,  item description and user comments on item screen
    bodyMedium = TextStyle(
        fontFamily = opensans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp
    ),
    // prices and rating on item screen
    bodyLarge = TextStyle(
        fontFamily = opensans,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.sp
    ),
    // item title and favorite card text on home screen
    titleSmall = TextStyle(
        fontFamily = opensans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp,
    ),
    // favorite card text and item title on item screen
    titleMedium = TextStyle(
        fontFamily = opensans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.sp,
    ),
    // category titles on home screen
    titleLarge = TextStyle(
        fontFamily = opensans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = opensans,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
)