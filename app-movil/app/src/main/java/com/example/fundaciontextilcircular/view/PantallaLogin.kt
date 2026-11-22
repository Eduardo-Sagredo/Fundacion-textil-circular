package com.example.fundaciontextilcircular.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextilcircular.data.remote.RetrofitClient
import com.example.fundaciontextilcircular.data.repository.AuthRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor
import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.RojoErrorTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil
import com.example.fundaciontextilcircular.viewmodel.LoginViewModel
import com.example.fundaciontextilcircular.viewmodel.LoginViewModelFactory

@Composable
fun PantallaLogin(
    modifier: Modifier = Modifier,
    onIngresarAdmin: () -> Unit = {},
    onIngresarUsuario: (String) -> Unit = {},
    onCuentaEnRevision: (String) -> Unit = {},
    onCrearCuenta: () -> Unit = {},
    onOlvideContrasena: () -> Unit = {},
    onVolver: () -> Unit = {}
) {

    val context =
        LocalContext.current


    // =========================
    // CONEXIÓN A INTERNET
    // =========================

    val networkMonitor =
        remember {
            NetworkMonitor(
                context.applicationContext
            )
        }

    val isOnline by
    networkMonitor
        .isOnline
        .collectAsState(
            initial = true
        )


    // =========================
    // REPOSITORY
    // =========================

    val authRepository =
        remember {
            AuthRepository(
                RetrofitClient.authApi
            )
        }


    // =========================
    // VIEWMODEL
    // =========================

    val loginViewModel: LoginViewModel =
        viewModel(
            factory =
                LoginViewModelFactory(
                    authRepository
                )
        )

    val uiState =
        loginViewModel.uiState


    // =========================
    // ESTADOS DE PANTALLA
    // =========================

    var correo by remember {
        mutableStateOf("")
    }

    var contrasena by remember {
        mutableStateOf("")
    }

    var mostrarContrasena by remember {
        mutableStateOf(false)
    }

    var mensajeErrorLocal by remember {
        mutableStateOf<String?>(null)
    }


    // =========================
    // RESPUESTA DEL BACKEND
    // =========================

    LaunchedEffect(
        uiState.resultado
    ) {

        val resultado =
            uiState.resultado
                ?: return@LaunchedEffect


        when (
            resultado
                .estado
                .uppercase()
        ) {

            // =========================
            // APROBADA
            // =========================

            "APROBADA" -> {

                if (
                    resultado.puedeIngresar
                ) {

                    onIngresarUsuario(
                        resultado.correo
                    )

                } else {

                    mensajeErrorLocal =
                        "La cuenta no tiene autorización para ingresar"
                }
            }


            // =========================
            // PENDIENTE
            // =========================

            "PENDIENTE" -> {

                onCuentaEnRevision(
                    resultado.correo
                )
            }


            // =========================
            // RECHAZADA
            // =========================

            "RECHAZADA" -> {

                onCuentaEnRevision(
                    resultado.correo
                )
            }


            else -> {

                mensajeErrorLocal =
                    "El estado de la cuenta no es válido"
            }
        }


        loginViewModel
            .limpiarResultado()
    }


    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(
                    NegroTextil
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    24.dp
                )
    ) {

        Spacer(
            modifier =
                Modifier
                    .height(70.dp)
        )


        Text(
            text =
                "INICIAR",

            color =
                BlancoTextil,

            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )


        Text(
            text =
                "SESIÓN",

            color =
                TurquesaTextil,

            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )


        Spacer(
            modifier =
                Modifier
                    .height(32.dp)
        )


        // =========================
        // CORREO
        // =========================

        OutlinedTextField(
            value =
                correo,

            onValueChange = {

                correo =
                    it

                mensajeErrorLocal =
                    null

                loginViewModel
                    .limpiarError()
            },

            modifier =
                Modifier
                    .fillMaxWidth(),

            label = {

                Text(
                    "Correo electrónico"
                )
            },

            leadingIcon = {

                Icon(
                    imageVector =
                        Icons.Default.Email,

                    contentDescription =
                        "Correo electrónico"
                )
            },

            singleLine =
                true,

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Email
                ),

            colors =
                coloresCampoRegistro()
        )


        Spacer(
            modifier =
                Modifier
                    .height(16.dp)
        )


        // =========================
        // CONTRASEÑA
        // =========================

        OutlinedTextField(
            value =
                contrasena,

            onValueChange = {

                contrasena =
                    it

                mensajeErrorLocal =
                    null

                loginViewModel
                    .limpiarError()
            },

            modifier =
                Modifier
                    .fillMaxWidth(),

            label = {

                Text(
                    "Contraseña"
                )
            },

            leadingIcon = {

                Icon(
                    imageVector =
                        Icons.Default.Lock,

                    contentDescription =
                        "Contraseña"
                )
            },

            trailingIcon = {

                IconButton(
                    onClick = {

                        mostrarContrasena =
                            !mostrarContrasena
                    }
                ) {

                    Icon(
                        imageVector =
                            if (
                                mostrarContrasena
                            ) {

                                Icons.Default
                                    .VisibilityOff

                            } else {

                                Icons.Default
                                    .Visibility
                            },

                        contentDescription =
                            "Mostrar u ocultar contraseña"
                    )
                }
            },

            visualTransformation =
                if (
                    mostrarContrasena
                ) {

                    VisualTransformation.None

                } else {

                    PasswordVisualTransformation()
                },

            singleLine =
                true,

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password
                ),

            colors =
                coloresCampoRegistro()
        )


        Spacer(
            modifier =
                Modifier
                    .height(8.dp)
        )


        // =========================
        // OLVIDÉ MI CONTRASEÑA
        // =========================

        TextButton(
            onClick =
                onOlvideContrasena,

            modifier =
                Modifier
                    .fillMaxWidth()
        ) {

            Text(
                text =
                    "¿Olvidaste tu contraseña?",

                color =
                    TurquesaTextil
            )
        }


        Spacer(
            modifier =
                Modifier
                    .height(8.dp)
        )


        // =========================
        // ERROR
        // =========================

        val mensajeError =
            mensajeErrorLocal
                ?: uiState.error


        mensajeError
            ?.let {

                Text(
                    text =
                        it,

                    color =
                        RojoErrorTextil,

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium
                )


                Spacer(
                    modifier =
                        Modifier
                            .height(12.dp)
                )
            }


        // =========================
        // INGRESAR
        // =========================

        Button(
            onClick = {

                mensajeErrorLocal =
                    null

                loginViewModel
                    .limpiarError()


                val correoNormalizado =
                    correo
                        .trim()
                        .lowercase()


                // =========================
                // VALIDACIÓN BÁSICA
                // =========================

                if (
                    correoNormalizado
                        .isBlank() ||
                    contrasena
                        .isBlank()
                ) {

                    mensajeErrorLocal =
                        "Debes ingresar correo y contraseña"

                    return@Button
                }


                // =========================
                // VALIDACIÓN INTERNET
                // =========================

                if (!isOnline) {

                    mensajeErrorLocal =
                        "No tienes conexión a internet"

                    return@Button
                }


                // =========================
                // ADMINISTRADOR
                // =========================

                if (
                    correoNormalizado ==
                    "admin@textilcircular.cl" &&
                    contrasena ==
                    "Admin123!"
                ) {

                    onIngresarAdmin()

                    return@Button
                }


                // =========================
                // LOGIN SPRING BOOT
                // =========================

                loginViewModel
                    .iniciarSesion(
                        correo =
                            correoNormalizado,

                        contrasena =
                            contrasena,

                        isOnline =
                            isOnline
                    )
            },

            enabled =
                !uiState.cargando,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),

            colors =
                ButtonDefaults
                    .buttonColors(

                        containerColor =
                            TurquesaTextil,

                        contentColor =
                            NegroTextil
                    )
        ) {

            Text(
                text =
                    if (
                        uiState.cargando
                    ) {

                        "INGRESANDO..."

                    } else {

                        "INGRESAR"
                    },

                style =
                    MaterialTheme
                        .typography
                        .labelLarge
            )
        }


        Spacer(
            modifier =
                Modifier
                    .height(20.dp)
        )


        // =========================
        // CREAR CUENTA
        // =========================

        TextButton(
            onClick =
                onCrearCuenta,

            modifier =
                Modifier
                    .fillMaxWidth()
        ) {

            Text(
                text =
                    "¿No tienes una cuenta? Crear cuenta",

                color =
                    BlancoTextil
            )
        }


        // =========================
        // VOLVER
        // =========================

        TextButton(
            onClick =
                onVolver,

            modifier =
                Modifier
                    .fillMaxWidth()
        ) {

            Text(
                text =
                    "Volver",

                color =
                    TurquesaTextil
            )
        }
    }
}