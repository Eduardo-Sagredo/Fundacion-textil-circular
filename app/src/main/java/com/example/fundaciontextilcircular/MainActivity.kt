package com.example.fundaciontextilcircular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.repository.ChatRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor
import com.example.fundaciontextilcircular.ui.theme.TextilCircularTheme
import com.example.fundaciontextilcircular.ui.view.ChatScreen
import com.example.fundaciontextilcircular.ui.viewmodel.ChatViewModel
import com.example.fundaciontextilcircular.view.MainScreen
import com.example.fundaciontextilcircular.view.PantallaAdmin
import com.example.fundaciontextilcircular.view.PantallaBienvenida
import com.example.fundaciontextilcircular.view.PantallaCuentaEnRevision
import com.example.fundaciontextilcircular.view.PantallaLogin
import com.example.fundaciontextilcircular.view.PantallaRecuperarContrasena
import com.example.fundaciontextilcircular.view.PantallaRegistro
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContent {

            TextilCircularTheme {

                // =========================================
                // PANTALLA ACTUAL
                // =========================================

                var pantallaActual by remember {

                    mutableStateOf(
                        "bienvenida"
                    )
                }


                // =========================================
                // CORREO CUENTA EN REVISIÓN
                // =========================================

                var correoEnRevision by remember {

                    mutableStateOf("")
                }


                // =========================================
                // CORREO USUARIO ACTUAL
                // =========================================

                var correoUsuarioActual by remember {

                    mutableStateOf("")
                }

                // =========================================
                // CHAT SELECCIONADO POR EL ADMIN
                // =========================================

                var chatSeleccionado by remember {
                    mutableStateOf("")
                }


                // =========================================
                // BARRAS DEL SISTEMA
                // =========================================

                SideEffect {

                    /*
                     * Todas las pantallas anteriores
                     * al MainScreen utilizan principalmente
                     * fondo oscuro.
                     *
                     * Principal utiliza fondo claro.
                     */

                    val pantallaOscura =
                        pantallaActual != "principal"


                    val controlador =
                        WindowCompat
                            .getInsetsController(
                                window,
                                window.decorView
                            )


                    // =====================================
                    // BARRA SUPERIOR
                    // =====================================

                    controlador
                        .isAppearanceLightStatusBars =
                        !pantallaOscura


                    // =====================================
                    // BARRA INFERIOR
                    // =====================================

                    controlador
                        .isAppearanceLightNavigationBars =
                        !pantallaOscura
                }


                // =========================================
                // NAVEGACIÓN
                // =========================================

                when (
                    pantallaActual
                ) {


                    // =====================================
                    // BIENVENIDA
                    // =====================================

                    "bienvenida" -> {

                        PantallaBienvenida(

                            onIniciarSesion = {

                                pantallaActual =
                                    "login"
                            },

                            onCrearCuenta = {

                                pantallaActual =
                                    "registro"
                            }
                        )
                    }


                    // =====================================
                    // LOGIN
                    // =====================================

                    "login" -> {

                        PantallaLogin(

                            onIngresarAdmin = {

                                pantallaActual =
                                    "admin"
                            },


                            onIngresarUsuario = {
                                    correo ->

                                correoUsuarioActual =
                                    correo

                                pantallaActual =
                                    "principal"
                            },


                            onCuentaEnRevision = {
                                    correo ->

                                correoEnRevision =
                                    correo

                                pantallaActual =
                                    "revision"
                            },


                            onCrearCuenta = {

                                pantallaActual =
                                    "registro"
                            },


                            onOlvideContrasena = {

                                pantallaActual =
                                    "recuperarContrasena"
                            },


                            onVolver = {

                                pantallaActual =
                                    "bienvenida"
                            }
                        )
                    }


                    // =====================================
                    // RECUPERAR CONTRASEÑA
                    // =====================================

                    "recuperarContrasena" -> {

                        PantallaRecuperarContrasena(

                            onVolver = {

                                pantallaActual =
                                    "login"
                            }
                        )
                    }


                    // =====================================
                    // REGISTRO
                    // =====================================

                    "registro" -> {

                        PantallaRegistro(

                            onRegistrar = {
                                    correo ->

                                correoEnRevision =
                                    correo

                                pantallaActual =
                                    "revision"
                            },


                            onVolver = {

                                pantallaActual =
                                    "bienvenida"
                            }
                        )
                    }


                    // =====================================
                    // CUENTA EN REVISIÓN
                    // =====================================

                    "revision" -> {

                        PantallaCuentaEnRevision(

                            correo =
                                correoEnRevision,


                            onVolverInicioSesion = {

                                pantallaActual =
                                    "login"
                            }
                        )
                    }


                    // =====================================
                    // ADMINISTRADOR
                    // =====================================

                    "admin" -> {

                        PantallaAdmin(

                            onCerrarSesion = {

                                pantallaActual =
                                    "login"
                            },

                            onVerChat = { idDelChat ->
                                chatSeleccionado = idDelChat
                                pantallaActual = "chat_admin"
                            }

                        )
                    }

                    // =====================================
                    // CHAT DEL ADMINISTRADOR
                    // =====================================
                    "chat_admin" -> {
                        val context = LocalContext.current
                        val database = remember { TextilDatabase.getDatabase(context) }
                        val repositorio = remember { ChatRepository(database.mensajeDao()) }
                        val networkMonitor = remember { NetworkMonitor(context) }

                        val adminChatViewModel: ChatViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                            factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                    return ChatViewModel(repositorio, networkMonitor) as T
                                }
                            }
                        )

                        adminChatViewModel.esAdmin = true

                        ChatScreen(
                            chatId = chatSeleccionado,
                            nombreChat = "Soporte Técnico (Ticket)",
                            viewModel = adminChatViewModel,
                            onVolver = {
                                pantallaActual = "admin"
                                chatSeleccionado = ""
                            }
                        )
                    }


                    // =====================================
                    // USUARIO PRINCIPAL
                    // =====================================

                    "principal" -> {

                        val context = LocalContext.current
                        val database = remember { TextilDatabase.getDatabase(context) }
                        val repositorio = remember { ChatRepository(database.mensajeDao()) }
                        val networkMonitor = remember { NetworkMonitor(context) }

                        val userChatViewModel: ChatViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                            factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                    return ChatViewModel(repositorio, networkMonitor) as T
                                }
                            }
                        )

                        userChatViewModel.esAdmin = false

                        MainScreen(

                            correo =
                                correoUsuarioActual,


                            onCerrarSesion = {

                                // =================================
                                // CERRAR SESIÓN FIREBASE
                                // =================================

                                FirebaseAuth
                                    .getInstance()
                                    .signOut()


                                // =================================
                                // LIMPIAR SESIÓN LOCAL
                                // =================================

                                correoUsuarioActual =
                                    ""

                                correoEnRevision =
                                    ""


                                // =================================
                                // VOLVER AL LOGIN
                                // =================================

                                pantallaActual =
                                    "login"
                            }
                        )
                    }


                    // =====================================
                    // SEGURIDAD
                    // =====================================

                    else -> {

                        PantallaBienvenida(

                            onIniciarSesion = {

                                pantallaActual =
                                    "login"
                            },

                            onCrearCuenta = {

                                pantallaActual =
                                    "registro"
                            }
                        )
                    }
                }
            }
        }
    }
}