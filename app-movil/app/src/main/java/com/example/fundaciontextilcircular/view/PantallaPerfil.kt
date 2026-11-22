package com.example.fundaciontextilcircular.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextilcircular.data.remote.RetrofitClient
import com.example.fundaciontextilcircular.data.repository.AuthRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor
import com.example.fundaciontextilcircular.ui.theme.SelectorTema
import com.example.fundaciontextilcircular.viewmodel.PerfilViewModel
import com.example.fundaciontextilcircular.viewmodel.PerfilViewModelFactory


@Composable
fun PantallaPerfil(
    correo: String,
    modifier: Modifier = Modifier,
    onCerrarSesion: () -> Unit = {}
) {

    val context =
        LocalContext.current


    // =========================================
    // RED
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

    val perfilViewModel:
            PerfilViewModel =
        viewModel(

            factory =
                PerfilViewModelFactory(
                    authRepository
                )
        )


    val uiState =
        perfilViewModel.uiState


    var mostrarCambiarContrasena by
    remember {

        mutableStateOf(
            false
        )
    }


    // =========================================
    // CARGAR PERFIL
    // =========================================

    LaunchedEffect(
        correo,
        isOnline
    ) {

        if (
            isOnline &&
            uiState.perfil == null
        ) {

            perfilViewModel
                .cargarPerfil(

                    correo =
                        correo,

                    isOnline =
                        true
                )
        }
    }


    Column(

        modifier =
            modifier
                .fillMaxSize()
                .background(
                    MaterialTheme
                        .colorScheme
                        .background
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
                    30.dp
                )
        )


        Text(
            text =
                "MI PERFIL",

            color =
                MaterialTheme
                    .colorScheme
                    .primary,

            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )


        Spacer(
            modifier =
                Modifier.height(
                    18.dp
                )
        )


        // =====================================
        // SELECTOR MODO CLARO / OSCURO
        // =====================================

        Card(

            modifier =
                Modifier.fillMaxWidth(),

            colors =
                CardDefaults.cardColors(

                    containerColor =
                        MaterialTheme
                            .colorScheme
                            .surface
                )
        ) {

            Column(

                modifier =
                    Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 4.dp
                    )
            ) {

                Text(

                    text =
                        "APARIENCIA",

                    color =
                        MaterialTheme
                            .colorScheme
                            .primary,

                    style =
                        MaterialTheme
                            .typography
                            .labelLarge
                )


                SelectorTema()
            }
        }


        Spacer(
            modifier =
                Modifier.height(
                    24.dp
                )
        )


        // =====================================
        // CARGANDO
        // =====================================

        if (
            uiState.cargando &&
            uiState.perfil == null
        ) {

            Text(
                text =
                    "Cargando información...",

                color =
                    MaterialTheme
                        .colorScheme
                        .onBackground
            )
        }


        // =====================================
        // ERROR
        // =====================================

        uiState.error
            ?.takeIf {

                uiState.perfil == null
            }
            ?.let {

                Text(
                    text =
                        it,

                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            16.dp
                        )
                )


                Button(

                    onClick = {

                        perfilViewModel
                            .cargarPerfil(

                                correo =
                                    correo,

                                isOnline =
                                    isOnline
                            )
                    },

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .primary,

                            contentColor =
                                MaterialTheme
                                    .colorScheme
                                    .onPrimary
                        )
                ) {

                    Text(
                        "REINTENTAR"
                    )
                }
            }


        // =====================================
        // PERFIL
        // =====================================

        uiState.perfil
            ?.let {
                    usuario ->


                TarjetaDatoPerfil(
                    titulo =
                        "Nombre",

                    valor =
                        usuario.nombre
                )


                TarjetaDatoPerfil(
                    titulo =
                        "RUT",

                    valor =
                        usuario.rut
                )


                TarjetaDatoPerfil(
                    titulo =
                        "Correo",

                    valor =
                        usuario.correo
                )


                TarjetaDatoPerfil(
                    titulo =
                        "Edad",

                    valor =
                        usuario.edad
                            .toString()
                )


                TarjetaDatoPerfil(
                    titulo =
                        "Género",

                    valor =
                        usuario.genero
                )


                TarjetaDatoPerfil(
                    titulo =
                        "Emprendimiento",

                    valor =
                        usuario.nombreEmprendimiento
                )


                TarjetaDatoPerfil(
                    titulo =
                        "Rubro",

                    valor =
                        usuario.rubro
                )


                TarjetaDatoPerfil(
                    titulo =
                        "Región",

                    valor =
                        usuario.region
                )


                TarjetaDatoPerfil(
                    titulo =
                        "Comuna",

                    valor =
                        usuario.comuna
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            20.dp
                        )
                )


                // =================================
                // ESTADO DE LA CUENTA
                // =================================

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .primary
                        )
                ) {

                    Column(

                        modifier =
                            Modifier.padding(
                                16.dp
                            )
                    ) {

                        Text(
                            text =
                                "Estado: ${usuario.estado}",

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onPrimary,

                            style =
                                MaterialTheme
                                    .typography
                                    .titleMedium
                        )


                        Spacer(
                            modifier =
                                Modifier.height(
                                    4.dp
                                )
                        )


                        Text(
                            text =

                                if (
                                    usuario.estado ==
                                    "APROBADA"
                                ) {

                                    "Tu cuenta se encuentra aprobada."

                                } else {

                                    "Estado actual de tu cuenta: ${usuario.estado}"
                                },

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onPrimary
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(
                            24.dp
                        )
                )


                // =================================
                // CAMBIAR CONTRASEÑA
                // =================================

                Button(

                    onClick = {

                        perfilViewModel
                            .limpiarMensajes()


                        mostrarCambiarContrasena =
                            true
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(
                                56.dp
                            ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .primary,

                            contentColor =
                                MaterialTheme
                                    .colorScheme
                                    .onPrimary
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Lock,

                        contentDescription =
                            null
                    )


                    Text(
                        text =
                            "  CAMBIAR CONTRASEÑA"
                    )
                }


                Spacer(
                    modifier =
                        Modifier.height(
                            16.dp
                        )
                )


                // =================================
                // CERRAR SESIÓN
                // =================================

                Button(

                    onClick =
                        onCerrarSesion,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(
                                56.dp
                            ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .error,

                            contentColor =
                                MaterialTheme
                                    .colorScheme
                                    .onError
                        )
                ) {

                    Text(
                        text =
                            "CERRAR SESIÓN",

                        style =
                            MaterialTheme
                                .typography
                                .labelLarge
                    )
                }


                Spacer(
                    modifier =
                        Modifier.height(
                            30.dp
                        )
                )
            }
    }


    // =========================================
    // CAMBIAR CONTRASEÑA
    // =========================================

    if (
        mostrarCambiarContrasena
    ) {

        DialogoCambiarContrasenaBackend(

            correo =
                correo,

            isOnline =
                isOnline,

            perfilViewModel =
                perfilViewModel,

            onCerrar = {

                perfilViewModel
                    .limpiarMensajes()


                mostrarCambiarContrasena =
                    false
            }
        )
    }
}


// =========================================
// TARJETA DATO DEL PERFIL
// =========================================

@Composable
fun TarjetaDatoPerfil(
    titulo: String,
    valor: String
) {

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    bottom =
                        12.dp
                ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    MaterialTheme
                        .colorScheme
                        .surface
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {

            Text(
                text =
                    titulo,

                color =
                    MaterialTheme
                        .colorScheme
                        .primary,

                style =
                    MaterialTheme
                        .typography
                        .labelLarge
            )


            Spacer(
                modifier =
                    Modifier.height(
                        6.dp
                    )
            )


            Text(
                text =
                    valor,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurface,

                style =
                    MaterialTheme
                        .typography
                        .bodyLarge
            )
        }
    }
}


// =========================================
// CAMBIAR CONTRASEÑA CON BACKEND
// =========================================

@Composable
fun DialogoCambiarContrasenaBackend(

    correo: String,

    isOnline: Boolean,

    perfilViewModel:
    PerfilViewModel,

    onCerrar: () -> Unit
) {

    val uiState =
        perfilViewModel.uiState


    var contrasenaActual by
    remember {

        mutableStateOf("")
    }


    var nuevaContrasena by
    remember {

        mutableStateOf("")
    }


    var confirmarContrasena by
    remember {

        mutableStateOf("")
    }


    AlertDialog(

        onDismissRequest = {

            if (
                !uiState.cambiandoContrasena
            ) {

                onCerrar()
            }
        },

        containerColor =
            MaterialTheme
                .colorScheme
                .surface,

        titleContentColor =
            MaterialTheme
                .colorScheme
                .onSurface,

        textContentColor =
            MaterialTheme
                .colorScheme
                .onSurface,

        title = {

            Text(
                text =
                    "Cambiar contraseña"
            )
        },

        text = {

            Column {

                Text(
                    text =
                        "Ingresa tu contraseña actual y luego crea una nueva."
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            16.dp
                        )
                )


                OutlinedTextField(

                    value =
                        contrasenaActual,

                    onValueChange = {

                        contrasenaActual =
                            it


                        perfilViewModel
                            .limpiarMensajes()
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            "Contraseña actual"
                        )
                    },

                    visualTransformation =
                        PasswordVisualTransformation(),

                    singleLine =
                        true
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                OutlinedTextField(

                    value =
                        nuevaContrasena,

                    onValueChange = {

                        nuevaContrasena =
                            it


                        perfilViewModel
                            .limpiarMensajes()
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            "Nueva contraseña"
                        )
                    },

                    visualTransformation =
                        PasswordVisualTransformation(),

                    singleLine =
                        true
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                OutlinedTextField(

                    value =
                        confirmarContrasena,

                    onValueChange = {

                        confirmarContrasena =
                            it


                        perfilViewModel
                            .limpiarMensajes()
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            "Confirmar nueva contraseña"
                        )
                    },

                    visualTransformation =
                        PasswordVisualTransformation(),

                    singleLine =
                        true
                )


                uiState.error
                    ?.let {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    12.dp
                                )
                        )


                        Text(
                            text =
                                it,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error
                        )
                    }


                uiState.mensaje
                    ?.let {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    12.dp
                                )
                        )


                        Text(
                            text =
                                it,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .primary
                        )
                    }
            }
        },

        confirmButton = {

            if (
                uiState.mensaje != null
            ) {

                TextButton(
                    onClick =
                        onCerrar
                ) {

                    Text(
                        "CERRAR"
                    )
                }

            } else {

                TextButton(

                    enabled =
                        !uiState.cambiandoContrasena,

                    onClick = {

                        perfilViewModel
                            .cambiarContrasena(

                                correo =
                                    correo,

                                contrasenaActual =
                                    contrasenaActual,

                                nuevaContrasena =
                                    nuevaContrasena,

                                confirmarContrasena =
                                    confirmarContrasena,

                                isOnline =
                                    isOnline
                            )
                    }
                ) {

                    Text(

                        if (
                            uiState.cambiandoContrasena
                        ) {

                            "CAMBIANDO..."

                        } else {

                            "CAMBIAR"
                        }
                    )
                }
            }
        },

        dismissButton = {

            if (
                uiState.mensaje == null
            ) {

                TextButton(

                    enabled =
                        !uiState.cambiandoContrasena,

                    onClick =
                        onCerrar
                ) {

                    Text(
                        "CANCELAR"
                    )
                }
            }
        }
    )
}