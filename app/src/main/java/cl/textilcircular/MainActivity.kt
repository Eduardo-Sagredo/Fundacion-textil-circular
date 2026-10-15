package cl.textilcircular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cl.textilcircular.ui.theme.TextilCircularTheme
import cl.textilcircular.view.PantallaBienvenida
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

                    "login" -> {
                        PantallaLogin(
                            onCrearCuenta = {
                                pantallaActual = "registro"
                            },
                            onVolver = {
                                pantallaActual = "bienvenida"
                            }
                        )
                    }

                    "registro" -> {
                        PantallaRegistro(
                            onRegistrar = {
                                // Más adelante validaremos los datos
                            },
                            onVolver = {
                                pantallaActual = "bienvenida"
                            }
                        )
                    }
                }
            }
        }
    }
}