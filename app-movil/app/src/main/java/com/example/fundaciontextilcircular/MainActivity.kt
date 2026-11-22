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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.repository.ChatRepository
import com.example.fundaciontextilcircular.data.repository.ProductoRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor
import com.example.fundaciontextilcircular.ui.theme.TextilCircularTheme
import com.example.fundaciontextilcircular.ui.view.ChatScreen
import com.example.fundaciontextilcircular.ui.viewmodel.ChatViewModel
import com.example.fundaciontextilcircular.ui.viewmodel.ProductoViewModel
import com.example.fundaciontextilcircular.view.MainScreen
import com.example.fundaciontextilcircular.view.PantallaAdmin
import com.example.fundaciontextilcircular.view.PantallaBienvenida
import com.example.fundaciontextilcircular.view.PantallaCuentaEnRevision
import com.example.fundaciontextilcircular.view.PantallaLogin
import com.example.fundaciontextilcircular.view.PantallaRecuperarContrasena
import com.example.fundaciontextilcircular.view.PantallaRegistro
import com.example.fundaciontextilcircular.view.PantallaSplash
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContent {

            TextilCircularTheme {

                // =========================================
                // PANTALLA ACTUAL
                // =========================================

                var pantallaActual by remember {
                    mutableStateOf("splash")
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
                    val pantallaClara =
                        pantallaActual == "splash" ||
                                pantallaActual == "principal"

                    val controlador =
                        WindowCompat
                            .getInsetsController(
                                window,
                                window.decorView
                            )

                    controlador.isAppearanceLightStatusBars = pantallaClara
                    controlador.isAppearanceLightNavigationBars = pantallaClara
                }

                // =========================================
                // NAVEGACIÓN
                // =========================================

                when (pantallaActual) {

                    // =====================================
                    // SPLASH ANIMADO
                    // =====================================

                    "splash" -> {
                        PantallaSplash(
                            onFinalizar = {
                                pantallaActual = "bienvenida"
                            }
                        )
                    }

                    // =====================================
                    // BIENVENIDA
                    // =====================================

                    "bienvenida" -> {
                        PantallaBienvenida(
                            onIniciarSesion = {
                                pantallaActual = "login"
                            },
                            onCrearCuenta = {
                                pantallaActual = "registro"
                            }
                        )
                    }

                    // =====================================
                    // LOGIN
                    // =====================================

                    "login" -> {
                        PantallaLogin(
                            onIngresarAdmin = {
                                pantallaActual = "admin"
                            },
                            onIngresarUsuario = { correo ->
                                correoUsuarioActual = correo
                                pantallaActual = "principal"
                            },
                            onCuentaEnRevision = { correo ->
                                correoEnRevision = correo
                                pantallaActual = "revision"
                            },
                            onCrearCuenta = {
                                pantallaActual = "registro"
                            },
                            onOlvideContrasena = {
                                pantallaActual = "recuperarContrasena"
                            },
                            onVolver = {
                                pantallaActual = "bienvenida"
                            }
                        )
                    }

                    // =====================================
                    // RECUPERAR CONTRASEÑA
                    // =====================================

                    "recuperarContrasena" -> {
                        PantallaRecuperarContrasena(
                            onVolver = {
                                pantallaActual = "login"
                            }
                        )
                    }

                    // =====================================
                    // REGISTRO
                    // =====================================

                    "registro" -> {
                        PantallaRegistro(
                            onRegistrar = { correo ->
                                correoEnRevision = correo
                                pantallaActual = "revision"
                            },
                            onVolver = {
                                pantallaActual = "bienvenida"
                            }
                        )
                    }

                    // =====================================
                    // CUENTA EN REVISIÓN
                    // =====================================

                    "revision" -> {
                        PantallaCuentaEnRevision(
                            correo = correoEnRevision,
                            onVolverInicioSesion = {
                                pantallaActual = "login"
                            }
                        )
                    }

                    // =====================================
                    // ADMINISTRADOR
                    // =====================================

                    "admin" -> {
                        PantallaAdmin(
                            onCerrarSesion = {
                                pantallaActual = "login"
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
                        val repositorio = remember {
                            ChatRepository(
                                mensajeDao = database.mensajeDao(),
                                appContext = context.applicationContext
                            )
                        }
                        val networkMonitor = remember { NetworkMonitor(context) }

                        val adminChatViewModel: ChatViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
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
                        val networkMonitor = remember { NetworkMonitor(context) }

                        val chatRepository = remember {
                            ChatRepository(
                                mensajeDao = database.mensajeDao(),
                                appContext = context.applicationContext
                            )
                        }

                        val productoRepository = remember {
                            ProductoRepository(
                                productoDao = database.productoDao(),
                                appContext = context.applicationContext
                            )
                        }

                        val userChatViewModel: ChatViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return ChatViewModel(chatRepository, networkMonitor) as T
                                }
                            }
                        )

                        val productoViewModel: ProductoViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return ProductoViewModel(productoRepository, networkMonitor) as T
                                }
                            }
                        )

                        userChatViewModel.esAdmin = false

                        MainScreen(
                            correo = correoUsuarioActual,
                            productoViewModel = productoViewModel,
                            onCerrarSesion = {
                                FirebaseAuth
                                    .getInstance()
                                    .signOut()

                                correoUsuarioActual = ""
                                correoEnRevision = ""
                                pantallaActual = "login"
                            }
                        )
                    }

                    // =====================================
                    // SEGURIDAD
                    // =====================================

                    else -> {
                        PantallaBienvenida(
                            onIniciarSesion = {
                                pantallaActual = "login"
                            },
                            onCrearCuenta = {
                                pantallaActual = "registro"
                            }
                        )
                    }
                }
            }
        }
    }
}