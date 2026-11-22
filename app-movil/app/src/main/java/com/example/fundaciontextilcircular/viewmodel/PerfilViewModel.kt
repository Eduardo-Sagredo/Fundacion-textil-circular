package com.example.fundaciontextilcircular.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextilcircular.data.remote.dto.PerfilResponseDTO
import com.example.fundaciontextilcircular.data.repository.AuthRepository
import kotlinx.coroutines.launch


data class PerfilUiState(

    val cargando: Boolean = false,

    val perfil: PerfilResponseDTO? = null,

    val error: String? = null,

    val mensaje: String? = null,

    val cambiandoContrasena: Boolean = false
)


class PerfilViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(
        PerfilUiState()
    )
        private set


    // =========================================
    // CARGAR PERFIL
    // =========================================

    fun cargarPerfil(
        correo: String,
        isOnline: Boolean
    ) {

        if (
            correo.isBlank()
        ) {

            uiState =
                uiState.copy(
                    error =
                        "No se encontró el correo del usuario"
                )

            return
        }


        if (
            !isOnline
        ) {

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
                authRepository.obtenerPerfil(
                    correo
                )


            resultado
                .onSuccess {
                        perfil ->

                    uiState =
                        uiState.copy(
                            cargando = false,
                            perfil = perfil,
                            error = null
                        )
                }
                .onFailure {
                        error ->

                    uiState =
                        uiState.copy(
                            cargando = false,
                            error =
                                error.message
                                    ?: "No se pudo cargar el perfil"
                        )
                }
        }
    }


    // =========================================
    // CAMBIAR CONTRASEÑA
    // =========================================

    fun cambiarContrasena(
        correo: String,
        contrasenaActual: String,
        nuevaContrasena: String,
        confirmarContrasena: String,
        isOnline: Boolean
    ) {

        limpiarMensajes()


        if (
            contrasenaActual.isBlank() ||
            nuevaContrasena.isBlank() ||
            confirmarContrasena.isBlank()
        ) {

            uiState =
                uiState.copy(
                    error =
                        "Debes completar todos los campos"
                )

            return
        }


        if (
            nuevaContrasena.length < 7
        ) {

            uiState =
                uiState.copy(
                    error =
                        "La nueva contraseña debe tener al menos 7 caracteres"
                )

            return
        }


        if (
            !nuevaContrasena.any {
                it.isLetter()
            }
        ) {

            uiState =
                uiState.copy(
                    error =
                        "La nueva contraseña debe contener una letra"
                )

            return
        }


        if (
            !nuevaContrasena.any {
                it.isDigit()
            }
        ) {

            uiState =
                uiState.copy(
                    error =
                        "La nueva contraseña debe contener un número"
                )

            return
        }


        if (
            !nuevaContrasena.any {

                !it.isLetterOrDigit() &&
                        !it.isWhitespace()
            }
        ) {

            uiState =
                uiState.copy(
                    error =
                        "La nueva contraseña debe contener un carácter especial"
                )

            return
        }


        if (
            nuevaContrasena !=
            confirmarContrasena
        ) {

            uiState =
                uiState.copy(
                    error =
                        "Las contraseñas no coinciden"
                )

            return
        }


        if (
            nuevaContrasena ==
            contrasenaActual
        ) {

            uiState =
                uiState.copy(
                    error =
                        "La nueva contraseña debe ser diferente a la actual"
                )

            return
        }


        if (
            !isOnline
        ) {

            uiState =
                uiState.copy(
                    error =
                        "No tienes conexión a internet"
                )

            return
        }


        uiState =
            uiState.copy(
                cambiandoContrasena = true,
                error = null,
                mensaje = null
            )


        viewModelScope.launch {

            val resultado =
                authRepository
                    .cambiarContrasena(

                        correo =
                            correo,

                        contrasenaActual =
                            contrasenaActual,

                        nuevaContrasena =
                            nuevaContrasena
                    )


            resultado
                .onSuccess {
                        mensaje ->

                    uiState =
                        uiState.copy(
                            cambiandoContrasena = false,
                            mensaje = mensaje,
                            error = null
                        )
                }
                .onFailure {
                        error ->

                    uiState =
                        uiState.copy(
                            cambiandoContrasena = false,
                            error =
                                error.message
                                    ?: "No se pudo cambiar la contraseña",
                            mensaje = null
                        )
                }
        }
    }


    fun limpiarMensajes() {

        uiState =
            uiState.copy(
                error = null,
                mensaje = null
            )
    }
}


class PerfilViewModelFactory(
    private val authRepository:
    AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                PerfilViewModel::class.java
            )
        ) {

            return PerfilViewModel(
                authRepository
            ) as T
        }


        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}