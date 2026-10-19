package com.example.fundaciontextilcircular.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.ui.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
    chatId: String,
    nombreChat: String,
    viewModel: ChatViewModel,
    onVolver: () -> Unit
) {

    val historial by
    viewModel.historial.collectAsState()

    val isOnline by
    viewModel.isOnline.collectAsState()

    var textoMensaje by remember {
        mutableStateOf("")
    }


    // =========================
    // ABRIR CONVERSACIÓN
    // =========================

    LaunchedEffect(chatId) {

        viewModel.seleccionarChat(
            chatId
        )

        // Cuando entramos al chat,
        // los mensajes anteriores
        // se consideran leídos.

        viewModel.marcarChatComoLeido(
            chatId
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // =========================
        // CABECERA
        // =========================

        Row(
            modifier = Modifier
                .fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {

                    // Si estábamos viendo una respuesta,
                    // se marca como leída antes de salir.

                    viewModel.marcarChatComoLeido(
                        chatId
                    )

                    onVolver()
                }
            ) {

                Icon(
                    imageVector =
                        Icons.Default.ArrowBack,

                    contentDescription =
                        "Volver"
                )
            }


            Column {

                Text(
                    text =
                        nombreChat,

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge
                )


                Text(
                    text =
                        if (isOnline) {
                            "🟢 Conectado"
                        } else {
                            "🔴 Sin conexión"
                        },

                    color =
                        if (isOnline) {
                            Color(0xFF2E7D32)
                        } else {
                            Color.Red
                        },

                    style =
                        MaterialTheme
                            .typography
                            .bodySmall
                )
            }
        }


        // =========================
        // HISTORIAL
        // =========================

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(
                    top = 16.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    6.dp
                )
        ) {

            items(
                items =
                    historial,

                key = {
                        mensaje ->

                    mensaje.id
                }

            ) {
                    mensaje ->

                val alineacion =

                    if (
                        mensaje.esDeUsuario
                    ) {
                        Alignment.End
                    } else {
                        Alignment.Start
                    }


                val colorFondo =

                    if (
                        mensaje.esDeUsuario
                    ) {
                        Color(
                            0xFFD7F8C6
                        )
                    } else {
                        Color(
                            0xFFE0E0E0
                        )
                    }


                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth(),

                    horizontalAlignment =
                        alineacion
                ) {

                    Column(
                        modifier =
                            Modifier
                                .background(
                                    color =
                                        colorFondo,

                                    shape =
                                        RoundedCornerShape(
                                            12.dp
                                        )
                                )
                                .padding(
                                    12.dp
                                )
                    ) {

                        Text(
                            text =
                                mensaje.contenido,

                            color =
                                Color.Black
                        )


                        if (
                            mensaje.esDeUsuario
                        ) {

                            Text(
                                text =
                                    if (
                                        mensaje.estaPendiente
                                    ) {
                                        "Pendiente"
                                    } else {
                                        "Enviado"
                                    },

                                style =
                                    MaterialTheme
                                        .typography
                                        .labelSmall,

                                color =
                                    Color.DarkGray,

                                modifier =
                                    Modifier
                                        .align(
                                            Alignment.End
                                        )
                            )
                        }
                    }
                }
            }
        }


        // =========================
        // ESCRIBIR MENSAJE
        // =========================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value =
                    textoMensaje,

                onValueChange = {

                    textoMensaje =
                        it
                },

                modifier =
                    Modifier
                        .weight(1f),

                placeholder = {

                    Text(
                        "Escribe un mensaje..."
                    )
                }
            )


            Spacer(
                modifier =
                    Modifier.width(
                        8.dp
                    )
            )


            Button(
                onClick = {

                    viewModel
                        .enviarMensaje(
                            textoMensaje
                        )

                    textoMensaje =
                        ""
                },

                enabled =
                    textoMensaje
                        .isNotBlank()
            ) {

                Text(
                    "Enviar"
                )
            }
        }
    }
}