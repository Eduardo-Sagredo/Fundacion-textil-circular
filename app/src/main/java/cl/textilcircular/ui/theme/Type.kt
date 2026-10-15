package cl.textilcircular.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cl.textilcircular.R

val NunitoFont = FontFamily(
    Font(
        resId = R.font.nunito_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.nunito_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.nunito_bold,
        weight = FontWeight.Bold
    )
)

val Typography = Typography(

    headlineLarge = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),

    titleLarge = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),

    titleMedium = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    labelLarge = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),

    labelMedium = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    )
)