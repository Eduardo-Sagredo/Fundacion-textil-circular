package com.example.fundaciontextilcircular.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext


// =========================================
// MODO CLARO
// =========================================

private val LightColorScheme =
    lightColorScheme(

        primary =
            TurquesaTextil,

        onPrimary =
            BlancoTextil,

        secondary =
            AmarilloTextil,

        onSecondary =
            NegroTextil,

        background =
            FondoClaroTextil,

        onBackground =
            TextoClaroTextil,

        surface =
            SuperficieClaraTextil,

        onSurface =
            TextoClaroTextil,

        error =
            RojoErrorTextil,

        onError =
            BlancoTextil
    )


// =========================================
// MODO OSCURO
// =========================================

private val DarkColorScheme =
    darkColorScheme(

        primary =
            TurquesaOscuroTextil,

        onPrimary =
            NegroTextil,

        secondary =
            AmarilloOscuroTextil,

        onSecondary =
            NegroTextil,

        background =
            FondoOscuroTextil,

        onBackground =
            TextoOscuroTextil,

        surface =
            SuperficieOscuraTextil,

        onSurface =
            TextoOscuroTextil,

        error =
            RojoErrorTextil,

        onError =
            BlancoTextil
    )


// =========================================
// TEMA GENERAL
// =========================================

@Composable
fun TextilCircularTheme(

    content:
    @Composable () -> Unit
) {

    val context =
        LocalContext.current


    val sistemaOscuro =
        isSystemInDarkTheme()


    LaunchedEffect(
        Unit
    ) {

        ThemeManager.inicializar(

            context =
                context,

            valorInicial =
                sistemaOscuro
        )
    }


    val modoOscuro =
        ThemeManager.darkMode
            ?: sistemaOscuro


    MaterialTheme(

        colorScheme =

            if (
                modoOscuro
            ) {

                DarkColorScheme

            } else {

                LightColorScheme
            },

        typography =
            Typography,

        content =
            content
    )
}