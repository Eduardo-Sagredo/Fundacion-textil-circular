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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.RojoErrorTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

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

    val scope =
        rememberCoroutineScope()


    // =========================
    // ROOM
    // =========================

    val dao =
        remember(context) {

            TextilDatabase
                .getDatabase(context)
                .emprendedoraDao()
        }


    // =========================
    // FIREBASE
    // =========================

    val auth =
        remember {

            FirebaseAuth
                .getInstance()
        }


    // =========================
    // ESTADOS
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


    var mensajeError by remember {
        mutableStateOf<String?>(null)
    }


    var iniciandoSesion by remember {
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

                mensajeError =
                    null
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

                mensajeError =
                    null
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

                mensajeError =
                    null


                val correoNormalizado =
                    correo
                        .trim()
                        .lowercase()


                if (
                    correoNormalizado
                        .isBlank() ||
                    contrasena
                        .isBlank()
                ) {

                    mensajeError =
                        "Debes ingresar correo y contraseña"

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


                iniciandoSesion =
                    true


                // =========================
                // FIREBASE
                // =========================

                auth
                    .signInWithEmailAndPassword(
                        correoNormalizado,
                        contrasena
                    )
                    .addOnCompleteListener {
                            task ->


                        if (
                            task.isSuccessful
                        ) {


                            // =========================
                            // BUSCAR DATOS EN ROOM
                            // =========================

                            scope.launch {

                                val cuenta =
                                    dao
                                        .obtenerPorCorreo(
                                            correoNormalizado
                                        )


                                if (
                                    cuenta ==
                                    null
                                ) {

                                    auth.signOut()

                                    iniciandoSesion =
                                        false

                                    mensajeError =
                                        "No se encontraron los datos de esta cuenta"

                                    return@launch
                                }


                                when (
                                    cuenta
                                        .estado
                                        .uppercase()
                                ) {


                                    // =========================
                                    // APROBADA
                                    // =========================

                                    "APROBADA" -> {

                                        iniciandoSesion =
                                            false

                                        onIngresarUsuario(
                                            cuenta.correo
                                        )
                                    }


                                    // =========================
                                    // PENDIENTE
                                    // =========================

                                    "PENDIENTE" -> {

                                        auth.signOut()

                                        iniciandoSesion =
                                            false

                                        onCuentaEnRevision(
                                            cuenta.correo
                                        )
                                    }


                                    // =========================
                                    // RECHAZADA
                                    // =========================

                                    "RECHAZADA" -> {

                                        auth.signOut()

                                        iniciandoSesion =
                                            false

                                        onCuentaEnRevision(
                                            cuenta.correo
                                        )
                                    }


                                    else -> {

                                        auth.signOut()

                                        iniciandoSesion =
                                            false

                                        mensajeError =
                                            "El estado de la cuenta no es válido"
                                    }
                                }
                            }


                        } else {


                            iniciandoSesion =
                                false


                            mensajeError =
                                when (
                                    task.exception
                                ) {


                                    is FirebaseAuthInvalidCredentialsException ->

                                        "Correo o contraseña incorrectos"


                                    is FirebaseAuthInvalidUserException ->

                                        "No existe una cuenta registrada con este correo"


                                    else ->

                                        "No se pudo iniciar sesión. Revisa tus datos y tu conexión."
                                }
                        }
                    }
            },

            enabled =
                !iniciandoSesion,

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
                        iniciandoSesion
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