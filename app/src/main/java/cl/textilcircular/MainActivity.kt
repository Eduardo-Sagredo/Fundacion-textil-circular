package cl.textilcircular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cl.textilcircular.ui.theme.TextilCircularTheme
import cl.textilcircular.view.MainScreen
import cl.textilcircular.view.PantallaAdmin
import cl.textilcircular.view.PantallaBienvenida
import cl.textilcircular.view.PantallaCuentaEnRevision
import cl.textilcircular.view.PantallaLogin
import cl.textilcircular.view.PantallaRegistro

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            TextilCircularTheme {

                var pantallaActual by remember {
                    mutableStateOf("bienvenida")
                }

                when (pantallaActual) {

                    // PANTALLA DE BIENVENIDA
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


                    // INICIO DE SESIÓN
                    "login" -> {

                        PantallaLogin(

                            onIngresarAdmin = {
                                pantallaActual = "admin"
                            },

                            onIngresarUsuario = {
                                pantallaActual = "principal"
                            },

                            onCrearCuenta = {
                                pantallaActual = "registro"
                            },

                            onVolver = {
                                pantallaActual = "bienvenida"
                            }
                        )
                    }


                    // REGISTRO
                    "registro" -> {

                        PantallaRegistro(

                            onRegistrar = {
                                pantallaActual = "revision"
                            },

                            onVolver = {
                                pantallaActual = "bienvenida"
                            }
                        )
                    }


                    // CUENTA EN REVISIÓN
                    "revision" -> {

                        PantallaCuentaEnRevision(

                            onVolverInicioSesion = {
                                pantallaActual = "login"
                            }
                        )
                    }


                    // PANEL ADMINISTRADOR
                    "admin" -> {

                        PantallaAdmin(

                            onCerrarSesion = {
                                pantallaActual = "login"
                            }
                        )
                    }


                    // APLICACIÓN PRINCIPAL
                    "principal" -> {

                        MainScreen()
                    }


                    // POR SEGURIDAD
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