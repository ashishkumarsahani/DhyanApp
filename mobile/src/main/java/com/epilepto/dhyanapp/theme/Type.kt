package com.epilepto.dhyanapp.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.epilepto.dhyanapp.R

val NunitoSans = FontFamily(
    listOf(
        Font(resId = R.font.nunito_normal, weight = FontWeight.Normal),
        Font(resId = R.font.nunito_semibold, weight = FontWeight.Bold),
        Font(resId = R.font.nunitosans_light, weight = FontWeight.Light)
    )
)

val Roboto = FontFamily(
    listOf(
        Font(resId = R.font.roboto_light, weight = FontWeight.Light),
        Font(resId = R.font.roboto_bold, weight = FontWeight.Bold),
        Font(resId = R.font.roboto_medium, weight = FontWeight.Medium),
        Font(resId = R.font.roboto_thin, weight = FontWeight.Thin),
        Font(resId = R.font.nunito_normal, weight = FontWeight.Normal),
//        Font(resId = R.font.roboto_regular, weight = FontWeight.Normal),
    )
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

