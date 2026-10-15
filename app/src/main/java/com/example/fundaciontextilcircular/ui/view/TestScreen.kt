package com.example.fundaciontextilcircular.ui.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.ui.viewmodel.TestViewModel

@Composable
fun TestScreen(viewModel: TestViewModel) {
    val listaEmprendedoras by viewModel.emprendedorasGuardadas.collectAsState()

    // 1. Variable para guardar la ruta de la foto seleccionada
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    // 2. El contrato que le pide a Android que abra su galería nativa segura
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        // Este bloque se ejecuta cuando la usuaria elige una foto o cancela
        imagenUri = uri
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // --- SECCIÓN DE BASE DE DATOS (Lo que ya tenías) ---
        Button(onClick = { viewModel.insertarEmprendedoraFalsa() }) {
            Text("Guardar Emprendedora en BD")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN DE HARDWARE: GALERÍA ---
        Button(onClick = {
            // Lanzamos la galería pidiendo exclusivamente imágenes (no videos)
            galeriaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Text("Abrir Galería")
        }

        // Mostramos la ruta de la imagen si se seleccionó alguna
        if (imagenUri != null) {
            Text(
                text = "¡Foto obtenida con éxito!\nRuta interna: $imagenUri",
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Emprendedoras en la BD:")
        LazyColumn {
            items(listaEmprendedoras) { emprendedora ->
                Text(text = "- ${emprendedora.nombre} - RUT: ${emprendedora.rut}")
            }
        }
    }
}