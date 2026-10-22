package com.example.fundaciontextilcircular.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExtendedFloatingActionButton

data class ChatResumenUI(

    val chatId: String,

    val nombre: String,

    val descripcion: String
)


@Composable
fun ListaChatsScreen(

    correoUsuario: String,

    onAbrirChat: (
        chatId: String,
        nombreChat: String
    ) -> Unit
) {

    val context =
        LocalContext.current


    // =========================
    // DAOS
    // =========================

    val database =
        remember(context) {

            TextilDatabase
                .getDatabase(
                    context
                )
        }


    val emprendedoraDao =
        remember(database) {

            database
                .emprendedoraDao()
        }


    val mensajeDao =
        remember(database) {

            database
                .mensajeDao()
        }


    // =========================
    // EMPRENDEDORAS APROBADAS
    // =========================

    val emprendedorasAprobadas by

    emprendedoraDao
        .obtenerAprobadas()
        .collectAsState(
            initial =
                emptyList()
        )


    // =========================
    // NO MOSTRARME A MÍ MISMO
    // =========================

    val emprendedorasDisponibles =

        emprendedorasAprobadas
            .filter { emprendedora ->

                emprendedora
                    .correo
                    .trim()
                    .lowercase() !=

                        correoUsuario
                            .trim()
                            .lowercase()
            }


    // =========================
    // CREAR CHATS
    // =========================

    val conversaciones =

        emprendedorasDisponibles
            .map { emprendedora ->

                ChatResumenUI(

                    chatId =
                        generarChatId(
                            correoUsuario,
                            emprendedora.correo
                        ),

                    nombre =
                        emprendedora
                            .nombreEmprendimiento,

                    descripcion =
                        "${emprendedora.rubro} • ${emprendedora.comuna}"
                )
            }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // =
            // TÍTULO
            //
            Text(
                text = "Mis conversaciones",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, bottom = 4.dp)
            )

            Text(
                text = "Selecciona una emprendedora",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            //
            // SIN CONVERSACIONES
            //
            if (conversaciones.isEmpty()) {
                Text(
                    text = "No hay emprendedoras disponibles para conversar.",
                    color = Color.Gray
                )
            } else {
                // LISTA DE CHATS
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = conversaciones,
                        key = { chat -> chat.chatId }
                    ) { chat ->
                        val mensajesNoLeidos by mensajeDao
                            .observarCantidadNoLeidos(chat.chatId)
                            .collectAsState(initial = 0)

                        val ultimoMensaje by mensajeDao
                            .observarUltimoMensaje(chat.chatId)
                            .collectAsState(initial = null)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAbrirChat(chat.chatId, chat.nombre)
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // FOTO / INICIAL
                                Surface(
                                    modifier = Modifier.width(48.dp).height(48.dp),
                                    shape = CircleShape,
                                    color = Color(0xFF00A6A6)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = chat.nombre.take(1).uppercase(),
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                // INFORMACIÓN
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = chat.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = if (mensajesNoLeidos > 0) FontWeight.Bold else FontWeight.SemiBold,
                                        color = Color.Black
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // ÚLTIMO MENSAJE
                                    Text(
                                        text = ultimoMensaje?.contenido ?: chat.descripcion,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (mensajesNoLeidos > 0) Color.Black else Color.Gray,
                                        fontWeight = if (mensajesNoLeidos > 0) FontWeight.Bold else FontWeight.Normal,
                                        maxLines = 1
                                    )
                                }

                                // AVISO MENSAJE NUEVO
                                if (mensajesNoLeidos > 0) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFFFFC107)
                                    ) {
                                        Text(
                                            text = mensajesNoLeidos.toString(),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // =========================
        // BOTÓN COMUNICARSE CON ADMINISTRADOR
        // =========================
        ExtendedFloatingActionButton(
            onClick = {
                // Crea un ID único para el chat con el administrador
                val chatIdAdmin = "chat_admin_${correoUsuario.replace(".", "_")}"
                onAbrirChat(chatIdAdmin, "Administrador")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF00A6A6), // Turquesa
            contentColor = Color.White
        ) {
            Text("Comunicarse con un administrador", fontWeight = FontWeight.Bold)
        }
    }
}


// =============================================
// CREAR ID ÚNICO ENTRE DOS USUARIOS
// =============================================

private fun generarChatId(

    correoA: String,

    correoB: String

): String {

    val correos =
        listOf(

            correoA
                .trim()
                .lowercase(),

            correoB
                .trim()
                .lowercase()
        )
            .sorted()


    return correos
        .joinToString(
            separator =
                "__"
        )
}