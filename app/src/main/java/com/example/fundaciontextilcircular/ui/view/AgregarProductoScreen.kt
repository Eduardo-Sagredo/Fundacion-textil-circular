package com.example.fundaciontextilcircular.ui.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.local.entities.ProductoEntity
import com.example.fundaciontextilcircular.data.utils.ImageCompressor
import kotlinx.coroutines.launch

@Composable
fun AgregarProductoScreen(
    correoUsuario: String,
    onProductoPublicado: () -> Unit,
    onVolver: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val database = remember(context) {
        TextilDatabase.getDatabase(context)
    }

    val productoDao = remember(database) {
        database.productoDao()
    }

    val emprendedoraDao = remember(database) {
        database.emprendedoraDao()
    }


    // =========================
    // CAMPOS DEL FORMULARIO
    // =========================

    var nombre by remember {
        mutableStateOf("")
    }

    var descripcion by remember {
        mutableStateOf("")
    }

    var precio by remember {
        mutableStateOf("")
    }

    var tipo by remember {
        mutableStateOf("PRODUCTO")
    }

    var rutaFinalImagen by remember {
        mutableStateOf<String?>(null)
    }

    var tempCamaraUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var mensajeError by remember {
        mutableStateOf<String?>(null)
    }

    var guardando by remember {
        mutableStateOf(false)
    }


    // =========================
    // GALERÍA
    // =========================

    val galeriaLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            if (uri != null) {

                rutaFinalImagen =
                    ImageCompressor.comprimirYGuardar(
                        context,
                        uri
                    )

                if (rutaFinalImagen == null) {
                    mensajeError =
                        "No se pudo procesar la imagen."
                }
            }
        }


    // =========================
    // CÁMARA
    // =========================

    val camaraLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.TakePicture()
        ) { exitoso ->

            if (
                exitoso &&
                tempCamaraUri != null
            ) {

                rutaFinalImagen =
                    ImageCompressor.comprimirYGuardar(
                        context,
                        tempCamaraUri!!
                    )

                if (rutaFinalImagen == null) {
                    mensajeError =
                        "No se pudo procesar la fotografía."
                }
            }
        }


    // =========================
    // PERMISO CÁMARA
    // =========================

    val permisoCamaraLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestPermission()
        ) { concedido ->

            if (concedido) {

                val uri =
                    ImageCompressor
                        .crearUriTemporalParaCamara(
                            context
                        )

                tempCamaraUri = uri

                camaraLauncher.launch(uri)

            } else {

                mensajeError =
                    "Necesitas permitir el acceso a la cámara."
            }
        }


    // =========================
    // IMAGEN PARA PREVISUALIZAR
    // =========================

    val bitmap =
        remember(rutaFinalImagen) {

            rutaFinalImagen?.let { ruta ->

                BitmapFactory
                    .decodeFile(ruta)
                    ?.asImageBitmap()
            }
        }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp)
    ) {

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Text(
            text =
                "Nueva publicación",
            style =
                MaterialTheme
                    .typography
                    .headlineMedium,
            fontWeight =
                FontWeight.Bold
        )

        Text(
            text =
                "Publica un producto o servicio en el catálogo.",
            color =
                Color.Gray,
            style =
                MaterialTheme
                    .typography
                    .bodyMedium
        )


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        // =========================
        // NOMBRE
        // =========================

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
            },
            modifier =
                Modifier.fillMaxWidth(),
            label = {
                Text(
                    "Nombre"
                )
            },
            placeholder = {
                Text(
                    "Ej: Bolso reciclado"
                )
            },
            singleLine = true
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // =========================
        // TIPO
        // =========================

        Text(
            text =
                "Tipo de publicación",
            fontWeight =
                FontWeight.SemiBold
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    10.dp
                )
        ) {

            Button(
                onClick = {
                    tipo = "PRODUCTO"
                },
                modifier =
                    Modifier.weight(1f),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            if (
                                tipo == "PRODUCTO"
                            ) {
                                Color(
                                    0xFF00A6A6
                                )
                            } else {
                                Color.Gray
                            }
                    )
            ) {

                Text(
                    "Producto"
                )
            }


            Button(
                onClick = {
                    tipo = "SERVICIO"
                },
                modifier =
                    Modifier.weight(1f),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            if (
                                tipo == "SERVICIO"
                            ) {
                                Color(
                                    0xFF00A6A6
                                )
                            } else {
                                Color.Gray
                            }
                    )
            ) {

                Text(
                    "Servicio"
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // =========================
        // DESCRIPCIÓN
        // =========================

        OutlinedTextField(
            value =
                descripcion,
            onValueChange = {
                descripcion = it
            },
            modifier =
                Modifier.fillMaxWidth(),
            label = {
                Text(
                    "Descripción"
                )
            },
            placeholder = {
                Text(
                    "Describe tu producto o servicio"
                )
            },
            minLines = 3,
            maxLines = 5
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // =========================
        // PRECIO
        // =========================

        OutlinedTextField(
            value =
                precio,
            onValueChange = { nuevoValor ->

                if (
                    nuevoValor.all {
                        it.isDigit()
                    }
                ) {
                    precio = nuevoValor
                }
            },
            modifier =
                Modifier.fillMaxWidth(),
            label = {
                Text(
                    "Precio"
                )
            },
            prefix = {
                Text(
                    "$"
                )
            },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Number
                ),
            singleLine = true
        )


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        // =========================
        // IMAGEN
        // =========================

        Text(
            text =
                "Imagen",
            style =
                MaterialTheme
                    .typography
                    .titleMedium,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    10.dp
                )
        ) {

            OutlinedButton(
                onClick = {

                    galeriaLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts
                                .PickVisualMedia
                                .ImageOnly
                        )
                    )
                },
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    "Galería"
                )
            }


            OutlinedButton(
                onClick = {

                    val permisoConcedido =

                        ContextCompat
                            .checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) ==
                                PackageManager.PERMISSION_GRANTED


                    if (permisoConcedido) {

                        val uri =
                            ImageCompressor
                                .crearUriTemporalParaCamara(
                                    context
                                )

                        tempCamaraUri =
                            uri

                        camaraLauncher
                            .launch(
                                uri
                            )

                    } else {

                        permisoCamaraLauncher
                            .launch(
                                Manifest.permission.CAMERA
                            )
                    }
                },
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    "Cámara"
                )
            }
        }


        // =========================
        // PREVISUALIZACIÓN
        // =========================

        bitmap?.let { imagen ->

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(
                        16.dp
                    ),
                elevation =
                    CardDefaults
                        .cardElevation(
                            defaultElevation =
                                3.dp
                        )
            ) {

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                12.dp
                            ),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Image(
                        bitmap =
                            imagen,
                        contentDescription =
                            "Imagen seleccionada",
                        modifier =
                            Modifier
                                .size(
                                    220.dp
                                )
                                .clip(
                                    RoundedCornerShape(
                                        12.dp
                                    )
                                )
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )

                    Text(
                        text =
                            "Imagen seleccionada",
                        color =
                            Color.Gray
                    )
                }
            }
        }


        // =========================
        // ERROR
        // =========================

        mensajeError?.let { error ->

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            Text(
                text =
                    error,
                color =
                    MaterialTheme
                        .colorScheme
                        .error
            )
        }


        Spacer(
            modifier =
                Modifier.height(
                    24.dp
                )
        )


        // =========================
        // PUBLICAR
        // =========================

        Button(
            onClick = {

                mensajeError =
                    null

                val precioNumero =
                    precio.toIntOrNull()


                when {

                    nombre.isBlank() -> {

                        mensajeError =
                            "Debes ingresar un nombre."
                    }


                    descripcion.isBlank() -> {

                        mensajeError =
                            "Debes ingresar una descripción."
                    }


                    precioNumero == null ||
                            precioNumero <= 0 -> {

                        mensajeError =
                            "Debes ingresar un precio válido."
                    }


                    rutaFinalImagen == null -> {

                        mensajeError =
                            "Debes seleccionar o tomar una imagen."
                    }


                    else -> {

                        guardando =
                            true

                        scope.launch {

                            val emprendedora =
                                emprendedoraDao
                                    .obtenerPorCorreo(
                                        correoUsuario
                                            .trim()
                                            .lowercase()
                                    )


                            if (
                                emprendedora == null
                            ) {

                                mensajeError =
                                    "No se encontraron los datos de la emprendedora."

                                guardando =
                                    false

                                return@launch
                            }


                            val nuevaPublicacion =
                                ProductoEntity(

                                    nombre =
                                        nombre.trim(),

                                    descripcion =
                                        descripcion.trim(),

                                    precio =
                                        precioNumero,

                                    tipo =
                                        tipo,

                                    imagenRuta =
                                        rutaFinalImagen,

                                    correoEmprendedora =
                                        emprendedora.correo,

                                    nombreEmprendimiento =
                                        emprendedora
                                            .nombreEmprendimiento
                                )


                            productoDao
                                .insertarProducto(
                                    nuevaPublicacion
                                )


                            guardando =
                                false

                            onProductoPublicado()
                        }
                    }
                }
            },
            enabled =
                !guardando,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(
                        55.dp
                    ),
            colors =
                ButtonDefaults
                    .buttonColors(
                        containerColor =
                            Color(
                                0xFF00A6A6
                            )
                    )
        ) {

            Text(
                text =
                    if (guardando) {
                        "PUBLICANDO..."
                    } else {
                        "PUBLICAR"
                    },
                fontWeight =
                    FontWeight.Bold
            )
        }


        Spacer(
            modifier =
                Modifier.height(
                    10.dp
                )
        )


        OutlinedButton(
            onClick =
                onVolver,
            modifier =
                Modifier
                    .fillMaxWidth()
        ) {

            Text(
                "Cancelar"
            )
        }


        Spacer(
            modifier =
                Modifier.height(
                    30.dp
                )
        )
    }
}