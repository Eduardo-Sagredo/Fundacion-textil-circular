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

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.fundaciontextilcircular.data.remote.RetrofitClient
import com.example.fundaciontextilcircular.data.repository.AuthRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor

import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.RojoErrorTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil

import com.example.fundaciontextilcircular.viewmodel.RecuperarContrasenaViewModel
import com.example.fundaciontextilcircular.viewmodel.RecuperarContrasenaViewModelFactory

import kotlinx.coroutines.delay


@Composable
fun PantallaRecuperarContrasena(
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {}
) {

    val context =
        LocalContext.current


    // =========================================
    // INTERNET
    // =========================================

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


    // =========================================
    // REPOSITORY
    // =========================================

    val authRepository =
        remember {

            AuthRepository(
                RetrofitClient.authApi
            )
        }


    // =========================================
    // VIEWMODEL
    // =========================================

    val recuperarViewModel:
            RecuperarContrasenaViewModel =
        viewModel(
            factory =
                RecuperarContrasenaViewModelFactory(
                    authRepository
                )
        )


    val uiState by
    recuperarViewModel
        .uiState
        .collectAsState()


    // =========================================
    // CAMPOS
    // =========================================

    var correo by remember {
        mutableStateOf("")
    }


    var codigo by remember {
        mutableStateOf("")
    }


    var nuevaContrasena by remember {
        mutableStateOf("")
    }


    var confirmarContrasena by remember {
        mutableStateOf("")
    }


    // =========================================
    // VOLVER AUTOMÁTICAMENTE AL LOGIN
    // =========================================

    LaunchedEffect(
        uiState.contrasenaRestablecida
    ) {

        if (
            uiState.contrasenaRestablecida
        ) {

            delay(
                1500
            )


            recuperarViewModel
                .reiniciarFlujo()


            onVolver()
        }
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
                Modifier.height(
                    70.dp
                )
        )


        Text(
            text =
                "RECUPERAR",

            color =
                BlancoTextil,

            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )


        Text(
            text =
                "CONTRASEÑA",

            color =
                TurquesaTextil,

            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )


        Spacer(
            modifier =
                Modifier.height(
                    24.dp
                )
        )


        // =========================================
        // PASO 1 - CORREO
        // =========================================

        if (
            !uiState.codigoEnviado
        ) {


            Text(
                text =
                    "Ingresa el correo asociado a tu cuenta. " +
                            "Te enviaremos un código de 6 dígitos.",

                color =
                    BlancoTextil,

                style =
                    MaterialTheme
                        .typography
                        .bodyLarge
            )


            Spacer(
                modifier =
                    Modifier.height(
                        28.dp
                    )
            )


            OutlinedTextField(

                value =
                    correo,

                onValueChange = {

                    correo = it


                    recuperarViewModel
                        .limpiarMensajes()
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
                            "Correo"
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
                    Modifier.height(
                        24.dp
                    )
            )


            Button(

                onClick = {

                    recuperarViewModel
                        .enviarCodigo(

                            correo =
                                correo,

                            isOnline =
                                isOnline
                        )
                },

                enabled =
                    !uiState.cargando,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(
                            56.dp
                        ),

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

                            "ENVIANDO..."

                        } else {

                            "ENVIAR CÓDIGO"
                        }
                )
            }

        } else {


            // =====================================
            // PASO 2
            // CÓDIGO + NUEVA CONTRASEÑA
            // =====================================

            if (
                !uiState
                    .contrasenaRestablecida
            ) {


                Text(
                    text =
                        "Enviamos un código de 6 dígitos a tu correo.",

                    color =
                        BlancoTextil,

                    style =
                        MaterialTheme
                            .typography
                            .bodyLarge
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            24.dp
                        )
                )


                OutlinedTextField(

                    value =
                        codigo,

                    onValueChange = {

                        if (
                            it.length <= 6 &&
                            it.all { caracter ->
                                caracter.isDigit()
                            }
                        ) {

                            codigo = it
                        }


                        recuperarViewModel
                            .limpiarMensajes()
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth(),

                    label = {

                        Text(
                            "Código de 6 dígitos"
                        )
                    },

                    singleLine =
                        true,

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Number
                        ),

                    colors =
                        coloresCampoRegistro()
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            16.dp
                        )
                )


                OutlinedTextField(

                    value =
                        nuevaContrasena,

                    onValueChange = {

                        nuevaContrasena =
                            it


                        recuperarViewModel
                            .limpiarMensajes()
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth(),

                    label = {

                        Text(
                            "Nueva contraseña"
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.Lock,

                            contentDescription =
                                "Nueva contraseña"
                        )
                    },

                    singleLine =
                        true,

                    visualTransformation =
                        PasswordVisualTransformation(),

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
                        Modifier.height(
                            16.dp
                        )
                )


                OutlinedTextField(

                    value =
                        confirmarContrasena,

                    onValueChange = {

                        confirmarContrasena =
                            it


                        recuperarViewModel
                            .limpiarMensajes()
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth(),

                    label = {

                        Text(
                            "Confirmar contraseña"
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.Lock,

                            contentDescription =
                                "Confirmar contraseña"
                        )
                    },

                    singleLine =
                        true,

                    visualTransformation =
                        PasswordVisualTransformation(),

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
                        Modifier.height(
                            24.dp
                        )
                )


                Button(

                    onClick = {

                        recuperarViewModel
                            .restablecerContrasena(

                                codigo =
                                    codigo,

                                nuevaContrasena =
                                    nuevaContrasena,

                                confirmarContrasena =
                                    confirmarContrasena,

                                isOnline =
                                    isOnline
                            )
                    },

                    enabled =
                        !uiState.cargando,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(
                                56.dp
                            ),

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

                                "CAMBIANDO..."

                            } else {

                                "CAMBIAR CONTRASEÑA"
                            }
                    )
                }

            } else {


                // =================================
                // CONTRASEÑA CAMBIADA
                // =================================


                Text(
                    text =
                        "Contraseña restablecida correctamente.",

                    color =
                        TurquesaTextil,

                    style =
                        MaterialTheme
                            .typography
                            .bodyLarge
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )


                Text(
                    text =
                        "Ya puedes iniciar sesión con tu nueva contraseña.",

                    color =
                        BlancoTextil,

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium
                )
            }
        }


        // =========================================
        // ERROR
        // =========================================

        uiState
            .error
            ?.let { error ->


                Spacer(
                    modifier =
                        Modifier.height(
                            18.dp
                        )
                )


                Text(
                    text =
                        error,

                    color =
                        RojoErrorTextil,

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium
                )
            }


        // =========================================
        // MENSAJE CORRECTO
        // =========================================

        if (
            !uiState
                .contrasenaRestablecida
        ) {

            uiState
                .mensaje
                ?.let { mensaje ->


                    Spacer(
                        modifier =
                            Modifier.height(
                                18.dp
                            )
                    )


                    Text(
                        text =
                            mensaje,

                        color =
                            TurquesaTextil,

                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium
                    )
                }
        }


        Spacer(
            modifier =
                Modifier.height(
                    20.dp
                )
        )


        // =========================================
        // VOLVER
        // =========================================

        TextButton(

            onClick = {


                // IMPORTANTE:
                // limpiar estado antes de volver

                recuperarViewModel
                    .reiniciarFlujo()


                onVolver()
            },

            modifier =
                Modifier
                    .fillMaxWidth()
        ) {

            Text(
                text =
                    "Volver al inicio de sesión",

                color =
                    TurquesaTextil
            )
        }
    }
}