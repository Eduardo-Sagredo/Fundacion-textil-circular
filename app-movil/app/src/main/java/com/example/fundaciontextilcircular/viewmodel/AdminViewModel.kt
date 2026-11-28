package com.example.fundaciontextilcircular.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextilcircular.data.remote.dto.AdminSolicitudDTO
import com.example.fundaciontextilcircular.data.repository.AdminRepository
import kotlinx.coroutines.launch

data class AdminUiState(
    val cargando: Boolean = false,
    val solicitudes: List<AdminSolicitudDTO> = emptyList(),
    val buscandoUsuarios: Boolean = false,
    val usuariosBuscados: List<AdminSolicitudDTO> = emptyList(),
    val error: String? = null,
    val mensaje: String? = null
)

class AdminViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    var uiState by mutableStateOf(
        AdminUiState()
    )
        private set


    fun cargarSolicitudes(
        isOnline: Boolean
    ) {

        if (!isOnline) {

            uiState =
                uiState.copy(
                    cargando = false,
                    error =
                        "No tienes conexión a internet"
                )

            return
        }


        uiState =
            uiState.copy(
                cargando = true,
                error = null
            )


        viewModelScope.launch {

            val resultado =
                adminRepository.obtenerSolicitudes()


            resultado
                .onSuccess { solicitudes ->

                    uiState =
                        uiState.copy(
                            cargando = false,
                            solicitudes = solicitudes,
                            error = null
                        )
                }
                .onFailure { error ->

                    uiState =
                        uiState.copy(
                            cargando = false,
                            error =
                                error.message
                                    ?: "No se pudieron cargar las solicitudes"
                        )
                }
        }
    }


    fun aprobarSolicitud(
        id: Long,
        isOnline: Boolean
    ) {

        if (!isOnline) {

            uiState =
                uiState.copy(
                    error =
                        "No tienes conexión a internet",
                    mensaje = null
                )

            return
        }


        uiState =
            uiState.copy(
                cargando = true,
                error = null,
                mensaje = null
            )


        viewModelScope.launch {

            val resultado =
                adminRepository.aprobarSolicitud(id)


            resultado
                .onSuccess {

                    uiState =
                        uiState.copy(
                            cargando = false,

                            solicitudes =
                                uiState
                                    .solicitudes
                                    .filterNot {
                                        it.id == id
                                    },

                            error = null,

                            mensaje =
                                "Solicitud aprobada correctamente"
                        )
                }
                .onFailure { error ->

                    uiState =
                        uiState.copy(
                            cargando = false,
                            error =
                                error.message
                                    ?: "No se pudo aprobar la solicitud",
                            mensaje = null
                        )
                }
        }
    }


    fun rechazarSolicitud(
        id: Long,
        motivo: String,
        isOnline: Boolean
    ) {

        val motivoLimpio =
            motivo.trim()


        if (motivoLimpio.isBlank()) {

            uiState =
                uiState.copy(
                    error =
                        "Debes indicar el motivo del rechazo",
                    mensaje = null
                )

            return
        }


        if (motivoLimpio.length > 500) {

            uiState =
                uiState.copy(
                    error =
                        "El motivo no puede superar los 500 caracteres",
                    mensaje = null
                )

            return
        }


        if (!isOnline) {

            uiState =
                uiState.copy(
                    error =
                        "No tienes conexión a internet",
                    mensaje = null
                )

            return
        }


        uiState =
            uiState.copy(
                cargando = true,
                error = null,
                mensaje = null
            )


        viewModelScope.launch {

            val resultado =
                adminRepository.rechazarSolicitud(
                    id = id,
                    motivo = motivoLimpio
                )


            resultado
                .onSuccess {

                    uiState =
                        uiState.copy(
                            cargando = false,

                            solicitudes =
                                uiState
                                    .solicitudes
                                    .filterNot {
                                        it.id == id
                                    },

                            error = null,

                            mensaje =
                                "Solicitud rechazada correctamente"
                        )
                }
                .onFailure { error ->

                    uiState =
                        uiState.copy(
                            cargando = false,
                            error =
                                error.message
                                    ?: "No se pudo rechazar la solicitud",
                            mensaje = null
                        )
                }
        }
    }

    fun buscarUsuariosPorNombre(
        nombre: String,
        isOnline: Boolean
    ) {
        if (!isOnline) {
            uiState = uiState.copy(
                buscandoUsuarios = false,
                error = "No tienes conexión a internet"
            )
            return
        }

        val filtro = nombre.trim()

        uiState = uiState.copy(
            buscandoUsuarios = true,
            error = null
        )

        viewModelScope.launch {
            val resultado = adminRepository.buscarUsuarios(filtro)

            resultado
                .onSuccess { lista ->
                    val filtrados = if (filtro.isBlank()) {
                        lista
                    } else {
                        lista.filter {
                            it.nombre.contains(filtro, ignoreCase = true)
                        }
                    }

                    uiState = uiState.copy(
                        buscandoUsuarios = false,
                        usuariosBuscados = filtrados,
                        error = null
                    )
                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        buscandoUsuarios = false,
                        error = error.message
                            ?: "No se pudieron buscar los usuarios"
                    )
                }
        }
    }

    fun eliminarUsuario(
        id: Long,
        isOnline: Boolean
    ) {
        if (!isOnline) {
            uiState = uiState.copy(
                error = "No tienes conexión a internet",
                mensaje = null
            )
            return
        }

        uiState = uiState.copy(
            cargando = true,
            error = null,
            mensaje = null
        )

        viewModelScope.launch {
            val resultado = adminRepository.eliminarUsuario(id)

            resultado
                .onSuccess {
                    uiState = uiState.copy(
                        cargando = false,
                        usuariosBuscados = uiState.usuariosBuscados.filterNot { it.id == id },
                        solicitudes = uiState.solicitudes.filterNot { it.id == id },
                        error = null,
                        mensaje = "Cuenta eliminada correctamente"
                    )
                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        cargando = false,
                        error = error.message
                            ?: "No se pudo eliminar la cuenta del usuario",
                        mensaje = null
                    )
                }
        }
    }


    fun limpiarError() {

        uiState =
            uiState.copy(
                error = null
            )
    }


    fun limpiarMensaje() {

        uiState =
            uiState.copy(
                mensaje = null
            )
    }
}


class AdminViewModelFactory(
    private val adminRepository:
    AdminRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                AdminViewModel::class.java
            )
        ) {

            return AdminViewModel(
                adminRepository
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}