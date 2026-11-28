package com.example.fundaciontextilcircular.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.ui.theme.AmarilloTextil
import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.RojoErrorTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil

@Composable
fun PantallaCuentaEnRevision(
    correo: String,
    modifier: Modifier = Modifier,
    onVolverInicioSesion: () -> Unit = {}
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


    val estado =
        cuenta
            ?.estado
            ?.uppercase()
            ?: "PENDIENTE"


    val colorEstado: Color =
        when (estado) {

            "APROBADA" -> {
                TurquesaTextil
            }

            "RECHAZADA" -> {
                RojoErrorTextil
            }

            else -> {
                AmarilloTextil
            }
        }


    val titulo =
        when (estado) {

            "APROBADA" -> {
                "APROBADA"
            }

            "RECHAZADA" -> {
                "RECHAZADA"
            }

            else -> {
                "EN REVISIÓN"
            }
        }


    val mensaje =
        when (estado) {

            "APROBADA" -> {
                "Tu cuenta fue aprobada. Ya puedes iniciar sesión."
            }

            "RECHAZADA" -> {
                "Tu solicitud fue rechazada por la administración."
            }

            else -> {
                "Tu registro fue enviado correctamente."
            }
        }


    val motivoRechazo =
        cuenta
            ?.motivoRechazo
            ?.takeIf {
                it.isNotBlank()
            }
            ?: "No se indicó un motivo."


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NegroTextil)
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {


        Spacer(
            modifier =
                Modifier.height(80.dp)
        )


        Text(
            text = "CUENTA",

            color =
                BlancoTextil,

            style =
                MaterialTheme
                    .typography
                    .headlineLarge,

            textAlign =
                TextAlign.Center
        )


        Text(
            text = titulo,

            color =
                colorEstado,

            style =
                MaterialTheme
                    .typography
                    .headlineLarge,

            textAlign =
                TextAlign.Center
        )


        Spacer(
            modifier =
                Modifier.height(30.dp)
        )


        Text(
            text = mensaje,

            color =
                BlancoTextil,

            style =
                MaterialTheme
                    .typography
                    .titleMedium,

            textAlign =
                TextAlign.Center
        )


        if (
            estado ==
            "PENDIENTE"
        ) {

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            Text(
                text =
                    "La información de tu cuenta será revisada antes de habilitar tu acceso a Textil Circular.",

                color =
                    BlancoTextil,

                style =
                    MaterialTheme
                        .typography
                        .bodyLarge,

                textAlign =
                    TextAlign.Center
            )
        }


        Spacer(
            modifier =
                Modifier.height(32.dp)
        )


        Surface(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(
                    12.dp
                ),

            color =
                colorEstado
        ) {


            Column(
                modifier =
                    Modifier.padding(
                        20.dp
                    ),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {


                Text(
                    text =
                        "ESTADO DE LA CUENTA",

                    color =
                        NegroTextil,

                    style =
                        MaterialTheme
                            .typography
                            .labelLarge,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )


                Text(
                    text =
                        when (estado) {

                            "APROBADA" -> {
                                "Cuenta activa"
                            }

                            "RECHAZADA" -> {
                                "Solicitud rechazada"
                            }

                            else -> {
                                "Pendiente de validación"
                            }
                        },

                    color =
                        NegroTextil,

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,

                    fontWeight =
                        FontWeight.Bold,

                    textAlign =
                        TextAlign.Center
                )


                // MOTIVO DEL RECHAZO

                if (
                    estado ==
                    "RECHAZADA"
                ) {


                    Spacer(
                        modifier =
                            Modifier.height(
                                22.dp
                            )
                    )


                    Text(
                        text =
                            "Motivo del rechazo:",

                        color =
                            NegroTextil,

                        style =
                            MaterialTheme
                                .typography
                                .bodyLarge,

                        fontWeight =
                            FontWeight.Bold,

                        textAlign =
                            TextAlign.Center
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )


                    Text(
                        text =
                            motivoRechazo,

                        color =
                            NegroTextil,

                        style =
                            MaterialTheme
                                .typography
                                .bodyLarge,

                        textAlign =
                            TextAlign.Center
                    )
                }
            }
        }


        Spacer(
            modifier =
                Modifier.height(40.dp)
        )


        Button(
            onClick =
                onVolverInicioSesion,

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
                        estado ==
                        "APROBADA"
                    ) {

                        "IR A INICIAR SESIÓN"

                    } else {

                        "VOLVER A INICIAR SESIÓN"
                    },

                style =
                    MaterialTheme
                        .typography
                        .labelLarge
            )
        }
    }
}