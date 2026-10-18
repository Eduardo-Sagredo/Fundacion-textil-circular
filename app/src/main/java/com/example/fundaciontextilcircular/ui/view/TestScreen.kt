package com.example.fundaciontextilcircular.ui.view

import android.Manifest
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
import com.example.fundaciontextilcircular.data.utils.LocationHelper
import com.example.fundaciontextilcircular.ui.viewmodel.TestViewModel

@Composable
fun TestScreen(viewModel: TestViewModel) {
    val listaEmprendedoras by viewModel.emprendedorasGuardadas.collectAsState()
    val context = LocalContext.current

    var rutaFinalImagen by remember { mutableStateOf<String?>(null) }
    var tempCamaraUri by remember { mutableStateOf<Uri?>(null) }

    // Estados para almacenar la ubicación sugerida obtenida
    var comunaSugerida by remember { mutableStateOf<String?>(null) }
    var regionSugerida by remember { mutableStateOf<String?>(null) }
    var mensajeErrorGps by remember { mutableStateOf<String?>(null) }

    // Lanzadores de imágenes
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            rutaFinalImagen = ImageCompressor.comprimirYGuardar(context, uri)
        }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exitoso ->
        if (exitoso && tempCamaraUri != null) {
            rutaFinalImagen = ImageCompressor.comprimirYGuardar(context, tempCamaraUri!!)
        }
    }

    // Lanzador para solicitar permisos de ubicación al usuario
    val permisoUbicacionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        val concedido = permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (concedido) {
            LocationHelper.obtenerUbicacionSugerida(
                context = context,
                onResultado = { comuna, region ->
                    comunaSugerida = comuna
                    regionSugerida = region
                    mensajeErrorGps = null
                },
                onError = { error -> mensajeErrorGps = error }
            )
        } else {
            mensajeErrorGps = "Permiso de ubicación denegado por el usuario."
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // --- SECCIÓN BASE DE DATOS ---
        Button(onClick = { viewModel.insertarEmprendedoraFalsa() }) {
            Text("Guardar Emprendedora en BD")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- SECCIÓN IMÁGENES ---
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

        if (rutaFinalImagen != null) {
            Text(
                text = "¡Foto procesada!\nRuta: $rutaFinalImagen",
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- SECCIÓN GEOLOCALIZACIÓN ---
        Button(onClick = {
            // Solicita permisos de ubicación fina y gruesa
            permisoUbicacionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }) {
            Text("Obtener Ubicación Sugerida (GPS)")
        }

        if (comunaSugerida != null) {
            Text(
                text = "Ubicación detectada:\nComuna: $comunaSugerida\nRegión: $regionSugerida",
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (mensajeErrorGps != null) {
            Text(
                text = "$mensajeErrorGps",
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