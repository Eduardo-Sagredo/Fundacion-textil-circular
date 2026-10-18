package com.example.fundaciontextilcircular.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val TextilCircularColorScheme = lightColorScheme(

    primary = TurquesaTextil,
    onPrimary = BlancoTextil,

    secondary = AmarilloTextil,
    onSecondary = NegroTextil,

    background = BlancoTextil,
    onBackground = NegroTextil,

    surface = BlancoTextil,
    onSurface = NegroTextil,

    error = RojoErrorTextil
)

@Composable
fun TextilCircularTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = TextilCircularColorScheme,
        typography = Typography,
        content = content
    )
}