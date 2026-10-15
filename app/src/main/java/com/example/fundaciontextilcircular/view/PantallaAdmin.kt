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
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity
import com.example.fundaciontextilcircular.ui.theme.AmarilloTextil
import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.RojoErrorTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil
import kotlinx.coroutines.launch

@Composable
fun PantallaAdmin(
    modifier: Modifier = Modifier,
    onCerrarSesion: () -> Unit = {}
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val dao = remember(context) {
        TextilDatabase
            .getDatabase(context)
            .emprendedoraDao()
    }


    // CUENTAS PENDIENTES

    val pendientes by dao
        .obtenerPendientes()
        .collectAsState(
            initial = emptyList()
        )


    // CUENTAS ACTIVAS

    val activas by dao
        .obtenerAprobadas()
        .collectAsState(
            initial = emptyList()
        )


    // CUENTAS RECHAZADAS

    val rechazadas by dao
        .obtenerRechazadas()
        .collectAsState(
            initial = emptyList()
        )


    // CUENTA QUE SE QUIERE RECHAZAR

    var solicitudARechazar by remember {
        mutableStateOf<EmprendedoraEntity?>(
            null
        )
    }


    // MOTIVO ESCRITO POR EL ADMIN

    var motivoRechazo by remember {
        mutableStateOf("")
    }


    // CUENTA QUE SE QUIERE ELIMINAR

    var cuentaAEliminar by remember {
        mutableStateOf<EmprendedoraEntity?>(
            null
        )
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NegroTextil)
            .padding(20.dp)
    ) {

        Spacer(
            modifier = Modifier.height(30.dp)
        )


        Text(
            text = "ADMINISTRACIÓN",
            color = TurquesaTextil,
            style =
                MaterialTheme.typography.headlineLarge
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        Text(
            text = "Gestión de cuentas",
            color = BlancoTextil,
            style =
                MaterialTheme.typography.titleMedium
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {


            // =============================
            // SOLICITUDES PENDIENTES
            // =============================

            item {

                Text(
                    text =
                        "SOLICITUDES PENDIENTES (${pendientes.size})",
                    color = AmarilloTextil,
                    style =
                        MaterialTheme.typography.titleMedium
                )
            }


            if (pendientes.isEmpty()) {

                item {

                    Text(
                        text =
                            "No hay cuentas pendientes.",
                        color = BlancoTextil
                    )
                }

            } else {

                items(
                    items = pendientes,
                    key = {
                        "pendiente_${it.rut}"
                    }
                ) { cuenta ->

                    TarjetaSolicitud(
                        solicitud = cuenta,

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
                        }
                    )
                }
            }


            item {

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )
            }


            // =============================
            // CUENTAS ACTIVAS
            // =============================

            item {

                Text(
                    text =
                        "CUENTAS ACTIVAS (${activas.size})",
                    color = TurquesaTextil,
                    style =
                        MaterialTheme.typography.titleMedium
                )
            }


            if (activas.isEmpty()) {

                item {

                    Text(
                        text =
                            "No hay cuentas activas.",
                        color = BlancoTextil
                    )
                }

            } else {

                items(
                    items = activas,
                    key = {
                        "activa_${it.rut}"
                    }
                ) { cuenta ->

                    TarjetaCuentaActiva(
                        cuenta = cuenta,

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
                        Modifier.height(20.dp)
                )
            }


            // =============================
            // CUENTAS RECHAZADAS
            // =============================

            item {

                Text(
                    text =
                        "CUENTAS RECHAZADAS (${rechazadas.size})",
                    color = RojoErrorTextil,
                    style =
                        MaterialTheme.typography.titleMedium
                )
            }


            if (rechazadas.isEmpty()) {

                item {

                    Text(
                        text =
                            "No hay cuentas rechazadas.",
                        color = BlancoTextil
                    )
                }

            } else {

                items(
                    items = rechazadas,
                    key = {
                        "rechazada_${it.rut}"
                    }
                ) { cuenta ->

                    TarjetaCuentaRechazada(
                        cuenta = cuenta,

                        onEliminar = {

                            cuentaAEliminar =
                                cuenta
                        }
                    )
                }
            }
        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        OutlinedButton(
            onClick = onCerrarSesion,
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text = "CERRAR SESIÓN",
                color = BlancoTextil
            )
        }
    }


    // =================================
    // DIÁLOGO RECHAZAR CUENTA
    // =================================

    solicitudARechazar?.let { cuenta ->

        AlertDialog(

            onDismissRequest = {

                solicitudARechazar =
                    null

                motivoRechazo =
                    ""
            },

            title = {

                Text(
                    text =
                        "Rechazar solicitud"
                )
            },

            text = {

                Column {

                    Text(
                        text =
                            "Indica el motivo del rechazo."
                    )


                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
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
                        }
                    )
                }
            },

            confirmButton = {

                Button(
                    onClick = {

                        // GUARDAMOS PRIMERO EL TEXTO
                        // EN UNA VARIABLE NORMAL

                        val motivo =
                            motivoRechazo
                                .trim()


                        if (
                            motivo.isNotBlank()
                        ) {

                            scope.launch {

                                // GUARDAMOS EN ROOM

                                dao.rechazarCuenta(
                                    rut =
                                        cuenta.rut,

                                    motivo =
                                        motivo
                                )


                                // RECIÉN DESPUÉS
                                // LIMPIAMOS EL CAMPO

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
                            "Cancelar"
                    )
                }
            }
        )
    }


    // =================================
    // DIÁLOGO ELIMINAR CUENTA
    // =================================

    cuentaAEliminar?.let { cuenta ->

        AlertDialog(

            onDismissRequest = {

                cuentaAEliminar =
                    null
            },

            title = {

                Text(
                    text =
                        "Eliminar cuenta"
                )
            },

            text = {

                Text(
                    text =
                        "¿Estás seguro de que deseas eliminar la cuenta de ${cuenta.nombre}? Esta acción no se puede deshacer."
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
                            "Cancelar"
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
                        Color(
                            0xFF1E1E1E
                        )
                )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            DatosCuenta(
                cuenta =
                    solicitud
            )


            Spacer(
                modifier =
                    Modifier.height(12.dp)
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
                    Modifier.height(18.dp)
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
                        Modifier.weight(1f),

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
                        Modifier.weight(1f),

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


// =====================================
// TARJETA CUENTA ACTIVA
// =====================================

@Composable
fun TarjetaCuentaActiva(
    cuenta: EmprendedoraEntity,
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
                        Color(
                            0xFF1E1E1E
                        )
                )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            DatosCuenta(
                cuenta =
                    cuenta
            )


            Spacer(
                modifier =
                    Modifier.height(12.dp)
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
                    Modifier.height(18.dp)
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
                        Color(
                            0xFF1E1E1E
                        )
                )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            DatosCuenta(
                cuenta =
                    cuenta
            )


            Spacer(
                modifier =
                    Modifier.height(12.dp)
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
                    Modifier.height(8.dp)
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
                    Modifier.height(18.dp)
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
// DATOS COMUNES DE LA CUENTA
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
            Modifier.height(4.dp)
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
            Modifier.height(12.dp)
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