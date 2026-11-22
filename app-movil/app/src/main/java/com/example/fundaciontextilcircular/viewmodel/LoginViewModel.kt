package com.example.fundaciontextilcircular.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextilcircular.data.remote.dto.LoginResponseDTO
import com.example.fundaciontextilcircular.data.repository.AuthRepository
import kotlinx.coroutines.launch

data class LoginUiState(
    val cargando: Boolean = false,
    val error: String? = null,
    val resultado: LoginResponseDTO? = null
)

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(
        LoginUiState()
    )
        private set


    fun iniciarSesion(
        correo: String,
        contrasena: String,
        isOnline: Boolean
    ) {

        val correoNormalizado =
            correo
                .trim()
                .lowercase()


        // =========================
        // VALIDACIÓN CAMPOS
        // =========================

        if (
            correoNormalizado.isBlank() ||
            contrasena.isBlank()
        ) {

            uiState =
                LoginUiState(
                    error =
                        "Debes ingresar correo y contraseña"
                )

            return
        }


        // =========================
        // VALIDACIÓN INTERNET
        // =========================

        if (!isOnline) {

            uiState =
                LoginUiState(
                    error =
                        "No tienes conexión a internet"
                )

            return
        }


        // =========================
        // CARGANDO
        // =========================

        uiState =
            LoginUiState(
                cargando = true
            )


        // =========================
        // LOGIN REMOTO
        // =========================

        viewModelScope.launch {

            val resultado =
                authRepository.login(
                    correo = correoNormalizado,
                    contrasena = contrasena
                )


            resultado
                .onSuccess { respuesta ->

                    uiState =
                        LoginUiState(
                            cargando = false,
                            resultado = respuesta
                        )
                }
                .onFailure { error ->

                    uiState =
                        LoginUiState(
                            cargando = false,
                            error =
                                error.message
                                    ?: "No se pudo iniciar sesión"
                        )
                }
        }
    }


    fun limpiarResultado() {

        uiState =
            uiState.copy(
                resultado = null
            )
    }


    fun limpiarError() {

        uiState =
            uiState.copy(
                error = null
            )
    }
}


// =========================
// FACTORY
// =========================

class LoginViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                LoginViewModel::class.java
            )
        ) {

            return LoginViewModel(
                authRepository
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}