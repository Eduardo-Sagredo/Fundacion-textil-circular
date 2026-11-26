package com.example.fundaciontextilcircular

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.repository.ChatRepository
import com.example.fundaciontextilcircular.data.repository.ProductoRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor
import com.example.fundaciontextilcircular.ui.theme.TextilCircularTheme
import com.example.fundaciontextilcircular.ui.theme.ThemeManager
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

        // =========================================
        // PERMISO RED LOCAL - ANDROID 17 / API 37
        // =========================================

        if (
            Build.VERSION.SDK_INT >= 37 &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_LOCAL_NETWORK
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_LOCAL_NETWORK),
                1001
            )
        }

        // =========================================
        // INTERFAZ
        // =========================================

        setContent {

            TextilCircularTheme {

                // =========================================
                // MODO CLARO / OSCURO
                // =========================================

                val modoOscuro =
                    ThemeManager.darkMode
                        ?: isSystemInDarkTheme()

                // =========================================
                // BARRAS DEL SISTEMA
                // =========================================

                SideEffect {
                    val controlador =
                        WindowCompat
                            .getInsetsController(
                                window,
                                window.decorView
                            )

                    controlador.isAppearanceLightStatusBars = !modoOscuro
                    controlador.isAppearanceLightNavigationBars = !modoOscuro
                }

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
                // CHAT SELECCIONADO POR ADMIN
                // =========================================

                var chatSeleccionado by remember {
                    mutableStateOf("")
                }

                // =========================================
                // NAVEGACIÓN
                // =========================================

                when (pantallaActual) {

                    // =====================================
                    // SPLASH
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
                    // CHAT ADMINISTRADOR
                    // =====================================

                    "chat_admin" -> {
                        val context = LocalContext.current
                        val database = remember(context) {
                            TextilDatabase.getDatabase(context)
                        }
                        val repositorio = remember(database) {
                            ChatRepository(
                                mensajeDao = database.mensajeDao(),
                                appContext = context.applicationContext
                            )
                        }
                        val networkMonitor = remember(context) {
                            NetworkMonitor(context.applicationContext)
                        }

                        val adminChatViewModel: ChatViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return ChatViewModel(
                                        repository = repositorio,
                                        networkMonitor = networkMonitor
                                    ) as T
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
                        val database = remember(context) {
                            TextilDatabase.getDatabase(context)
                        }
                        val networkMonitor = remember(context) {
                            NetworkMonitor(context.applicationContext)
                        }

                        val chatRepository = remember(database) {
                            ChatRepository(
                                mensajeDao = database.mensajeDao(),
                                appContext = context.applicationContext
                            )
                        }

                        val productoRepository = remember(database) {
                            ProductoRepository(
                                productoDao = database.productoDao(),
                                appContext = context.applicationContext
                            )
                        }

                        val userChatViewModel: ChatViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return ChatViewModel(
                                        repository = chatRepository,
                                        networkMonitor = networkMonitor
                                    ) as T
                                }
                            }
                        )

                        val productoViewModel: ProductoViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return ProductoViewModel(
                                        repository = productoRepository,
                                        networkMonitor = networkMonitor
                                    ) as T
                                }
                            }
                        )

                        userChatViewModel.esAdmin = false

                        MainScreen(
                            correo = correoUsuarioActual,
                            productoViewModel = productoViewModel,
                            onCerrarSesion = {
                                // =================================
                                // CERRAR FIREBASE
                                // =================================
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
                    // SEGURIDAD / PANTALLA DESCONOCIDA
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