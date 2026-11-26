package com.example.fundaciontextilcircular.view

import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.local.entities.ChatEntity
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity
import com.example.fundaciontextilcircular.data.remote.RetrofitClient
import com.example.fundaciontextilcircular.data.remote.dto.AdminSolicitudDTO
import com.example.fundaciontextilcircular.data.repository.AdminRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor
import com.example.fundaciontextilcircular.ui.theme.AmarilloTextil
import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.RojoErrorTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil
import com.example.fundaciontextilcircular.viewmodel.AdminViewModel
import com.example.fundaciontextilcircular.viewmodel.AdminViewModelFactory
import kotlinx.coroutines.launch


private val FondoAdmin =
    Color(0xFF1E1E1E)


@Composable
fun PantallaAdmin(
    modifier: Modifier = Modifier,
    onCerrarSesion: () -> Unit = {},
    onVerChat: (String) -> Unit = {},
    onIrAEliminarUsuarios: () -> Unit = {}
) {

    val context =
        LocalContext.current


    // =========================================
    // CONECTIVIDAD
    // =========================================

    val networkMonitor =
        remember {

            NetworkMonitor(
                context.applicationContext
            )
        }


    val isOnline by
    networkMonitor
        .isOnline
        .collectAsState(
            initial = true
        )


    // =========================================
    // ADMIN REMOTO
    // =========================================

    val adminRepository =
        remember {

            AdminRepository(
                RetrofitClient.adminApi
            )
        }


    val adminViewModel:
            AdminViewModel =
        viewModel(

            factory =
                AdminViewModelFactory(
                    adminRepository
                )
        )


    val adminUiState =
        adminViewModel.uiState


    // =========================================
    // ROOM
    // Se mantiene para chats y cuentas antiguas
    // =========================================

    val scope =
        rememberCoroutineScope()


    val database =
        remember(context) {

            TextilDatabase
                .getDatabase(context)
        }


    val emprendedoraDao =
        remember(database) {

            database
                .emprendedoraDao()
        }


    val chatDao =
        remember(database) {

            database
                .mensajeDao()
        }


    val activas by
    emprendedoraDao
        .obtenerAprobadas()
        .collectAsState(
            initial = emptyList()
        )


    val rechazadas by
    emprendedoraDao
        .obtenerRechazadas()
        .collectAsState(
            initial = emptyList()
        )


    val chats by
    chatDao
        .obtenerTodosLosChats()
        .collectAsState(
            initial = emptyList()
        )


    // =========================================
    // SOLICITUDES DEL BACKEND
    // =========================================

    val pendientes =
        adminUiState.solicitudes


    LaunchedEffect(
        isOnline
    ) {

        if (isOnline) {

            adminViewModel
                .cargarSolicitudes(
                    isOnline = true
                )
        }
    }


    // =========================================
    // ESTADOS
    // =========================================

    var solicitudARechazar by remember {

        mutableStateOf<AdminSolicitudDTO?>(
            null
        )
    }


    var motivoRechazo by remember {

        mutableStateOf("")
    }


    // =========================================
    // PANTALLA
    // =========================================

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(
                    NegroTextil
                )
                .padding(
                    20.dp
                )
    ) {

        Spacer(
            modifier =
                Modifier.height(
                    30.dp
                )
        )


        Text(
            text =
                "ADMINISTRACIÓN",

            color =
                TurquesaTextil,

            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )


        Spacer(
            modifier =
                Modifier.height(
                    8.dp
                )
        )


        Text(
            text =
                "Gestión de cuentas",

            color =
                BlancoTextil,

            style =
                MaterialTheme
                    .typography
                    .titleMedium
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onIrAEliminarUsuarios,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = RojoErrorTextil,
                contentColor = BlancoTextil
            )
        ) {
            Text(text = "BUSCAR Y ELIMINAR USUARIOS")
        }


        // =====================================
        // MENSAJE
        // =====================================

        adminUiState.mensaje
            ?.let {

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                Text(
                    text = it,
                    color =
                        TurquesaTextil
                )
            }


        // =====================================
        // ERROR
        // =====================================

        adminUiState.error
            ?.let {

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                Text(
                    text = it,
                    color =
                        RojoErrorTextil
                )
            }


        Spacer(
            modifier =
                Modifier.height(
                    20.dp
                )
        )


        LazyColumn(

            modifier =
                Modifier.weight(
                    1f
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    16.dp
                )
        ) {


            // =================================
            // CHATS
            // =================================

            item {

                Text(
                    text =
                        "MENSAJES DE EMPRENDEDORAS (${chats.size})",

                    color =
                        AmarilloTextil,

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )
            }


            if (
                chats.isEmpty()
            ) {

                item {

                    Text(
                        text =
                            "No hay mensajes ni chats activos.",

                        color =
                            BlancoTextil
                    )
                }

            } else {

                items(
                    items =
                        chats,

                    key = {
                        "chat_${it.chatId}"
                    }
                ) {
                        chat ->


                    val emprendedoraAsociada =
                        activas.find {
                                cuenta ->

                            val correoLimpio =
                                cuenta
                                    .correo
                                    .replace(
                                        ".",
                                        "_"
                                    )


                            chat.chatId
                                .contains(
                                    correoLimpio
                                )
                        }


                    TarjetaChatAdmin(

                        chat =
                            chat,

                        emprendedora =
                            emprendedoraAsociada,

                        onResponder = {

                            onVerChat(
                                chat.chatId
                            )
                        },

                        onCerrarChat = {

                            scope.launch {

                                chatDao
                                    .cerrarChat(
                                        chat.chatId
                                    )
                            }
                        }
                    )
                }
            }


            item {

                Spacer(
                    modifier =
                        Modifier.height(
                            20.dp
                        )
                )
            }


            // =================================
            // SOLICITUDES PENDIENTES REMOTAS
            // =================================

            item {

                Text(
                    text =
                        "SOLICITUDES PENDIENTES (${pendientes.size})",

                    color =
                        AmarilloTextil,

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )
            }


            if (
                adminUiState.cargando &&
                pendientes.isEmpty()
            ) {

                item {

                    Text(
                        text =
                            "Cargando solicitudes...",

                        color =
                            BlancoTextil
                    )
                }

            } else if (
                pendientes.isEmpty()
            ) {

                item {

                    Text(
                        text =
                            "No hay cuentas pendientes.",

                        color =
                            BlancoTextil
                    )
                }

            } else {

                items(

                    items =
                        pendientes,

                    key = {

                        "pendiente_${it.id}"
                    }

                ) {
                        solicitud ->


                    TarjetaSolicitudRemota(

                        solicitud =
                            solicitud,

                        onAprobar = {

                            adminViewModel
                                .aprobarSolicitud(

                                    id =
                                        solicitud.id,

                                    isOnline =
                                        isOnline
                                )
                        },

                        onRechazar = {

                            solicitudARechazar =
                                solicitud

                            motivoRechazo =
                                ""

                            adminViewModel
                                .limpiarError()
                        }
                    )
                }
            }


            item {

                Spacer(
                    modifier =
                        Modifier.height(
                            20.dp
                        )
                )
            }


            // =================================
            // ACTIVAS LOCALES
            // =================================

            item {

                Text(
                    text =
                        "CUENTAS ACTIVAS LOCALES (${activas.size})",

                    color =
                        TurquesaTextil,

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )
            }


            if (
                activas.isEmpty()
            ) {

                item {

                    Text(
                        text =
                            "No hay cuentas activas locales.",

                        color =
                            BlancoTextil
                    )
                }

            } else {

                items(
                    items =
                        activas,

                    key = {
                        "activa_${it.rut}"
                    }
                ) {
                        cuenta ->

                    TarjetaCuentaLocal(
                        cuenta =
                            cuenta
                    )
                }
            }


            item {

                Spacer(
                    modifier =
                        Modifier.height(
                            20.dp
                        )
                )
            }


            // =================================
            // RECHAZADAS LOCALES
            // =================================

            item {

                Text(
                    text =
                        "CUENTAS RECHAZADAS LOCALES (${rechazadas.size})",

                    color =
                        RojoErrorTextil,

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )
            }


            if (
                rechazadas.isEmpty()
            ) {

                item {

                    Text(
                        text =
                            "No hay cuentas rechazadas locales.",

                        color =
                            BlancoTextil
                    )
                }

            } else {

                items(
                    items =
                        rechazadas,

                    key = {
                        "rechazada_${it.rut}"
                    }
                ) {
                        cuenta ->

                    TarjetaCuentaLocal(
                        cuenta =
                            cuenta
                    )
                }
            }
        }


        Spacer(
            modifier =
                Modifier.height(
                    16.dp
                )
        )


        OutlinedButton(

            onClick =
                onCerrarSesion,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text =
                    "CERRAR SESIÓN",

                color =
                    BlancoTextil
            )
        }
    }


    // =========================================
    // DIÁLOGO RECHAZAR
    // =========================================

    solicitudARechazar
        ?.let {
                solicitud ->


            AlertDialog(

                onDismissRequest = {

                    solicitudARechazar =
                        null

                    motivoRechazo =
                        ""

                    adminViewModel
                        .limpiarError()
                },

                containerColor =
                    FondoAdmin,

                title = {

                    Text(
                        text =
                            "Rechazar solicitud",

                        color =
                            TurquesaTextil
                    )
                },

                text = {

                    Column {

                        Text(
                            text =
                                "Indica el motivo del rechazo de ${solicitud.nombre}.",

                            color =
                                BlancoTextil
                        )


                        Spacer(
                            modifier =
                                Modifier.height(
                                    12.dp
                                )
                        )


                        OutlinedTextField(

                            value =
                                motivoRechazo,

                            onValueChange = {

                                motivoRechazo =
                                    it

                                adminViewModel
                                    .limpiarError()
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            label = {

                                Text(
                                    "Motivo"
                                )
                            },

                            maxLines =
                                5
                        )
                    }
                },

                confirmButton = {

                    Button(

                        onClick = {

                            val motivo =
                                motivoRechazo
                                    .trim()


                            if (
                                motivo.isBlank()
                            ) {

                                return@Button
                            }


                            adminViewModel
                                .rechazarSolicitud(

                                    id =
                                        solicitud.id,

                                    motivo =
                                        motivo,

                                    isOnline =
                                        isOnline
                                )


                            solicitudARechazar =
                                null


                            motivoRechazo =
                                ""
                        },

                        colors =
                            ButtonDefaults
                                .buttonColors(

                                    containerColor =
                                        RojoErrorTextil,

                                    contentColor =
                                        BlancoTextil
                                )
                    ) {

                        Text(
                            text =
                                "RECHAZAR"
                        )
                    }
                },

                dismissButton = {

                    TextButton(

                        onClick = {

                            solicitudARechazar =
                                null

                            motivoRechazo =
                                ""
                        }
                    ) {

                        Text(
                            text =
                                "CANCELAR",

                            color =
                                TurquesaTextil
                        )
                    }
                }
            )
        }
}


// =========================================
// TARJETA SOLICITUD BACKEND
// =========================================

@Composable
fun TarjetaSolicitudRemota(

    solicitud:
    AdminSolicitudDTO,

    onAprobar: () -> Unit,

    onRechazar: () -> Unit
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                12.dp
            ),

        colors =
            CardDefaults
                .cardColors(

                    containerColor =
                        FondoAdmin
                )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {

            Text(
                text =
                    solicitud.nombre,

                color =
                    BlancoTextil,

                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )


            Spacer(
                modifier =
                    Modifier.height(
                        4.dp
                    )
            )


            Text(
                text =
                    solicitud.nombreEmprendimiento,

                color =
                    TurquesaTextil
            )


            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            Text(
                text =
                    "RUT: ${solicitud.rut}",

                color =
                    BlancoTextil
            )


            Text(
                text =
                    "Correo: ${solicitud.correo}",

                color =
                    BlancoTextil
            )


            Text(
                text =
                    "Edad: ${solicitud.edad}",

                color =
                    BlancoTextil
            )


            Text(
                text =
                    "Género: ${solicitud.genero}",

                color =
                    BlancoTextil
            )


            Text(
                text =
                    "Región: ${solicitud.region}",

                color =
                    BlancoTextil
            )


            Text(
                text =
                    "Comuna: ${solicitud.comuna}",

                color =
                    BlancoTextil
            )


            Text(
                text =
                    "Rubro: ${solicitud.rubro}",

                color =
                    BlancoTextil
            )


            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            Text(
                text =
                    "Estado: ${solicitud.estado}",

                color =
                    AmarilloTextil,

                style =
                    MaterialTheme
                        .typography
                        .labelLarge
            )


            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
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

                    onClick =
                        onAprobar,

                    modifier =
                        Modifier.weight(
                            1f
                        ),

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
                            "APROBAR"
                    )
                }


                Button(

                    onClick =
                        onRechazar,

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    colors =
                        ButtonDefaults
                            .buttonColors(

                                containerColor =
                                    RojoErrorTextil,

                                contentColor =
                                    BlancoTextil
                            )
                ) {

                    Text(
                        text =
                            "RECHAZAR"
                    )
                }
            }
        }
    }
}


// =========================================
// CUENTA LOCAL
// =========================================

@Composable
fun TarjetaCuentaLocal(
    cuenta:
    EmprendedoraEntity
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                12.dp
            ),

        colors =
            CardDefaults
                .cardColors(

                    containerColor =
                        FondoAdmin
                )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {

            Text(
                text =
                    cuenta.nombre,

                color =
                    BlancoTextil,

                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )


            Spacer(
                modifier =
                    Modifier.height(
                        4.dp
                    )
            )


            Text(
                text =
                    cuenta.nombreEmprendimiento,

                color =
                    TurquesaTextil
            )


            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            Text(
                text =
                    "RUT: ${cuenta.rut}",

                color =
                    BlancoTextil
            )


            Text(
                text =
                    "Correo: ${cuenta.correo}",

                color =
                    BlancoTextil
            )


            Text(
                text =
                    "Estado: ${cuenta.estado}",

                color =
                    if (
                        cuenta.estado ==
                        "RECHAZADA"
                    ) {

                        RojoErrorTextil

                    } else {

                        TurquesaTextil
                    }
            )


            cuenta
                .motivoRechazo
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {

                    Text(
                        text =
                            "Motivo: $it",

                        color =
                            BlancoTextil
                    )
                }
        }
    }
}


// =========================================
// TARJETA CHAT
// =========================================

@Composable
fun TarjetaChatAdmin(

    chat:
    ChatEntity,

    emprendedora:
    EmprendedoraEntity?,

    onResponder: () -> Unit,

    onCerrarChat: () -> Unit
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                12.dp
            ),

        colors =
            CardDefaults
                .cardColors(

                    containerColor =
                        FondoAdmin
                )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {

            val tituloMostrar =

                if (
                    emprendedora != null
                ) {

                    "${emprendedora.nombre} / ${emprendedora.nombreEmprendimiento}"

                } else {

                    "Emprendedora desconocida"
                }


            Text(
                text =
                    tituloMostrar,

                color =
                    BlancoTextil,

                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )


            Spacer(
                modifier =
                    Modifier.height(
                        4.dp
                    )
            )


            val estadoText =

                if (
                    chat.cerrado
                ) {

                    "CERRADO"

                } else {

                    "ABIERTO"
                }


            val estadoColor =

                if (
                    chat.cerrado
                ) {

                    RojoErrorTextil

                } else {

                    TurquesaTextil
                }


            Text(
                text =
                    "Estado: $estadoText",

                color =
                    estadoColor,

                style =
                    MaterialTheme
                        .typography
                        .labelLarge
            )


            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
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

                    onClick =
                        onResponder,

                    modifier =
                        Modifier.weight(
                            1f
                        ),

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
                            "VER / RESPONDER"
                    )
                }


                if (
                    !chat.cerrado
                ) {

                    Button(

                        onClick =
                            onCerrarChat,

                        modifier =
                            Modifier.weight(
                                1f
                            ),

                        colors =
                            ButtonDefaults
                                .buttonColors(

                                    containerColor =
                                        RojoErrorTextil,

                                    contentColor =
                                        BlancoTextil
                                )
                    ) {

                        Text(
                            text =
                                "CERRAR CHAT"
                        )
                    }
                }
            }
        }
    }
}