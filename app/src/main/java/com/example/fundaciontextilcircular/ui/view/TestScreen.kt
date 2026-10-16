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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.data.utils.ImageCompressor
import com.example.fundaciontextilcircular.data.utils.ImageCompressor.crearUriTemporalParaCamara
import com.example.fundaciontextilcircular.ui.viewmodel.TestViewModel

@Composable
fun TestScreen(viewModel: TestViewModel) {
    val listaEmprendedoras by viewModel.emprendedorasGuardadas.collectAsState()
    val context = LocalContext.current

    // Ruta física permanente de la imagen comprimida
    var rutaFinalImagen by remember { mutableStateOf<String?>(null) }

    // Uri temporal para la captura de la cámara
    var tempCamaraUri by remember { mutableStateOf<Uri?>(null) }

    // 1. Lanzador de Galería
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            rutaFinalImagen = ImageCompressor.comprimirYGuardar(context, uri)
        }
    }

    // 2. Lanzador de Cámara
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exitoso ->
        if (exitoso && tempCamaraUri != null) {
            // Comprimimos la imagen capturada por la cámara y la guardamos en almacenamiento permanente
            rutaFinalImagen = ImageCompressor.comprimirYGuardar(context, tempCamaraUri!!)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // --- SECCIÓN BD ---
        Button(onClick = { viewModel.insertarEmprendedoraFalsa() }) {
            Text("Guardar Emprendedora en BD")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN HARDWARE: GALERÍA Y CÁMARA ---
        Button(onClick = {
            galeriaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Text("Abrir Galería")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val uri = crearUriTemporalParaCamara(context)
            tempCamaraUri = uri
            camaraLauncher.launch(uri)
        }) {
            Text("Tomar Foto con Cámara")
        }

        // Muestra de resultados
        if (rutaFinalImagen != null) {
            Text(
                text = "¡Foto procesada y guardada con éxito!\nRuta permanente: $rutaFinalImagen",
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