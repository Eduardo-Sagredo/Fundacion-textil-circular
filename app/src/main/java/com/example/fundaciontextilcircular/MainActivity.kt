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

        super.onCreate(
            savedInstanceState
        )

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


                when (pantallaActual) {


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


                    "login" -> {

                        PantallaLogin(

                            onIngresarAdmin = {

                                pantallaActual =
                                    "admin"
                            },

                            onIngresarUsuario = {

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

                            onVolver = {

                                pantallaActual =
                                    "bienvenida"
                            }
                        )
                    }


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


                    "admin" -> {

                        PantallaAdmin(

                            onCerrarSesion = {

                                pantallaActual =
                                    "login"
                            }
                        )
                    }


                    "principal" -> {

                        MainScreen()
                    }


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