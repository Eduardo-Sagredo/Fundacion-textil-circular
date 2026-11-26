package com.example.fundaciontextilcircular.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import com.example.fundaciontextilcircular.data.repository.AuthRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class RecuperarContrasenaUiState(

    val cargando: Boolean = false,

    val codigoEnviado: Boolean = false,

    val contrasenaRestablecida: Boolean = false,

    val mensaje: String? = null,

    val error: String? = null
)


class RecuperarContrasenaViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _uiState =
        MutableStateFlow(
            RecuperarContrasenaUiState()
        )


    val uiState: StateFlow<RecuperarContrasenaUiState> =
        _uiState.asStateFlow()


    // =========================================
    // ENVIAR CÓDIGO
    // =========================================

    fun enviarCodigo(
        correo: String,
        isOnline: Boolean
    ) {

        val correoNormalizado =
            correo
                .trim()
                .lowercase()


        if (
            correoNormalizado.isBlank()
        ) {

            _uiState.value =
                RecuperarContrasenaUiState(
                    error =
                        "Debes ingresar tu correo electrónico"
                )

            return
        }


        if (
            !correoNormalizado.contains("@") ||
            !correoNormalizado.contains(".")
        ) {

            _uiState.value =
                RecuperarContrasenaUiState(
                    error =
                        "Ingresa un correo electrónico válido"
                )

            return
        }


        if (
            !isOnline
        ) {

            _uiState.value =
                RecuperarContrasenaUiState(
                    error =
                        "No tienes conexión a internet"
                )

            return
        }


        _uiState.value =
            RecuperarContrasenaUiState(
                cargando = true
            )


        viewModelScope.launch {

            val resultado =
                authRepository
                    .olvideContrasena(
                        correoNormalizado
                    )


            resultado
                .onSuccess { mensaje ->

                    _uiState.value =
                        RecuperarContrasenaUiState(
                            codigoEnviado = true,
                            mensaje = mensaje
                        )
                }
                .onFailure { error ->

                    _uiState.value =
                        RecuperarContrasenaUiState(
                            error =
                                error.message
                                    ?: "No se pudo enviar el código"
                        )
                }
        }
    }


    // =========================================
    // RESTABLECER CONTRASEÑA
    // =========================================

    fun restablecerContrasena(
        codigo: String,
        nuevaContrasena: String,
        confirmarContrasena: String,
        isOnline: Boolean
    ) {

        val codigoNormalizado =
            codigo.trim()


        if (
            codigoNormalizado.length != 6 ||
            !codigoNormalizado.all {
                it.isDigit()
            }
        ) {

            _uiState.value =
                _uiState.value.copy(
                    error =
                        "El código debe tener 6 dígitos",
                    mensaje = null
                )

            return
        }


        if (
            nuevaContrasena.isBlank()
        ) {

            _uiState.value =
                _uiState.value.copy(
                    error =
                        "Debes ingresar una nueva contraseña",
                    mensaje = null
                )

            return
        }


        if (
            nuevaContrasena.length < 8
        ) {

            _uiState.value =
                _uiState.value.copy(
                    error =
                        "La contraseña debe tener al menos 8 caracteres",
                    mensaje = null
                )

            return
        }


        if (
            nuevaContrasena !=
            confirmarContrasena
        ) {

            _uiState.value =
                _uiState.value.copy(
                    error =
                        "Las contraseñas no coinciden",
                    mensaje = null
                )

            return
        }


        if (
            !isOnline
        ) {

            _uiState.value =
                _uiState.value.copy(
                    error =
                        "No tienes conexión a internet",
                    mensaje = null
                )

            return
        }


        _uiState.value =
            _uiState.value.copy(
                cargando = true,
                error = null,
                mensaje = null
            )


        viewModelScope.launch {

            val resultado =
                authRepository
                    .restablecerContrasena(
                        token =
                            codigoNormalizado,

                        nuevaContrasena =
                            nuevaContrasena
                    )


            resultado
                .onSuccess { mensaje ->

                    _uiState.value =
                        RecuperarContrasenaUiState(
                            codigoEnviado = true,
                            contrasenaRestablecida = true,
                            mensaje = mensaje
                        )
                }
                .onFailure { error ->

                    _uiState.value =
                        _uiState.value.copy(
                            cargando = false,
                            error =
                                error.message
                                    ?: "No se pudo cambiar la contraseña",
                            mensaje = null
                        )
                }
        }
    }


    // =========================================
    // LIMPIAR MENSAJES
    // =========================================

    fun limpiarMensajes() {

        _uiState.value =
            _uiState.value.copy(
                mensaje = null,
                error = null
            )
    }


    // =========================================
    // REINICIAR FLUJO COMPLETO
    // =========================================

    fun reiniciarFlujo() {

        _uiState.value =
            RecuperarContrasenaUiState()
    }
}


// =========================================
// FACTORY
// =========================================

class RecuperarContrasenaViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                RecuperarContrasenaViewModel::class.java
            )
        ) {

            return RecuperarContrasenaViewModel(
                authRepository
            ) as T
        }


        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}