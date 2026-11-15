package com.example.fundaciontextilcircular.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.repository.ChatRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor
import com.example.fundaciontextilcircular.ui.view.AgregarProductoScreen
import com.example.fundaciontextilcircular.ui.view.ChatScreen
import com.example.fundaciontextilcircular.ui.view.ListaChatsScreen
import com.example.fundaciontextilcircular.ui.viewmodel.ChatViewModel

@Composable
fun MainScreen(
    correo: String,
    onCerrarSesion: () -> Unit = {}
) {

    val context =
        LocalContext.current


    // =============================================
    // BASE DE DATOS
    // =============================================

    val database =
        remember(context) {

            TextilDatabase
                .getDatabase(
                    context
                )
        }


    val mensajeDao =
        remember(database) {

            database
                .mensajeDao()
        }


    // =============================================
    // MENSAJES NO LEÍDOS
    // =============================================

    val totalMensajesNoLeidos by
    mensajeDao
        .observarTotalNoLeidos()
        .collectAsState(
            initial = 0
        )


    // =============================================
    // PESTAÑA ACTUAL
    // =============================================

    var opcionSeleccionada by
    remember {

        mutableIntStateOf(0)
    }


    // =============================================
    // CATÁLOGO
    // =============================================

    var mostrarAgregarProducto by
    remember {

        mutableStateOf(false)
    }


    // =============================================
    // CHAT
    // =============================================

    var chatIdSeleccionado by
    remember {

        mutableStateOf<String?>(
            null
        )
    }


    var nombreChatSeleccionado by
    remember {

        mutableStateOf("")
    }


    // =============================================
    // CHAT REPOSITORY
    // =============================================

    val chatRepository =
        remember(
            mensajeDao
        ) {

            ChatRepository(
                mensajeDao =
                    mensajeDao
            )
        }


    // =============================================
    // NETWORK MONITOR
    // =============================================

    val networkMonitor =
        remember(
            context
        ) {

            NetworkMonitor(
                context.applicationContext
            )
        }


    // =============================================
    // CHAT VIEWMODEL FACTORY
    // =============================================

    val chatViewModelFactory =
        remember(
            chatRepository,
            networkMonitor
        ) {

            object :
                ViewModelProvider.Factory {

                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {

                    if (
                        modelClass
                            .isAssignableFrom(
                                ChatViewModel::class.java
                            )
                    ) {

                        @Suppress("UNCHECKED_CAST")

                        return ChatViewModel(

                            repository =
                                chatRepository,

                            networkMonitor =
                                networkMonitor

                        ) as T
                    }


                    throw IllegalArgumentException(
                        "ViewModel desconocido"
                    )
                }
            }
        }


    // =============================================
    // CHAT VIEWMODEL
    // =============================================

    val chatViewModel:
            ChatViewModel =

        viewModel(
            factory =
                chatViewModelFactory
        )


    // =============================================
    // SCAFFOLD
    // =============================================

    Scaffold(

        bottomBar = {

            NavigationBar(

                containerColor =
                    MaterialTheme
                        .colorScheme
                        .background

            ) {


                // =================================
                // INICIO
                // =================================

                NavigationBarItem(

                    selected =
                        opcionSeleccionada == 0,

                    onClick = {

                        opcionSeleccionada =
                            0

                        mostrarAgregarProducto =
                            false
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

                        Text(
                            "Inicio"
                        )
                    },

                    colors =
                        coloresNavegacion()
                )


                // =================================
                // CATÁLOGO
                // =================================

                NavigationBarItem(

                    selected =
                        opcionSeleccionada == 1,

                    onClick = {

                        opcionSeleccionada =
                            1

                        mostrarAgregarProducto =
                            false
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

                        Text(
                            "Catálogo"
                        )
                    },

                    colors =
                        coloresNavegacion()
                )


                // =================================
                // MENSAJES
                // =================================

                NavigationBarItem(

                    selected =
                        opcionSeleccionada == 2,

                    onClick = {

                        opcionSeleccionada =
                            2

                        mostrarAgregarProducto =
                            false

                        chatIdSeleccionado =
                            null

                        nombreChatSeleccionado =
                            ""
                    },

                    icon = {

                        BadgedBox(

                            badge = {

                                if (
                                    totalMensajesNoLeidos > 0
                                ) {

                                    Badge(

                                        containerColor =
                                            Color(
                                                0xFFFFC107
                                            ),

                                        contentColor =
                                            Color.Black

                                    ) {

                                        Text(

                                            text =
                                                if (
                                                    totalMensajesNoLeidos > 99
                                                ) {

                                                    "99+"

                                                } else {

                                                    totalMensajesNoLeidos
                                                        .toString()
                                                }
                                        )
                                    }
                                }
                            }

                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.Email,

                                contentDescription =
                                    "Mensajes"
                            )
                        }
                    },

                    label = {

                        Text(
                            "Mensajes"
                        )
                    },

                    colors =
                        coloresNavegacion()
                )


                // =================================
                // PERFIL
                // =================================

                NavigationBarItem(

                    selected =
                        opcionSeleccionada == 3,

                    onClick = {

                        opcionSeleccionada =
                            3

                        mostrarAgregarProducto =
                            false
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

                        Text(
                            "Perfil"
                        )
                    },

                    colors =
                        coloresNavegacion()
                )
            }
        }

    ) { paddingValues ->


        when (
            opcionSeleccionada
        ) {


            // =====================================
            // INICIO
            // =====================================

            0 -> {

                PantallaInicio(

                    modifier =
                        Modifier
                            .padding(
                                paddingValues
                            )
                )
            }


            // =====================================
            // CATÁLOGO
            // =====================================

            1 -> {

                if (
                    mostrarAgregarProducto
                ) {

                    // =============================
                    // NUEVA PUBLICACIÓN
                    // =============================

                    Box(

                        modifier =
                            Modifier
                                .padding(
                                    paddingValues
                                )

                    ) {

                        AgregarProductoScreen(

                            correoUsuario =
                                correo,

                            onProductoPublicado = {

                                mostrarAgregarProducto =
                                    false
                            },

                            onVolver = {

                                mostrarAgregarProducto =
                                    false
                            }
                        )
                    }

                } else {

                    // =============================
                    // CATÁLOGO PRINCIPAL
                    // =============================

                    PantallaCatalogo(

                        correoUsuario =
                            correo,

                        modifier =
                            Modifier
                                .padding(
                                    paddingValues
                                ),

                        onAgregarProducto = {

                            mostrarAgregarProducto =
                                true
                        }
                    )
                }
            }


            // =====================================
            // MENSAJES
            // =====================================

            2 -> {

                Box(

                    modifier =
                        Modifier
                            .padding(
                                paddingValues
                            )

                ) {

                    if (
                        chatIdSeleccionado == null
                    ) {

                        // =============================
                        // LISTA DE CONVERSACIONES
                        // =============================

                        ListaChatsScreen(

                            correoUsuario =
                                correo,

                            onAbrirChat = {
                                    chatId,
                                    nombreChat ->


                                chatIdSeleccionado =
                                    chatId


                                nombreChatSeleccionado =
                                    nombreChat
                            }
                        )

                    } else {

                        // =============================
                        // CHAT
                        // =============================

                        ChatScreen(

                            chatId =
                                chatIdSeleccionado!!,

                            nombreChat =
                                nombreChatSeleccionado,

                            viewModel =
                                chatViewModel,

                            onVolver = {

                                chatIdSeleccionado =
                                    null

                                nombreChatSeleccionado =
                                    ""
                            }
                        )
                    }
                }
            }


            // =====================================
            // PERFIL
            // =====================================

            3 -> {

                PantallaPerfil(

                    correo =
                        correo,

                    onCerrarSesion = {

                        onCerrarSesion()
                    },

                    modifier =
                        Modifier
                            .padding(
                                paddingValues
                            )
                )
            }
        }
    }
}


// =============================================
// COLORES DE NAVEGACIÓN
// =============================================

@Composable
private fun coloresNavegacion() =

    NavigationBarItemDefaults
        .colors(

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