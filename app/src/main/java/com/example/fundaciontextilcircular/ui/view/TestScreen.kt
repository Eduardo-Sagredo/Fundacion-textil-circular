package com.example.fundaciontextilcircular.ui.view

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.data.utils.ImageCompressor
import com.example.fundaciontextilcircular.ui.viewmodel.TestViewModel

@Composable
fun TestScreen(viewModel: TestViewModel) {
    val listaEmprendedoras by viewModel.emprendedorasGuardadas.collectAsState()

    // 1. Obtenemos el contexto actual para interactuar con los archivos internos del dispositivo
    val context = LocalContext.current

    // 2. Estado para almacenar la ruta absoluta permanente del archivo ya comprimido
    var rutaFinalImagen by remember { mutableStateOf<String?>(null) }

    // 3. Contrato para abrir la galería y procesar la imagen seleccionada
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            // Se comprime la imagen y se guarda permanentemente en la memoria interna
            rutaFinalImagen = ImageCompressor.comprimirYGuardar(context, uri)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // --- SECCIÓN DE BASE DE DATOS ---
        Button(onClick = { viewModel.insertarEmprendedoraFalsa() }) {
            Text("Guardar Emprendedora en BD")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN DE HARDWARE: GALERÍA Y COMPRESIÓN ---
        Button(onClick = {
            galeriaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Text("Abrir Galería")
        }

        // Muestra la ruta física permanente guardada en app storage (/data/user/0/...)
        if (rutaFinalImagen != null) {
            Text(
                text = "¡Foto comprimida y guardada con éxito!\nRuta permanente: $rutaFinalImagen",
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