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
        TextilDatabase.getDatabase(context).emprendedoraDao()
    }

    val solicitudes by dao.obtenerPendientes()
        .collectAsState(initial = emptyList())

    var solicitudARechazar by remember {
        mutableStateOf<EmprendedoraEntity?>(null)
    }
    var motivoRechazo by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NegroTextil)
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "ADMINISTRACIÓN",
            color = TurquesaTextil,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Solicitudes de registro",
            color = BlancoTextil,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (solicitudes.isEmpty()) {
            Text(
                text = "No hay cuentas pendientes de revisión.",
                color = BlancoTextil,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = solicitudes,
                    key = { it.rut }
                ) { solicitud ->
                    TarjetaSolicitud(
                        solicitud = solicitud,
                        onAprobar = {
                            scope.launch {
                                dao.aprobarCuenta(solicitud.rut)
                            }
                        },
                        onRechazar = {
                            solicitudARechazar = solicitud
                            motivoRechazo = ""
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "CERRAR SESIÓN",
                color = BlancoTextil
            )
        }
    }

    solicitudARechazar?.let { solicitud ->
        AlertDialog(
            onDismissRequest = {
                solicitudARechazar = null
                motivoRechazo = ""
            },
            title = {
                Text(text = "Rechazar solicitud")
            },
            text = {
                Column {
                    Text(text = "Indica el motivo del rechazo.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = motivoRechazo,
                        onValueChange = { motivoRechazo = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Motivo") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (motivoRechazo.isNotBlank()) {
                            scope.launch {
                                dao.rechazarCuenta(
                                    rut = solicitud.rut,
                                    motivo = motivoRechazo.trim()
                                )
                            }
                            solicitudARechazar = null
                            motivoRechazo = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RojoErrorTextil,
                        contentColor = BlancoTextil
                    )
                ) {
                    Text(text = "RECHAZAR")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        solicitudARechazar = null
                        motivoRechazo = ""
                    }
                ) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}

@Composable
fun TarjetaSolicitud(
    solicitud: EmprendedoraEntity,
    onAprobar: () -> Unit,
    onRechazar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = solicitud.nombre,
                color = BlancoTextil,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = solicitud.nombreEmprendimiento,
                color = TurquesaTextil,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(text = "RUT: ${solicitud.rut}", color = BlancoTextil)
            Text(text = "Correo: ${solicitud.correo}", color = BlancoTextil)
            Text(text = "Región: ${solicitud.region}", color = BlancoTextil)
            Text(text = "Comuna: ${solicitud.comuna}", color = BlancoTextil)
            Text(text = "Rubro: ${solicitud.rubro}", color = BlancoTextil)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Estado: ${solicitud.estado}",
                color = AmarilloTextil,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onAprobar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TurquesaTextil,
                        contentColor = NegroTextil
                    )
                ) {
                    Text(text = "APROBAR")
                }

                Button(
                    onClick = onRechazar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RojoErrorTextil,
                        contentColor = BlancoTextil
                    )
                ) {
                    Text(text = "RECHAZAR")
                }
            }
        }
    }
}
