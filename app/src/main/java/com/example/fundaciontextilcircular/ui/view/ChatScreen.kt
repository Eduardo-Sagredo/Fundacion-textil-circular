package com.example.fundaciontextilcircular.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.ui.viewmodel.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val historial by viewModel.historial.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    var textoMensaje by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 1. Cabecera con estado de red
        Text(
            text = if (isOnline) "🟢 Conectado" else "🔴 Sin conexión (Modo Offline)",
            color = if (isOnline) Color(0xFF2E7D32) else Color.Red, // Verde oscuro o Rojo
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp, top = 24.dp)
        )

        // 2. Historial de mensajes
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(historial) { mensaje ->
                val alineacion = if (mensaje.esDeUsuario) Alignment.End else Alignment.Start
                val colorFondo = if (mensaje.esDeUsuario) Color(0xFFDCF8C6) else Color(0xFFE0E0E0)
                val estado = if (mensaje.esDeUsuario) {
                    if (mensaje.estaPendiente) " 🕒" else " ✓✓"
                } else ""

                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalAlignment = alineacion
                ) {
                    Text(
                        text = "${mensaje.contenido}$estado",
                        modifier = Modifier
                            .background(colorFondo, shape = RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        color = Color.Black
                    )
                }
            }
        }

        // 3. Input y Botón de Enviar
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = textoMensaje,
                onValueChange = { textoMensaje = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe tu duda aquí...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                viewModel.enviarMensaje(textoMensaje)
                textoMensaje = "" // Limpiamos la caja de texto
            }) {
                Text("Enviar")
            }
        }
    }
}