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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import kotlinx.coroutines.delay

private val FondoTarjetaAdmin = Color(0xFF1E1E1E)

@Composable
fun PantallaEliminarUsuarios(
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {}
) {
    val context = LocalContext.current

    val networkMonitor = remember {
        NetworkMonitor(context.applicationContext)
    }

    val isOnline by networkMonitor
        .isOnline
        .collectAsState(initial = true)

    val adminRepository = remember {
        AdminRepository(RetrofitClient.adminApi)
    }

    val adminViewModel: AdminViewModel = viewModel(
        factory = AdminViewModelFactory(adminRepository)
    )

    val uiState = adminViewModel.uiState

    var textoBusqueda by remember {
        mutableStateOf("")
    }

    var usuarioAEliminar by remember {
        mutableStateOf<AdminSolicitudDTO?>(null)
    }

    LaunchedEffect(textoBusqueda, isOnline) {
        if (textoBusqueda.isNotBlank()) {
            delay(300)
        }
        adminViewModel.buscarUsuariosPorNombre(
            nombre = textoBusqueda,
            isOnline = isOnline
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NegroTextil)
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "ELIMINAR USUARIOS",
            color = TurquesaTextil,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Busca una cuenta por nombre para eliminarla",
            color = BlancoTextil,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = {
                textoBusqueda = it
                adminViewModel.limpiarError()
                adminViewModel.limpiarMensaje()
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = BlancoTextil),
            label = {
                Text(
                    text = "Buscar por nombre",
                    color = TurquesaTextil
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = TurquesaTextil
                )
            },
            trailingIcon = {
                if (textoBusqueda.isNotEmpty()) {
                    IconButton(onClick = { textoBusqueda = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Limpiar búsqueda",
                            tint = BlancoTextil
                        )
                    }
                }
            },
            singleLine = true
        )

        uiState.mensaje?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                color = TurquesaTextil
            )
        }

        uiState.error?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                color = RojoErrorTextil
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.buscandoUsuarios && uiState.usuariosBuscados.isEmpty()) {
                item {
                    Text(
                        text = "Buscando usuarios...",
                        color = BlancoTextil
                    )
                }
            } else if (uiState.usuariosBuscados.isEmpty()) {
                item {
                    Text(
                        text = "No se encontraron usuarios con ese nombre.",
                        color = BlancoTextil
                    )
                }
            } else {
                items(
                    items = uiState.usuariosBuscados,
                    key = { "usuario_${it.id}" }
                ) { usuario ->
                    TarjetaUsuarioEliminar(
                        usuario = usuario,
                        habilitado = !uiState.cargando,
                        onSolicitarEliminar = {
                            usuarioAEliminar = usuario
                            adminViewModel.limpiarError()
                            adminViewModel.limpiarMensaje()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "VOLVER AL PANEL",
                color = BlancoTextil
            )
        }
    }

    usuarioAEliminar?.let { usuario ->
        AlertDialog(
            onDismissRequest = { usuarioAEliminar = null },
            containerColor = FondoTarjetaAdmin,
            title = {
                Text(
                    text = "Confirmar eliminación",
                    color = RojoErrorTextil
                )
            },
            text = {
                Text(
                    text = "¿Seguro que deseas eliminar permanentemente la cuenta de ${usuario.nombre} (${usuario.correo})? Esta acción no se puede deshacer.",
                    color = BlancoTextil
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        adminViewModel.eliminarUsuario(
                            id = usuario.id,
                            isOnline = isOnline
                        )
                        usuarioAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RojoErrorTextil,
                        contentColor = BlancoTextil
                    )
                ) {
                    Text("ELIMINAR")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { usuarioAEliminar = null }
                ) {
                    Text(
                        text = "CANCELAR",
                        color = TurquesaTextil
                    )
                }
            }
        )
    }
}

@Composable
private fun TarjetaUsuarioEliminar(
    usuario: AdminSolicitudDTO,
    habilitado: Boolean,
    onSolicitarEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = FondoTarjetaAdmin
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = usuario.nombre,
                color = BlancoTextil,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = usuario.nombreEmprendimiento,
                color = TurquesaTextil
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "RUT: ${usuario.rut}",
                color = BlancoTextil
            )

            Text(
                text = "Correo: ${usuario.correo}",
                color = BlancoTextil
            )

            Text(
                text = "Estado: ${usuario.estado}",
                color = AmarilloTextil,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onSolicitarEliminar,
                    enabled = habilitado,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RojoErrorTextil,
                        contentColor = BlancoTextil
                    )
                ) {
                    Text("ELIMINAR CUENTA")
                }
            }
        }
    }
}