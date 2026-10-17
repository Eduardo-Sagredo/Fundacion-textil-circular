package com.example.fundaciontextilcircular.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(
    correo: String
) {

    var opcionSeleccionada by remember {
        mutableIntStateOf(0)
    }

    Scaffold(

        bottomBar = {

            NavigationBar(
                containerColor =
                    MaterialTheme.colorScheme.background
            ) {


                // INICIO

                NavigationBarItem(
                    selected =
                        opcionSeleccionada == 0,

                    onClick = {
                        opcionSeleccionada = 0
                    },

                    icon = {

                        Icon(
                            imageVector =
                                Icons.Default.Home,
                            contentDescription =
                                "Inicio"
                        )
                    },

                    label = {
                        Text("Inicio")
                    },

                    colors =
                        NavigationBarItemDefaults.colors(

                            selectedIconColor =
                                MaterialTheme
                                    .colorScheme
                                    .primary,

                            selectedTextColor =
                                MaterialTheme
                                    .colorScheme
                                    .primary,

                            indicatorColor =
                                MaterialTheme
                                    .colorScheme
                                    .secondary,

                            unselectedIconColor =
                                MaterialTheme
                                    .colorScheme
                                    .onBackground,

                            unselectedTextColor =
                                MaterialTheme
                                    .colorScheme
                                    .onBackground
                        )
                )


                // CATÁLOGO

                NavigationBarItem(
                    selected =
                        opcionSeleccionada == 1,

                    onClick = {
                        opcionSeleccionada = 1
                    },

                    icon = {

                        Icon(
                            imageVector =
                                Icons.Default.List,
                            contentDescription =
                                "Catálogo"
                        )
                    },

                    label = {
                        Text("Catálogo")
                    },

                    colors =
                        NavigationBarItemDefaults.colors(

                            selectedIconColor =
                                MaterialTheme
                                    .colorScheme
                                    .primary,

                            selectedTextColor =
                                MaterialTheme
                                    .colorScheme
                                    .primary,

                            indicatorColor =
                                MaterialTheme
                                    .colorScheme
                                    .secondary,

                            unselectedIconColor =
                                MaterialTheme
                                    .colorScheme
                                    .onBackground,

                            unselectedTextColor =
                                MaterialTheme
                                    .colorScheme
                                    .onBackground
                        )
                )


                // MENSAJES

                NavigationBarItem(
                    selected =
                        opcionSeleccionada == 2,

                    onClick = {
                        opcionSeleccionada = 2
                    },

                    icon = {

                        Icon(
                            imageVector =
                                Icons.Default.Email,
                            contentDescription =
                                "Mensajes"
                        )
                    },

                    label = {
                        Text("Mensajes")
                    },

                    colors =
                        NavigationBarItemDefaults.colors(

                            selectedIconColor =
                                MaterialTheme
                                    .colorScheme
                                    .primary,

                            selectedTextColor =
                                MaterialTheme
                                    .colorScheme
                                    .primary,

                            indicatorColor =
                                MaterialTheme
                                    .colorScheme
                                    .secondary,

                            unselectedIconColor =
                                MaterialTheme
                                    .colorScheme
                                    .onBackground,

                            unselectedTextColor =
                                MaterialTheme
                                    .colorScheme
                                    .onBackground
                        )
                )


                // PERFIL

                NavigationBarItem(
                    selected =
                        opcionSeleccionada == 3,

                    onClick = {
                        opcionSeleccionada = 3
                    },

                    icon = {

                        Icon(
                            imageVector =
                                Icons.Default.Person,
                            contentDescription =
                                "Perfil"
                        )
                    },

                    label = {
                        Text("Perfil")
                    },

                    colors =
                        NavigationBarItemDefaults.colors(

                            selectedIconColor =
                                MaterialTheme
                                    .colorScheme
                                    .primary,

                            selectedTextColor =
                                MaterialTheme
                                    .colorScheme
                                    .primary,

                            indicatorColor =
                                MaterialTheme
                                    .colorScheme
                                    .secondary,

                            unselectedIconColor =
                                MaterialTheme
                                    .colorScheme
                                    .onBackground,

                            unselectedTextColor =
                                MaterialTheme
                                    .colorScheme
                                    .onBackground
                        )
                )
            }
        }

    ) { paddingValues ->


        when (opcionSeleccionada) {


            // INICIO

            0 -> {

                PantallaInicio(
                    modifier =
                        Modifier.padding(
                            paddingValues
                        )
                )
            }


            // CATÁLOGO

            1 -> {

                PantallaCatalogo(
                    modifier =
                        Modifier.padding(
                            paddingValues
                        )
                )
            }


            // MENSAJES

            2 -> {

                PantallaMensajes(
                    modifier =
                        Modifier.padding(
                            paddingValues
                        )
                )
            }


            // PERFIL

            3 -> {

                PantallaPerfil(
                    correo = correo,

                    modifier =
                        Modifier.padding(
                            paddingValues
                        )
                )
            }
        }
    }
}