package cl.textilcircular.view

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cl.textilcircular.ui.theme.AmarilloTextil
import cl.textilcircular.ui.theme.BlancoTextil
import cl.textilcircular.ui.theme.NegroTextil
import cl.textilcircular.ui.theme.RojoErrorTextil
import cl.textilcircular.ui.theme.TurquesaTextil


data class SolicitudCuenta(
    val id: Int,
    val nombre: String,
    val emprendimiento: String,
    val rut: String,
    val region: String,
    val comuna: String,
    val rubro: String,
    val estado: String = "PENDIENTE",
    val motivoRechazo: String = ""
)


@Composable
fun PantallaAdmin(
    modifier: Modifier = Modifier,
    onCerrarSesion: () -> Unit = {}
) {

    val solicitudes = remember {

        mutableStateListOf(

            SolicitudCuenta(
                id = 1,
                nombre = "María González",
                emprendimiento = "Telas del Mar",
                rut = "12345678-5",
                region = "Valparaíso",
                comuna = "Viña del Mar",
                rubro = "Reparación textil"
            ),

            SolicitudCuenta(
                id = 2,
                nombre = "Andrea Pérez",
                emprendimiento = "Segunda Vida",
                rut = "98765432-1",
                region = "Metropolitana de Santiago",
                comuna = "Santiago",
                rubro = "Reutilización textil"
            )
        )
    }

    var solicitudARechazar by remember {
        mutableStateOf<SolicitudCuenta?>(null)
    }

    var motivoRechazo by remember {
        mutableStateOf("")
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
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Solicitudes de registro",
            color = BlancoTextil,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )


        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(
                items = solicitudes,
                key = {
                    it.id
                }
            ) { solicitud ->

                TarjetaSolicitud(
                    solicitud = solicitud,

                    onAprobar = {

                        val posicion =
                            solicitudes.indexOfFirst {
                                it.id == solicitud.id
                            }

                        if (posicion != -1) {

                            solicitudes[posicion] =
                                solicitud.copy(
                                    estado = "APROBADA"
                                )
                        }
                    },

                    onRechazar = {

                        solicitudARechazar = solicitud
                        motivoRechazo = ""
                    }
                )
            }
        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )

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


    if (solicitudARechazar != null) {

        AlertDialog(

            onDismissRequest = {

                solicitudARechazar = null
                motivoRechazo = ""
            },

            title = {

                Text(
                    text = "Rechazar solicitud"
                )
            },

            text = {

                Column {

                    Text(
                        text = "Indica el motivo del rechazo."
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedTextField(
                        value = motivoRechazo,
                        onValueChange = {
                            motivoRechazo = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Motivo")
                        }
                    )
                }
            },

            confirmButton = {

                Button(
                    onClick = {

                        val solicitud =
                            solicitudARechazar

                        if (
                            solicitud != null &&
                            motivoRechazo.isNotBlank()
                        ) {

                            val posicion =
                                solicitudes.indexOfFirst {
                                    it.id == solicitud.id
                                }

                            if (posicion != -1) {

                                solicitudes[posicion] =
                                    solicitud.copy(
                                        estado = "RECHAZADA",
                                        motivoRechazo =
                                            motivoRechazo
                                    )
                            }

                            solicitudARechazar = null
                            motivoRechazo = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            RojoErrorTextil,
                        contentColor =
                            BlancoTextil
                    )
                ) {

                    Text(
                        text = "RECHAZAR"
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {

                        solicitudARechazar = null
                        motivoRechazo = ""
                    }
                ) {

                    Text(
                        text = "Cancelar"
                    )
                }
            }
        )
    }
}


@Composable
fun TarjetaSolicitud(
    solicitud: SolicitudCuenta,
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

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = solicitud.emprendimiento,
                color = TurquesaTextil,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = "RUT: ${solicitud.rut}",
                color = BlancoTextil
            )

            Text(
                text = "Región: ${solicitud.region}",
                color = BlancoTextil
            )

            Text(
                text = "Comuna: ${solicitud.comuna}",
                color = BlancoTextil
            )

            Text(
                text = "Rubro: ${solicitud.rubro}",
                color = BlancoTextil
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Estado: ${solicitud.estado}",
                color = when (solicitud.estado) {

                    "PENDIENTE" ->
                        AmarilloTextil

                    "APROBADA" ->
                        TurquesaTextil

                    else ->
                        RojoErrorTextil
                },
                style = MaterialTheme.typography.labelLarge
            )


            if (
                solicitud.estado == "RECHAZADA" &&
                solicitud.motivoRechazo.isNotBlank()
            ) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Motivo: ${solicitud.motivoRechazo}",
                    color = RojoErrorTextil,
                    style =
                        MaterialTheme.typography.bodyMedium
                )
            }


            if (solicitud.estado == "PENDIENTE") {

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    Button(
                        onClick = onAprobar,
                        modifier = Modifier.weight(1f),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    TurquesaTextil,
                                contentColor =
                                    NegroTextil
                            )
                    ) {

                        Text(
                            text = "APROBAR"
                        )
                    }


                    Button(
                        onClick = onRechazar,
                        modifier = Modifier.weight(1f),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    RojoErrorTextil,
                                contentColor =
                                    BlancoTextil
                            )
                    ) {

                        Text(
                            text = "RECHAZAR"
                        )
                    }
                }
            }
        }
    }
}