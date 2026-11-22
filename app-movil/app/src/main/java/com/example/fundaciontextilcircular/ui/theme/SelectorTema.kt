package com.example.fundaciontextilcircular.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@Composable
fun SelectorTema(

    modifier: Modifier =
        Modifier
) {

    val context =
        LocalContext.current


    val sistemaOscuro =
        isSystemInDarkTheme()


    val modoOscuro =
        ThemeManager.darkMode
            ?: sistemaOscuro


    Row(

        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    vertical = 12.dp
                ),

        horizontalArrangement =
            Arrangement.SpaceBetween,

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Row(

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(

                imageVector =

                    if (
                        modoOscuro
                    ) {

                        Icons.Default.DarkMode

                    } else {

                        Icons.Default.LightMode
                    },

                contentDescription =
                    null,

                tint =
                    MaterialTheme
                        .colorScheme
                        .primary
            )


            Text(

                text =

                    if (
                        modoOscuro
                    ) {

                        "  Modo oscuro"

                    } else {

                        "  Modo claro"
                    },

                color =
                    MaterialTheme
                        .colorScheme
                        .onBackground
            )
        }


        Switch(

            checked =
                modoOscuro,

            onCheckedChange = {
                    oscuro ->

                ThemeManager
                    .cambiarModo(

                        context =
                            context,

                        oscuro =
                            oscuro
                    )
            }
        )
    }
}