package com.example.fundaciontextilcircular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.fundaciontextilcircular.ui.theme.TextilCircularTheme
import com.example.fundaciontextilcircular.view.MainScreen
import com.example.fundaciontextilcircular.view.PantallaAdmin
import com.example.fundaciontextilcircular.view.PantallaBienvenida
import com.example.fundaciontextilcircular.view.PantallaCuentaEnRevision
import com.example.fundaciontextilcircular.view.PantallaLogin
import com.example.fundaciontextilcircular.view.PantallaRegistro

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContent {

            TextilCircularTheme {

                var pantallaActual by remember {
                    mutableStateOf("bienvenida")
                }


                // CORREO DE CUENTA PENDIENTE O RECHAZADA

                var correoEnRevision by remember {
                    mutableStateOf("")
                }


                // CORREO DE LA EMPRENDEDORA
                // QUE INICIÓ SESIÓN

                var correoUsuarioActual by remember {
                    mutableStateOf("")
                }


                when (pantallaActual) {


                    // =========================
                    // BIENVENIDA
                    // =========================

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


                    // =========================
                    // LOGIN
                    // =========================

                    "login" -> {

                        PantallaLogin(

                            onIngresarAdmin = {

                                pantallaActual =
                                    "admin"
                            },


                            // USUARIO APROBADO

                            onIngresarUsuario = { correo ->

                                correoUsuarioActual =
                                    correo

                                pantallaActual =
                                    "principal"
                            },


                            // PENDIENTE O RECHAZADO

                            onCuentaEnRevision = { correo ->

                                correoEnRevision =
                                    correo

                                pantallaActual =
                                    "revision"
                            },


                            onCrearCuenta = {

                                pantallaActual =
                                    "registro"
                            },


                            onVolver = {

                                pantallaActual =
                                    "bienvenida"
                            }
                        )
                    }


                    // =========================
                    // REGISTRO
                    // =========================

                    "registro" -> {

                        PantallaRegistro(

                            onRegistrar = { correo ->

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


                    // =========================
                    // CUENTA EN REVISIÓN
                    // =========================

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


                    // =========================
                    // ADMINISTRADOR
                    // =========================

                    "admin" -> {

                        PantallaAdmin(

                            onCerrarSesion = {

                                pantallaActual =
                                    "login"
                            }
                        )
                    }


                    // =========================
                    // APLICACIÓN PRINCIPAL
                    // =========================

                    "principal" -> {

                        MainScreen(
                            correo =
                                correoUsuarioActual
                        )
                    }


                    // =========================
                    // SEGURIDAD
                    // =========================

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