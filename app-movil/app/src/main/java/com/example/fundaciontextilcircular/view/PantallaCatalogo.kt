package com.example.fundaciontextilcircular.view

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.local.entities.ProductoEntity
import kotlinx.coroutines.launch

@Composable
fun PantallaCatalogo(
    correoUsuario: String,
    modifier: Modifier = Modifier,
    onAgregarProducto: () -> Unit = {}
) {

    val context =
        LocalContext.current

    val scope =
        rememberCoroutineScope()

    val productoDao =
        remember(context) {

            TextilDatabase
                .getDatabase(context)
                .productoDao()
        }


    // =============================================
    // PRODUCTOS ROOM
    // =============================================

    val productos by
    productoDao
        .obtenerTodos()
        .collectAsState(
            initial = emptyList()
        )


    // =============================================
    // BUSCADOR
    // =============================================

    var busqueda by remember {
        mutableStateOf("")
    }


    // =============================================
    // ELIMINAR
    // =============================================

    var productoAEliminar by remember {
        mutableStateOf<ProductoEntity?>(null)
    }


    // =============================================
    // EDITAR
    // =============================================

    var productoAEditar by remember {
        mutableStateOf<ProductoEntity?>(null)
    }

    var nombreEditado by remember {
        mutableStateOf("")
    }

    var descripcionEditada by remember {
        mutableStateOf("")
    }

    var precioEditado by remember {
        mutableStateOf("")
    }

    var tipoEditado by remember {
        mutableStateOf("PRODUCTO")
    }

    var errorEdicion by remember {
        mutableStateOf<String?>(null)
    }


    // =============================================
    // FILTRAR
    // =============================================

    val productosFiltrados =
        productos.filter { producto ->

            producto.nombre.contains(
                busqueda,
                ignoreCase = true
            ) ||

                    producto.descripcion.contains(
                        busqueda,
                        ignoreCase = true
                    ) ||

                    producto.nombreEmprendimiento.contains(
                        busqueda,
                        ignoreCase = true
                    ) ||

                    producto.tipo.contains(
                        busqueda,
                        ignoreCase = true
                    )
        }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Catálogo",
            style =
                MaterialTheme
                    .typography
                    .headlineMedium,
            fontWeight =
                FontWeight.Bold,
            modifier =
                Modifier.padding(
                    top = 20.dp
                )
        )

        Text(
            text =
                "Productos y servicios de nuestras emprendedoras",
            color =
                Color.Gray
        )


        Spacer(
            modifier =
                Modifier.height(14.dp)
        )


        // =============================================
        // NUEVA PUBLICACIÓN
        // =============================================

        Button(
            onClick =
                onAgregarProducto,

            modifier =
                Modifier.fillMaxWidth(),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        Color(0xFF00A6A6)
                )
        ) {

            Icon(
                imageVector =
                    Icons.Default.Add,
                contentDescription =
                    "Nueva publicación"
            )

            Text(
                text =
                    " Nueva publicación",
                fontWeight =
                    FontWeight.Bold
            )
        }


        Spacer(
            modifier =
                Modifier.height(14.dp)
        )


        // =============================================
        // BUSCAR
        // =============================================

        OutlinedTextField(
            value =
                busqueda,

            onValueChange = {
                busqueda = it
            },

            modifier =
                Modifier.fillMaxWidth(),

            leadingIcon = {

                Icon(
                    imageVector =
                        Icons.Default.Search,
                    contentDescription =
                        "Buscar"
                )
            },

            placeholder = {

                Text(
                    "Buscar producto, servicio o emprendimiento"
                )
            },

            singleLine =
                true
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        Text(
            text =
                "${productosFiltrados.size} publicaciones",

            fontWeight =
                FontWeight.SemiBold
        )


        Spacer(
            modifier =
                Modifier.height(10.dp)
        )


        // =============================================
        // SIN PRODUCTOS
        // =============================================

        if (
            productosFiltrados.isEmpty()
        ) {

            Text(
                text =
                    if (
                        busqueda.isBlank()
                    ) {
                        "Todavía no hay publicaciones."
                    } else {
                        "No se encontraron resultados."
                    },

                color =
                    Color.Gray,

                modifier =
                    Modifier.padding(
                        top = 30.dp
                    )
            )

        } else {

            LazyColumn(
                verticalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                items(
                    items =
                        productosFiltrados,

                    key = {
                        it.id
                    }

                ) { producto ->

                    val esMia =
                        producto
                            .correoEmprendedora
                            .trim()
                            .lowercase() ==
                                correoUsuario
                                    .trim()
                                    .lowercase()


                    TarjetaProductoCatalogo(
                        producto =
                            producto,

                        esMia =
                            esMia,

                        onEditar = {

                            productoAEditar =
                                producto

                            nombreEditado =
                                producto.nombre

                            descripcionEditada =
                                producto.descripcion

                            precioEditado =
                                producto.precio
                                    .toString()

                            tipoEditado =
                                producto.tipo

                            errorEdicion =
                                null
                        },

                        onCambiarEstado = {

                            scope.launch {

                                if (
                                    producto.estado ==
                                    "VENDIDO"
                                ) {

                                    productoDao
                                        .marcarComoDisponible(
                                            producto.id
                                        )

                                } else {

                                    productoDao
                                        .marcarComoVendido(
                                            producto.id
                                        )
                                }
                            }
                        },

                        onEliminar = {

                            productoAEliminar =
                                producto
                        }
                    )
                }


                item {

                    Spacer(
                        modifier =
                            Modifier.height(
                                20.dp
                            )
                    )
                }
            }
        }
    }


    // =============================================
    // DIÁLOGO ELIMINAR
    // =============================================

    productoAEliminar?.let {
            producto ->

        AlertDialog(

            onDismissRequest = {

                productoAEliminar =
                    null
            },

            title = {

                Text(
                    "Eliminar publicación"
                )
            },

            text = {

                Text(
                    "¿Estás seguro de que deseas eliminar \"${producto.nombre}\"?"
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        scope.launch {

                            productoDao
                                .eliminarProducto(
                                    producto
                                )

                            productoAEliminar =
                                null
                        }
                    },

                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    Color(
                                        0xFFD32F2F
                                    )
                            )
                ) {

                    Text(
                        "Eliminar"
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {

                        productoAEliminar =
                            null
                    }
                ) {

                    Text(
                        "Cancelar"
                    )
                }
            }
        )
    }


    // =============================================
    // DIÁLOGO EDITAR
    // =============================================

    productoAEditar?.let {
            producto ->

        AlertDialog(

            onDismissRequest = {

                productoAEditar =
                    null

                errorEdicion =
                    null
            },

            title = {

                Text(
                    "Editar publicación"
                )
            },

            text = {

                Column {

                    OutlinedTextField(
                        value =
                            nombreEditado,

                        onValueChange = {
                            nombreEditado = it
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text(
                                "Nombre"
                            )
                        },

                        singleLine =
                            true
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                10.dp
                            )
                    )


                    OutlinedTextField(
                        value =
                            descripcionEditada,

                        onValueChange = {
                            descripcionEditada = it
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text(
                                "Descripción"
                            )
                        },

                        minLines =
                            3
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                10.dp
                            )
                    )


                    OutlinedTextField(
                        value =
                            precioEditado,

                        onValueChange = {
                                nuevoValor ->

                            if (
                                nuevoValor.all {
                                    it.isDigit()
                                }
                            ) {

                                precioEditado =
                                    nuevoValor
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
                            Text("$")
                        },

                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Number
                            ),

                        singleLine =
                            true
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                12.dp
                            )
                    )


                    Text(
                        text =
                            "Tipo",
                        fontWeight =
                            FontWeight.SemiBold
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                6.dp
                            )
                    )


                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.spacedBy(
                                8.dp
                            )
                    ) {

                        Button(
                            onClick = {

                                tipoEditado =
                                    "PRODUCTO"
                            },

                            modifier =
                                Modifier.weight(1f),

                            colors =
                                ButtonDefaults
                                    .buttonColors(

                                        containerColor =
                                            if (
                                                tipoEditado ==
                                                "PRODUCTO"
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

                                tipoEditado =
                                    "SERVICIO"
                            },

                            modifier =
                                Modifier.weight(1f),

                            colors =
                                ButtonDefaults
                                    .buttonColors(

                                        containerColor =
                                            if (
                                                tipoEditado ==
                                                "SERVICIO"
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


                    errorEdicion?.let {
                            error ->

                        Spacer(
                            modifier =
                                Modifier.height(
                                    8.dp
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
                }
            },

            confirmButton = {

                Button(
                    onClick = {

                        val precioNumero =
                            precioEditado
                                .toIntOrNull()


                        when {

                            nombreEditado
                                .isBlank() -> {

                                errorEdicion =
                                    "Ingresa un nombre."
                            }


                            descripcionEditada
                                .isBlank() -> {

                                errorEdicion =
                                    "Ingresa una descripción."
                            }


                            precioNumero == null ||
                                    precioNumero <= 0 -> {

                                errorEdicion =
                                    "Ingresa un precio válido."
                            }


                            else -> {

                                scope.launch {

                                    productoDao
                                        .actualizarProducto(

                                            id =
                                                producto.id,

                                            nombre =
                                                nombreEditado
                                                    .trim(),

                                            descripcion =
                                                descripcionEditada
                                                    .trim(),

                                            precio =
                                                precioNumero,

                                            tipo =
                                                tipoEditado,

                                            // Por ahora conserva
                                            // la misma foto.
                                            imagenRuta =
                                                producto
                                                    .imagenRuta
                                        )


                                    productoAEditar =
                                        null

                                    errorEdicion =
                                        null
                                }
                            }
                        }
                    },

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
                        "Guardar"
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {

                        productoAEditar =
                            null

                        errorEdicion =
                            null
                    }
                ) {

                    Text(
                        "Cancelar"
                    )
                }
            }
        )
    }
}


@Composable
private fun TarjetaProductoCatalogo(
    producto: ProductoEntity,
    esMia: Boolean,
    onEditar: () -> Unit,
    onCambiarEstado: () -> Unit,
    onEliminar: () -> Unit
) {

    val bitmap =
        remember(
            producto.imagenRuta
        ) {

            producto
                .imagenRuta
                ?.let { ruta ->

                    BitmapFactory
                        .decodeFile(
                            ruta
                        )
                        ?.asImageBitmap()
                }
        }


    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                16.dp
            ),

        colors =
            CardDefaults
                .cardColors(
                    containerColor =
                        Color.White
                ),

        elevation =
            CardDefaults
                .cardElevation(
                    defaultElevation =
                        4.dp
                )
    ) {

        Column {

            // =============================================
            // IMAGEN
            // =============================================

            bitmap?.let {
                    imagen ->

                Image(
                    bitmap =
                        imagen,

                    contentDescription =
                        producto.nombre,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(
                                190.dp
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStart =
                                        16.dp,
                                    topEnd =
                                        16.dp
                                )
                            ),

                    contentScale =
                        ContentScale.Crop
                )
            }


            Column(
                modifier =
                    Modifier.padding(
                        16.dp
                    )
            ) {

                // =============================================
                // NOMBRE Y TIPO
                // =============================================

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text =
                            producto.nombre,

                        modifier =
                            Modifier.weight(
                                1f
                            ),

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color.Black
                    )


                    Text(
                        text =
                            if (
                                producto.tipo ==
                                "PRODUCTO"
                            ) {
                                "Producto"
                            } else {
                                "Servicio"
                            },

                        color =
                            Color(
                                0xFF00A6A6
                            ),

                        fontWeight =
                            FontWeight.Bold
                    )
                }


                Spacer(
                    modifier =
                        Modifier.height(
                            5.dp
                        )
                )


                Text(
                    text =
                        producto
                            .nombreEmprendimiento,

                    color =
                        Color(
                            0xFF00A6A6
                        ),

                    fontWeight =
                        FontWeight.SemiBold
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )


                Text(
                    text =
                        producto.descripcion,

                    color =
                        Color.DarkGray
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                Text(
                    text =
                        "$${formatearPrecio(producto.precio)}",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color.Black
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )


                // =============================================
                // ESTADO
                // =============================================

                Text(
                    text =
                        if (
                            producto.estado ==
                            "VENDIDO"
                        ) {

                            "VENDIDO"

                        } else {

                            "DISPONIBLE"
                        },

                    color =
                        if (
                            producto.estado ==
                            "VENDIDO"
                        ) {

                            Color(
                                0xFFD32F2F
                            )

                        } else {

                            Color(
                                0xFF2E7D32
                            )
                        },

                    fontWeight =
                        FontWeight.Bold
                )


                // =============================================
                // SOLO EL DUEÑO PUEDE MODIFICAR
                // =============================================

                if (
                    esMia
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )


                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.spacedBy(
                                8.dp
                            )
                    ) {

                        Button(
                            onClick =
                                onEditar,

                            modifier =
                                Modifier.weight(
                                    1f
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
                                "Editar"
                            )
                        }


                        OutlinedButton(
                            onClick =
                                onCambiarEstado,

                            modifier =
                                Modifier.weight(
                                    1f
                                )
                        ) {

                            Text(
                                text =
                                    if (
                                        producto.estado ==
                                        "VENDIDO"
                                    ) {

                                        "Disponible"

                                    } else {

                                        "Vendido"
                                    }
                            )
                        }
                    }


                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )


                    OutlinedButton(
                        onClick =
                            onEliminar,

                        modifier =
                            Modifier.fillMaxWidth(),

                        colors =
                            ButtonDefaults
                                .outlinedButtonColors(
                                    contentColor =
                                        Color(
                                            0xFFD32F2F
                                        )
                                )
                    ) {

                        Text(
                            "Eliminar publicación"
                        )
                    }
                }
            }
        }
    }
}


private fun formatearPrecio(
    precio: Int
): String {

    return "%,d"
        .format(
            precio
        )
        .replace(
            ',',
            '.'
        )
}