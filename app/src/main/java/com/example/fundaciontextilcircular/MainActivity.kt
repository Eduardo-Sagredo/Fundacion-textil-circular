package com.example.fundaciontextilcircular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import com.example.fundaciontextilcircular.ui.theme.TextilCircularTheme
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


        // =========================================
        // BARRA SUPERIOR DEL TELÉFONO
        // =========================================

        WindowCompat
            .getInsetsController(
                window,
                window.decorView
            )
            .isAppearanceLightStatusBars = true


        // =========================================
        // BARRA INFERIOR DEL TELÉFONO
        // =========================================

        WindowCompat
            .getInsetsController(
                window,
                window.decorView
            )
            .isAppearanceLightNavigationBars = true


        setContent {

            TextilCircularTheme {

                var pantallaActual by remember {
                    mutableStateOf(
                        "bienvenida"
                    )
                }


                var correoEnRevision by remember {
                    mutableStateOf("")
                }


                var correoUsuarioActual by remember {
                    mutableStateOf("")
                }


                when (
                    pantallaActual
                ) {


                    // =================================
                    // BIENVENIDA
                    // =================================

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


                    // =================================
                    // LOGIN
                    // =================================

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


                    // =================================
                    // RECUPERAR CONTRASEÑA
                    // =================================

                    "recuperarContrasena" -> {

                        PantallaRecuperarContrasena(

                            onVolver = {

                                pantallaActual =
                                    "login"
                            }
                        )
                    }


                    // =================================
                    // REGISTRO
                    // =================================

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


                    // =================================
                    // CUENTA EN REVISIÓN
                    // =================================

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


                    // =================================
                    // ADMINISTRADOR
                    // =================================

                    "admin" -> {

                        PantallaAdmin(

                            onCerrarSesion = {

                                pantallaActual =
                                    "login"
                            }
                        )
                    }


                    // =================================
                    // USUARIO PRINCIPAL
                    // =================================

                    "principal" -> {

                        MainScreen(

                            correo =
                                correoUsuarioActual,


                            onCerrarSesion = {

                                // =====================
                                // CERRAR FIREBASE
                                // =====================

                                FirebaseAuth
                                    .getInstance()
                                    .signOut()


                                // =====================
                                // LIMPIAR SESIÓN LOCAL
                                // =====================

                                correoUsuarioActual =
                                    ""

                                correoEnRevision =
                                    ""


                                // =====================
                                // VOLVER AL LOGIN
                                // =====================

                                pantallaActual =
                                    "login"
                            }
                        )
                    }


                    // =================================
                    // SEGURIDAD
                    // =================================

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