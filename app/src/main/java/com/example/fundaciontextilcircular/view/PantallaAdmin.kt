package com.example.fundaciontextilcircular.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity
import com.example.fundaciontextilcircular.ui.theme.AmarilloTextil
import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.RojoErrorTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import com.example.fundaciontextilcircular.data.local.entities.ChatEntity


// Color de las tarjetas y ventanas del administrador
private val FondoAdmin =
    Color(0xFF1E1E1E)


@Composable()
fun PantallaAdmin(
    modifier: Modifier = Modifier,
    onCerrarSesion: () -> Unit = {},
    onVerChat: (String) -> Unit = {}
) {

    val context =
        LocalContext.current

    val scope =
        rememberCoroutineScope()

    val database = remember(context) { TextilDatabase.getDatabase(context) }

    val dao =
        remember(context) {

            TextilDatabase
                .getDatabase(context)
                .emprendedoraDao()
        }

    val chatDao = remember(database) { database.mensajeDao() }

    val auth =
        remember {

            FirebaseAuth
                .getInstance()
        }




    // =========================
    // CUENTAS PENDIENTES
    // =========================

    val pendientes by dao
        .obtenerPendientes()
        .collectAsState(
            initial = emptyList()
        )


    // =========================
    // CUENTAS ACTIVAS
    // =========================

    val activas by dao
        .obtenerAprobadas()
        .collectAsState(
            initial = emptyList()
        )


    // =========================
    // CUENTAS RECHAZADAS
    // =========================

    val rechazadas by dao
        .obtenerRechazadas()
        .collectAsState(
            initial = emptyList()
        )

    val chats by chatDao.obtenerTodosLosChats().collectAsState(initial = emptyList())


    // =========================
    // ESTADOS
    // =========================

    var solicitudARechazar by remember {
        mutableStateOf<EmprendedoraEntity?>(
            null
        )
    }


    var motivoRechazo by remember {
        mutableStateOf("")
    }


    var cuentaAEliminar by remember {
        mutableStateOf<EmprendedoraEntity?>(
            null
        )
    }


    var cuentaAEditar by remember {
        mutableStateOf<EmprendedoraEntity?>(
            null
        )
    }


    var cuentaARecuperar by remember {
        mutableStateOf<EmprendedoraEntity?>(
            null
        )
    }


    var mensajeAdmin by remember {
        mutableStateOf<String?>(
            null
        )
    }


    var errorAdmin by remember {
        mutableStateOf<String?>(
            null
        )
    }


    // =========================
    // PANTALLA PRINCIPAL ADMIN
    // =========================

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


        // =========================
        // MENSAJE CORRECTO
        // =========================

        mensajeAdmin
            ?.let {

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                Text(
                    text =
                        it,

                    color =
                        TurquesaTextil
                )
            }


        // =========================
        // MENSAJE ERROR
        // =========================

        errorAdmin
            ?.let {

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                Text(
                    text =
                        it,

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


        // =========================
        // LISTADO
        // =========================

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

            // =========================
            // MENSAJES Y CHATS
            // =========================
            item {
                Text(
                    text = "MENSAJES DE EMPRENDEDORAS (${chats.size})",
                    color = AmarilloTextil,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (chats.isEmpty()) {
                item {
                    Text(
                        text = "No hay mensajes ni chats activos.",
                        color = BlancoTextil
                    )
                }
            } else {
                items(
                    items = chats,
                    key = { "chat_${it.chatId}" }
                ) { chat ->

                    val emprendedoraAsociada = activas.find { cuenta ->
                        val correoLimpio = cuenta.correo.replace(".", "_")
                        chat.chatId.contains(correoLimpio)
                    }

                    TarjetaChatAdmin(
                        chat = chat,
                        emprendedora = emprendedoraAsociada,
                        onResponder = { onVerChat(chat.chatId) },
                        onCerrarChat = {
                            scope.launch {
                                chatDao.cerrarChat(chat.chatId)
                            }
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }


            // =========================
            // SOLICITUDES PENDIENTES
            // =========================

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

                        "pendiente_${it.rut}"
                    }

                ) {
                        cuenta ->


                    TarjetaSolicitud(

                        solicitud =
                            cuenta,


                        onAprobar = {

                            scope.launch {

                                dao.aprobarCuenta(
                                    cuenta.rut
                                )
                            }
                        },


                        onRechazar = {

                            solicitudARechazar =
                                cuenta

                            motivoRechazo =
                                ""
                        },


                        onEditar = {

                            cuentaAEditar =
                                cuenta
                        },


                        onRecuperarContrasena = {

                            cuentaARecuperar =
                                cuenta
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


            // =========================
            // CUENTAS ACTIVAS
            // =========================

            item {

                Text(
                    text =
                        "CUENTAS ACTIVAS (${activas.size})",

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
                            "No hay cuentas activas.",

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


                    TarjetaCuentaActiva(

                        cuenta =
                            cuenta,


                        onEditar = {

                            cuentaAEditar =
                                cuenta
                        },


                        onRecuperarContrasena = {

                            cuentaARecuperar =
                                cuenta
                        },


                        onEliminar = {

                            cuentaAEliminar =
                                cuenta
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


            // =========================
            // CUENTAS RECHAZADAS
            // =========================

            item {

                Text(
                    text =
                        "CUENTAS RECHAZADAS (${rechazadas.size})",

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
                            "No hay cuentas rechazadas.",

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


                    TarjetaCuentaRechazada(

                        cuenta =
                            cuenta,


                        onEditar = {

                            cuentaAEditar =
                                cuenta
                        },


                        onRecuperarContrasena = {

                            cuentaARecuperar =
                                cuenta
                        },


                        onEliminar = {

                            cuentaAEliminar =
                                cuenta
                        }
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


        // =========================
        // CERRAR SESIÓN
        // =========================

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


    // =================================
    // DIÁLOGO RECHAZAR CUENTA
    // =================================

    solicitudARechazar
        ?.let {
                cuenta ->


            AlertDialog(

                onDismissRequest = {

                    solicitudARechazar =
                        null

                    motivoRechazo =
                        ""
                },


                containerColor =
                    FondoAdmin,


                titleContentColor =
                    TurquesaTextil,


                textContentColor =
                    BlancoTextil,


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
                                "Indica el motivo del rechazo.",

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
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            label = {

                                Text(
                                    "Motivo"
                                )
                            },

                            colors =
                                coloresCampoAdmin()
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
                                motivo
                                    .isNotBlank()
                            ) {

                                scope.launch {

                                    dao.rechazarCuenta(

                                        rut =
                                            cuenta.rut,

                                        motivo =
                                            motivo
                                    )


                                    solicitudARechazar =
                                        null


                                    motivoRechazo =
                                        ""
                                }
                            }
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


    // =================================
    // DIÁLOGO EDITAR CUENTA
    // =================================

    cuentaAEditar
        ?.let {
                cuenta ->


            DialogoEditarCuentaAdmin(

                cuenta =
                    cuenta,


                onCancelar = {

                    cuentaAEditar =
                        null
                },


                onGuardar = {
                        nombre,
                        nombreEmprendimiento,
                        rubro,
                        region,
                        comuna,
                        edad,
                        genero ->


                    scope.launch {

                        try {

                            dao.actualizarDatosPerfil(

                                rut =
                                    cuenta.rut,

                                nombre =
                                    nombre,

                                nombreEmprendimiento =
                                    nombreEmprendimiento,

                                rubro =
                                    rubro,

                                region =
                                    region,

                                comuna =
                                    comuna,

                                edad =
                                    edad,

                                genero =
                                    genero
                            )


                            cuentaAEditar =
                                null


                            errorAdmin =
                                null


                            mensajeAdmin =
                                "Datos actualizados correctamente"


                        } catch (
                            e: Exception
                        ) {

                            mensajeAdmin =
                                null


                            errorAdmin =
                                "No se pudieron actualizar los datos."
                        }
                    }
                }
            )
        }


    // =================================
    // RESTABLECER CONTRASEÑA
    // =================================

    cuentaARecuperar
        ?.let {
                cuenta ->


            AlertDialog(

                onDismissRequest = {

                    cuentaARecuperar =
                        null
                },


                containerColor =
                    FondoAdmin,


                titleContentColor =
                    TurquesaTextil,


                textContentColor =
                    BlancoTextil,


                title = {

                    Text(
                        text =
                            "Restablecer contraseña",

                        color =
                            TurquesaTextil
                    )
                },


                text = {

                    Text(
                        text =
                            "Se enviará un enlace de recuperación a:\n\n${cuenta.correo}",

                        color =
                            BlancoTextil
                    )
                },


                confirmButton = {

                    Button(

                        onClick = {

                            cuentaARecuperar =
                                null


                            auth
                                .sendPasswordResetEmail(
                                    cuenta.correo
                                )
                                .addOnCompleteListener {
                                        task ->


                                    if (
                                        task.isSuccessful
                                    ) {

                                        errorAdmin =
                                            null


                                        mensajeAdmin =
                                            "Se envió el enlace de recuperación a ${cuenta.correo}"

                                    } else {

                                        mensajeAdmin =
                                            null


                                        errorAdmin =
                                            "No se pudo enviar el enlace de recuperación."
                                    }
                                }
                        },


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
                                "ENVIAR"
                        )
                    }
                },


                dismissButton = {

                    TextButton(

                        onClick = {

                            cuentaARecuperar =
                                null
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


    // =================================
    // ELIMINAR CUENTA
    // =================================

    cuentaAEliminar
        ?.let {
                cuenta ->


            AlertDialog(

                onDismissRequest = {

                    cuentaAEliminar =
                        null
                },


                containerColor =
                    FondoAdmin,


                titleContentColor =
                    RojoErrorTextil,


                textContentColor =
                    BlancoTextil,


                title = {

                    Text(
                        text =
                            "Eliminar cuenta",

                        color =
                            RojoErrorTextil
                    )
                },


                text = {

                    Text(
                        text =
                            "¿Estás seguro de que deseas eliminar la cuenta de ${cuenta.nombre}?",

                        color =
                            BlancoTextil
                    )
                },


                confirmButton = {

                    Button(

                        onClick = {

                            scope.launch {

                                dao.eliminarCuenta(
                                    cuenta.rut
                                )


                                cuentaAEliminar =
                                    null
                            }
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
                                "ELIMINAR"
                        )
                    }
                },


                dismissButton = {

                    TextButton(

                        onClick = {

                            cuentaAEliminar =
                                null
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


// =====================================
// TARJETA SOLICITUD PENDIENTE
// =====================================

@Composable
fun TarjetaSolicitud(

    solicitud: EmprendedoraEntity,

    onAprobar: () -> Unit,

    onRechazar: () -> Unit,

    onEditar: () -> Unit,

    onRecuperarContrasena: () -> Unit
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

            DatosCuenta(
                cuenta =
                    solicitud
            )


            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            Text(
                text =
                    "Estado: PENDIENTE",

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


            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            OutlinedButton(

                onClick =
                    onEditar,

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        "EDITAR DATOS",

                    color =
                        TurquesaTextil
                )
            }


            OutlinedButton(

                onClick =
                    onRecuperarContrasena,

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        "RESTABLECER CONTRASEÑA",

                    color =
                        BlancoTextil
                )
            }
        }
    }
}


// =====================================
// TARJETA CUENTA ACTIVA
// =====================================

@Composable
fun TarjetaCuentaActiva(

    cuenta: EmprendedoraEntity,

    onEditar: () -> Unit,

    onRecuperarContrasena: () -> Unit,

    onEliminar: () -> Unit
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

            DatosCuenta(
                cuenta =
                    cuenta
            )


            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            Text(
                text =
                    "Estado: ACTIVA",

                color =
                    TurquesaTextil,

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


            Button(

                onClick =
                    onEditar,

                modifier =
                    Modifier.fillMaxWidth(),

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
                        "EDITAR DATOS"
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            OutlinedButton(

                onClick =
                    onRecuperarContrasena,

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        "RESTABLECER CONTRASEÑA",

                    color =
                        BlancoTextil
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Button(

                onClick =
                    onEliminar,

                modifier =
                    Modifier.fillMaxWidth(),

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
                        "ELIMINAR CUENTA"
                )
            }
        }
    }
}


// =====================================
// TARJETA CUENTA RECHAZADA
// =====================================

@Composable
fun TarjetaCuentaRechazada(

    cuenta: EmprendedoraEntity,

    onEditar: () -> Unit,

    onRecuperarContrasena: () -> Unit,

    onEliminar: () -> Unit
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

            DatosCuenta(
                cuenta =
                    cuenta
            )


            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            Text(
                text =
                    "Estado: RECHAZADA",

                color =
                    RojoErrorTextil,

                style =
                    MaterialTheme
                        .typography
                        .labelLarge
            )


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            val motivo =

                cuenta
                    .motivoRechazo
                    ?.takeIf {

                        it.isNotBlank()
                    }

                    ?: "No se indicó un motivo."


            Text(
                text =
                    "Motivo: $motivo",

                color =
                    BlancoTextil
            )


            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            Button(

                onClick =
                    onEditar,

                modifier =
                    Modifier.fillMaxWidth(),

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
                        "EDITAR DATOS"
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            OutlinedButton(

                onClick =
                    onRecuperarContrasena,

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        "RESTABLECER CONTRASEÑA",

                    color =
                        BlancoTextil
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Button(

                onClick =
                    onEliminar,

                modifier =
                    Modifier.fillMaxWidth(),

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
                        "ELIMINAR CUENTA"
                )
            }
        }
    }
}


// =====================================
// DIÁLOGO EDITAR USUARIO
// =====================================

@Composable
fun DialogoEditarCuentaAdmin(

    cuenta: EmprendedoraEntity,

    onCancelar: () -> Unit,

    onGuardar: (
        nombre: String,
        nombreEmprendimiento: String,
        rubro: String,
        region: String,
        comuna: String,
        edad: Int,
        genero: String
    ) -> Unit
) {

    var nombre by remember {

        mutableStateOf(
            cuenta.nombre
        )
    }


    var nombreEmprendimiento by remember {

        mutableStateOf(
            cuenta.nombreEmprendimiento
        )
    }


    var rubro by remember {

        mutableStateOf(
            cuenta.rubro
        )
    }


    var region by remember {

        mutableStateOf(
            cuenta.region
        )
    }


    var comuna by remember {

        mutableStateOf(
            cuenta.comuna
        )
    }


    var edad by remember {

        mutableStateOf(
            cuenta.edad
                .toString()
        )
    }


    var genero by remember {

        mutableStateOf(
            cuenta.genero
        )
    }


    // =========================
    // GÉNERO
    // =========================

    var menuGenero by remember {

        mutableStateOf(
            false
        )
    }


    // =========================
    // REGIÓN
    // =========================

    var menuRegionAbierto by remember {

        mutableStateOf(
            false
        )
    }


    var busquedaRegion by remember {

        mutableStateOf("")
    }


    // =========================
    // COMUNA
    // =========================

    var menuComunaAbierto by remember {

        mutableStateOf(
            false
        )
    }


    var busquedaComuna by remember {

        mutableStateOf("")
    }


    var error by remember {

        mutableStateOf<String?>(
            null
        )
    }


    val generos =

        listOf(

            "Femenino",
            "Masculino",
            "No binario",
            "Prefiero no responder"
        )


    // =========================
    // COMUNAS SEGÚN REGIÓN
    // =========================

    val comunasDisponibles =

        COMUNAS_POR_REGION_ADMIN[
            region
        ] ?: emptyList()


    AlertDialog(

        onDismissRequest =
            onCancelar,


        containerColor =
            FondoAdmin,


        titleContentColor =
            TurquesaTextil,


        textContentColor =
            BlancoTextil,


        title = {

            Text(
                text =
                    "EDITAR DATOS",

                color =
                    TurquesaTextil,

                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )
        },


        text = {

            Column(

                modifier =
                    Modifier
                        .heightIn(
                            max = 520.dp
                        )
                        .verticalScroll(
                            rememberScrollState()
                        )
            ) {


                // =========================
                // RUT
                // =========================

                Text(
                    text =
                        "RUT",

                    color =
                        TurquesaTextil,

                    style =
                        MaterialTheme
                            .typography
                            .labelLarge
                )


                Text(
                    text =
                        cuenta.rut,

                    color =
                        BlancoTextil
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                // =========================
                // CORREO
                // =========================

                Text(
                    text =
                        "Correo",

                    color =
                        TurquesaTextil,

                    style =
                        MaterialTheme
                            .typography
                            .labelLarge
                )


                Text(
                    text =
                        cuenta.correo,

                    color =
                        BlancoTextil
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            20.dp
                        )
                )


                // =========================
                // NOMBRE
                // =========================

                OutlinedTextField(

                    value =
                        nombre,

                    onValueChange = {

                        nombre =
                            it

                        error =
                            null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            "Nombre"
                        )
                    },

                    singleLine =
                        true,

                    colors =
                        coloresCampoAdmin()
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                // =========================
                // EDAD
                // =========================

                OutlinedTextField(

                    value =
                        edad,

                    onValueChange = {

                        edad =
                            it
                                .filter {
                                        caracter ->

                                    caracter
                                        .isDigit()
                                }
                                .take(
                                    3
                                )


                        error =
                            null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            "Edad"
                        )
                    },

                    keyboardOptions =
                        KeyboardOptions(

                            keyboardType =
                                KeyboardType.Number
                        ),

                    singleLine =
                        true,

                    colors =
                        coloresCampoAdmin()
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                // =========================
                // GÉNERO
                // =========================

                Text(
                    text =
                        "Género",

                    color =
                        BlancoTextil,

                    style =
                        MaterialTheme
                            .typography
                            .bodyLarge
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )


                OutlinedButton(

                    onClick = {

                        menuGenero =
                            true
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text(
                        text =
                            genero,

                        color =
                            BlancoTextil
                    )
                }


                DropdownMenu(

                    expanded =
                        menuGenero,

                    onDismissRequest = {

                        menuGenero =
                            false
                    }
                ) {

                    generos
                        .forEach {
                                opcion ->


                            DropdownMenuItem(

                                text = {

                                    Text(
                                        opcion
                                    )
                                },

                                onClick = {

                                    genero =
                                        opcion

                                    menuGenero =
                                        false

                                    error =
                                        null
                                }
                            )
                        }
                }


                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                // =========================
                // EMPRENDIMIENTO
                // =========================

                OutlinedTextField(

                    value =
                        nombreEmprendimiento,

                    onValueChange = {

                        nombreEmprendimiento =
                            it

                        error =
                            null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            "Nombre del emprendimiento"
                        )
                    },

                    singleLine =
                        true,

                    colors =
                        coloresCampoAdmin()
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                // =========================
                // RUBRO
                // =========================

                OutlinedTextField(

                    value =
                        rubro,

                    onValueChange = {

                        rubro =
                            it

                        error =
                            null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            "Rubro"
                        )
                    },

                    singleLine =
                        true,

                    colors =
                        coloresCampoAdmin()
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            20.dp
                        )
                )


                // =========================
                // REGIÓN
                // =========================

                SelectorRegion(

                    regionSeleccionada =
                        region,

                    busqueda =
                        busquedaRegion,

                    menuAbierto =
                        menuRegionAbierto,

                    regiones =
                        REGIONES_CHILE_ADMIN,

                    error =
                        null,


                    onAbrirMenu = {

                        menuRegionAbierto =
                            true

                        busquedaRegion =
                            ""
                    },


                    onCerrarMenu = {

                        menuRegionAbierto =
                            false

                        busquedaRegion =
                            ""
                    },


                    onBusquedaCambio = {

                        busquedaRegion =
                            it
                    },


                    onRegionSeleccionada = {
                            nuevaRegion ->


                        if (
                            region !=
                            nuevaRegion
                        ) {

                            comuna =
                                ""
                        }


                        region =
                            nuevaRegion


                        menuRegionAbierto =
                            false


                        busquedaRegion =
                            ""


                        menuComunaAbierto =
                            false


                        busquedaComuna =
                            ""


                        error =
                            null
                    }
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            18.dp
                        )
                )


                // =========================
                // COMUNA
                // =========================

                SelectorComuna(

                    comunaSeleccionada =
                        comuna,

                    regionSeleccionada =
                        region,

                    busqueda =
                        busquedaComuna,

                    menuAbierto =
                        menuComunaAbierto,

                    comunas =
                        comunasDisponibles,

                    error =
                        null,


                    onAbrirMenu = {

                        menuComunaAbierto =
                            true

                        busquedaComuna =
                            ""
                    },


                    onCerrarMenu = {

                        menuComunaAbierto =
                            false

                        busquedaComuna =
                            ""
                    },


                    onBusquedaCambio = {

                        busquedaComuna =
                            it
                    },


                    onComunaSeleccionada = {

                        comuna =
                            it


                        menuComunaAbierto =
                            false


                        busquedaComuna =
                            ""


                        error =
                            null
                    }
                )


                error
                    ?.let {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    16.dp
                                )
                        )


                        Text(
                            text =
                                it,

                            color =
                                RojoErrorTextil
                        )
                    }
            }
        },


        confirmButton = {

            Button(

                onClick = {

                    val edadNumero =
                        edad
                            .toIntOrNull()


                    if (
                        nombre
                            .trim()
                            .isEmpty() ||

                        nombreEmprendimiento
                            .trim()
                            .isEmpty() ||

                        rubro
                            .trim()
                            .isEmpty() ||

                        region
                            .trim()
                            .isEmpty() ||

                        comuna
                            .trim()
                            .isEmpty() ||

                        genero
                            .trim()
                            .isEmpty()
                    ) {

                        error =
                            "Debes completar todos los campos"


                        return@Button
                    }


                    if (
                        edadNumero ==
                        null ||

                        edadNumero <=
                        0
                    ) {

                        error =
                            "Ingresa una edad válida"


                        return@Button
                    }


                    if (
                        comuna !in
                        comunasDisponibles
                    ) {

                        error =
                            "Selecciona una comuna válida para la región"


                        return@Button
                    }


                    onGuardar(

                        nombre
                            .trim(),

                        nombreEmprendimiento
                            .trim(),

                        rubro
                            .trim(),

                        region,

                        comuna,

                        edadNumero,

                        genero
                    )
                },


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
                        "GUARDAR"
                )
            }
        },


        dismissButton = {

            TextButton(

                onClick =
                    onCancelar
            ) {

                Text(
                    text =
                        "CANCELAR",

                    color =
                        BlancoTextil
                )
            }
        }
    )
}


// =====================================
// DATOS DE CUENTA
// =====================================

@Composable
fun DatosCuenta(
    cuenta: EmprendedoraEntity
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
            TurquesaTextil,

        style =
            MaterialTheme
                .typography
                .bodyLarge
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
            "Edad: ${cuenta.edad}",

        color =
            BlancoTextil
    )


    Text(
        text =
            "Género: ${cuenta.genero}",

        color =
            BlancoTextil
    )


    Text(
        text =
            "Región: ${cuenta.region}",

        color =
            BlancoTextil
    )


    Text(
        text =
            "Comuna: ${cuenta.comuna}",

        color =
            BlancoTextil
    )


    Text(
        text =
            "Rubro: ${cuenta.rubro}",

        color =
            BlancoTextil
    )
}


// =====================================
// COLORES CAMPOS ADMIN
// =====================================

@Composable
fun coloresCampoAdmin() =
    OutlinedTextFieldDefaults.colors(

        focusedTextColor =
            BlancoTextil,

        unfocusedTextColor =
            BlancoTextil,

        focusedBorderColor =
            TurquesaTextil,

        unfocusedBorderColor =
            Color.Gray,

        focusedLabelColor =
            TurquesaTextil,

        unfocusedLabelColor =
            Color.LightGray,

        cursorColor =
            TurquesaTextil,

        errorBorderColor =
            RojoErrorTextil,

        errorLabelColor =
            RojoErrorTextil,

        errorCursorColor =
            RojoErrorTextil
    )

// =========================
// TARJETA CHAT ADMIN
// =========================
@Composable
fun TarjetaChatAdmin(
    chat: ChatEntity,
    emprendedora: EmprendedoraEntity?,
    onResponder: () -> Unit,
    onCerrarChat: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FondoAdmin)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            val tituloMostrar = if (emprendedora != null) {
                "${emprendedora.nombre} / ${emprendedora.nombreEmprendimiento}"
            } else {
                "Emprendedora desconocida"
            }

            Text(
                text = tituloMostrar,
                color = BlancoTextil,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            val estadoText = if (chat.cerrado) "CERRADO" else "ABIERTO"
            val estadoColor = if (chat.cerrado) RojoErrorTextil else TurquesaTextil

            Text(
                text = "Estado: $estadoText",
                color = estadoColor,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onResponder,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TurquesaTextil,
                        contentColor = NegroTextil
                    )
                ) {
                    Text(text = "VER / RESPONDER")
                }

                if (!chat.cerrado) {
                    Button(
                        onClick = onCerrarChat,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RojoErrorTextil,
                            contentColor = BlancoTextil
                        )
                    ) {
                        Text(text = "CERRAR CHAT")
                    }
                }
            }
        }
    }
}