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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.RojoErrorTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

@Composable
fun PantallaPerfil(
    correo: String,
    modifier: Modifier = Modifier,
    onCerrarSesion: () -> Unit = {}
) {

    val context =
        LocalContext.current

    val dao =
        remember(context) {

            TextilDatabase
                .getDatabase(context)
                .emprendedoraDao()
        }

    val cuenta by dao
        .observarPorCorreo(correo)
        .collectAsState(
            initial = null
        )

    val auth =
        remember {

            FirebaseAuth
                .getInstance()
        }

    var mostrarCambiarContrasena by remember {
        mutableStateOf(false)
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
                .padding(24.dp)
    ) {

        Spacer(
            modifier =
                Modifier.height(30.dp)
        )


        Text(
            text =
                "MI PERFIL",

            color =
                TurquesaTextil,

            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )


        Spacer(
            modifier =
                Modifier.height(24.dp)
        )


        if (
            cuenta == null
        ) {

            Text(
                text =
                    "Cargando información...",

                color =
                    BlancoTextil
            )

        } else {

            val usuario =
                cuenta!!


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
                    Modifier.height(20.dp)
            )


            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    CardDefaults
                        .cardColors(
                            containerColor =
                                TurquesaTextil
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
                            "Cuenta activa",

                        color =
                            NegroTextil,

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium
                    )


                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )


                    Text(
                        text =
                            "Tu cuenta se encuentra aprobada.",

                        color =
                            NegroTextil
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            // =========================
            // CAMBIAR CONTRASEÑA
            // =========================

            Button(
                onClick = {

                    mostrarCambiarContrasena =
                        true
                },

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
                    Modifier.height(16.dp)
            )


            // =========================
            // CERRAR SESIÓN
            // =========================

            Button(
                onClick = {

                    onCerrarSesion()
                },

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                RojoErrorTextil,

                            contentColor =
                                BlancoTextil
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
                    Modifier.height(30.dp)
            )
        }
    }


    // =========================
    // VENTANA CAMBIAR CONTRASEÑA
    // =========================

    if (
        mostrarCambiarContrasena
    ) {

        DialogoCambiarContrasena(

            auth =
                auth,

            onCerrar = {

                mostrarCambiarContrasena =
                    false
            }
        )
    }
}


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
                    bottom = 12.dp
                ),

        colors =
            CardDefaults
                .cardColors(
                    containerColor =
                        BlancoTextil
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
                    TurquesaTextil,

                style =
                    MaterialTheme
                        .typography
                        .labelLarge
            )


            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )


            Text(
                text =
                    valor,

                color =
                    NegroTextil,

                style =
                    MaterialTheme
                        .typography
                        .bodyLarge
            )
        }
    }
}


@Composable
fun DialogoCambiarContrasena(
    auth: FirebaseAuth,
    onCerrar: () -> Unit
) {

    var contrasenaActual by remember {
        mutableStateOf("")
    }

    var nuevaContrasena by remember {
        mutableStateOf("")
    }

    var confirmarContrasena by remember {
        mutableStateOf("")
    }

    var mensajeError by remember {
        mutableStateOf<String?>(null)
    }

    var mensajeExito by remember {
        mutableStateOf<String?>(null)
    }

    var cambiando by remember {
        mutableStateOf(false)
    }


    AlertDialog(

        onDismissRequest = {

            if (
                !cambiando
            ) {

                onCerrar()
            }
        },


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
                        Modifier.height(16.dp)
                )


                OutlinedTextField(
                    value =
                        contrasenaActual,

                    onValueChange = {

                        contrasenaActual =
                            it

                        mensajeError =
                            null
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
                        Modifier.height(12.dp)
                )


                OutlinedTextField(
                    value =
                        nuevaContrasena,

                    onValueChange = {

                        nuevaContrasena =
                            it

                        mensajeError =
                            null
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
                        Modifier.height(12.dp)
                )


                OutlinedTextField(
                    value =
                        confirmarContrasena,

                    onValueChange = {

                        confirmarContrasena =
                            it

                        mensajeError =
                            null
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


                mensajeError
                    ?.let {

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )


                        Text(
                            text =
                                it,

                            color =
                                RojoErrorTextil
                        )
                    }


                mensajeExito
                    ?.let {

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )


                        Text(
                            text =
                                it,

                            color =
                                TurquesaTextil
                        )
                    }
            }
        },


        confirmButton = {

            TextButton(
                enabled =
                    !cambiando,

                onClick = {

                    mensajeError =
                        null

                    mensajeExito =
                        null


                    if (
                        contrasenaActual.isBlank() ||
                        nuevaContrasena.isBlank() ||
                        confirmarContrasena.isBlank()
                    ) {

                        mensajeError =
                            "Debes completar todos los campos"

                        return@TextButton
                    }


                    if (
                        nuevaContrasena.length <
                        7
                    ) {

                        mensajeError =
                            "La nueva contraseña debe tener al menos 7 caracteres"

                        return@TextButton
                    }


                    if (
                        !nuevaContrasena.any {
                            it.isLetter()
                        }
                    ) {

                        mensajeError =
                            "La nueva contraseña debe contener una letra"

                        return@TextButton
                    }


                    if (
                        !nuevaContrasena.any {
                            it.isDigit()
                        }
                    ) {

                        mensajeError =
                            "La nueva contraseña debe contener un número"

                        return@TextButton
                    }


                    if (
                        nuevaContrasena.all {
                            it.isLetterOrDigit()
                        }
                    ) {

                        mensajeError =
                            "La nueva contraseña debe contener un carácter especial"

                        return@TextButton
                    }


                    if (
                        nuevaContrasena !=
                        confirmarContrasena
                    ) {

                        mensajeError =
                            "Las contraseñas no coinciden"

                        return@TextButton
                    }


                    if (
                        nuevaContrasena ==
                        contrasenaActual
                    ) {

                        mensajeError =
                            "La nueva contraseña debe ser diferente a la actual"

                        return@TextButton
                    }


                    val usuario =
                        auth.currentUser


                    val correo =
                        usuario?.email


                    if (
                        usuario == null ||
                        correo == null
                    ) {

                        mensajeError =
                            "No se encontró la sesión del usuario"

                        return@TextButton
                    }


                    cambiando =
                        true


                    val credencial =
                        EmailAuthProvider
                            .getCredential(
                                correo,
                                contrasenaActual
                            )


                    usuario
                        .reauthenticate(
                            credencial
                        )
                        .addOnCompleteListener {
                                reauthTask ->


                            if (
                                reauthTask
                                    .isSuccessful
                            ) {


                                usuario
                                    .updatePassword(
                                        nuevaContrasena
                                    )
                                    .addOnCompleteListener {
                                            actualizarTask ->


                                        cambiando =
                                            false


                                        if (
                                            actualizarTask
                                                .isSuccessful
                                        ) {

                                            contrasenaActual =
                                                ""

                                            nuevaContrasena =
                                                ""

                                            confirmarContrasena =
                                                ""


                                            mensajeExito =
                                                "Contraseña actualizada correctamente"


                                        } else {

                                            mensajeError =
                                                "No se pudo actualizar la contraseña. Inténtalo nuevamente."
                                        }
                                    }


                            } else {

                                cambiando =
                                    false


                                mensajeError =
                                    if (
                                        reauthTask.exception
                                                is FirebaseAuthInvalidCredentialsException
                                    ) {

                                        "La contraseña actual es incorrecta"

                                    } else {

                                        "No se pudo verificar tu contraseña actual"
                                    }
                            }
                        }
                }
            ) {

                Text(
                    text =
                        if (
                            cambiando
                        ) {

                            "GUARDANDO..."

                        } else {

                            "GUARDAR"
                        }
                )
            }
        },


        dismissButton = {

            TextButton(
                enabled =
                    !cambiando,

                onClick =
                    onCerrar
            ) {

                Text(
                    text =
                        "CANCELAR"
                )
            }
        }
    )
}