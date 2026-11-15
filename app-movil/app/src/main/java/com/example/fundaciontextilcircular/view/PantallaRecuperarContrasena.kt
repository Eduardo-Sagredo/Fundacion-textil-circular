package com.example.fundaciontextilcircular.view

import android.util.Patterns
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.RojoErrorTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaRecuperarContrasena(
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {}
) {

    val auth = remember {
        FirebaseAuth.getInstance()
    }

    var correo by remember {
        mutableStateOf("")
    }

    var enviando by remember {
        mutableStateOf(false)
    }

    var mensajeError by remember {
        mutableStateOf<String?>(null)
    }

    var mensajeExito by remember {
        mutableStateOf<String?>(null)
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NegroTextil)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp)
    ) {

        Spacer(
            modifier = Modifier.height(70.dp)
        )


        Text(
            text = "RECUPERAR",
            color = BlancoTextil,
            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )


        Text(
            text = "CONTRASEÑA",
            color = TurquesaTextil,
            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        Text(
            text =
                "Ingresa el correo asociado a tu cuenta. " +
                        "Te enviaremos un enlace para crear una nueva contraseña.",

            color = BlancoTextil,

            style =
                MaterialTheme
                    .typography
                    .bodyLarge
        )


        Spacer(
            modifier = Modifier.height(32.dp)
        )


        OutlinedTextField(
            value = correo,

            onValueChange = {

                correo = it

                mensajeError = null
                mensajeExito = null
            },

            modifier =
                Modifier.fillMaxWidth(),

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

            singleLine = true,

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Email
                ),

            colors =
                coloresCampoRegistro()
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        mensajeError?.let {

            Text(
                text = it,
                color = RojoErrorTextil,
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium
            )


            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )
        }


        mensajeExito?.let {

            Text(
                text = it,
                color = TurquesaTextil,
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium
            )


            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )
        }


        Button(
            onClick = {

                val correoNormalizado =
                    correo
                        .trim()
                        .lowercase()


                mensajeError = null
                mensajeExito = null


                if (
                    correoNormalizado
                        .isBlank()
                ) {

                    mensajeError =
                        "Debes ingresar tu correo electrónico"

                    return@Button
                }


                if (
                    !Patterns
                        .EMAIL_ADDRESS
                        .matcher(
                            correoNormalizado
                        )
                        .matches()
                ) {

                    mensajeError =
                        "Ingresa un correo electrónico válido"

                    return@Button
                }


                enviando = true


                auth
                    .sendPasswordResetEmail(
                        correoNormalizado
                    )
                    .addOnCompleteListener { task ->

                        enviando = false


                        if (
                            task.isSuccessful
                        ) {

                            mensajeExito =
                                "Si el correo está asociado a una cuenta, " +
                                        "recibirás un enlace para restablecer tu contraseña."

                        } else {

                            mensajeError =
                                "No se pudo enviar el correo. " +
                                        "Revisa tu conexión e inténtalo nuevamente."
                        }
                    }
            },

            enabled =
                !enviando,

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
                        enviando
                    ) {

                        "ENVIANDO..."

                    } else {

                        "ENVIAR ENLACE"
                    },

                style =
                    MaterialTheme
                        .typography
                        .labelLarge
            )
        }


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        TextButton(
            onClick =
                onVolver,

            modifier =
                Modifier.fillMaxWidth()
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