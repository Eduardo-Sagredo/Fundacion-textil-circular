package com.example.fundaciontextilcircular.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextilcircular.data.remote.dto.RegistroRequestDTO
import com.example.fundaciontextilcircular.data.remote.dto.RegistroResponseDTO
import com.example.fundaciontextilcircular.data.repository.AuthRepository
import kotlinx.coroutines.launch


data class RegistroUiState(
    val cargando: Boolean = false,
    val error: String? = null,
    val resultado: RegistroResponseDTO? = null
)


class RegistroViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(
        RegistroUiState()
    )
        private set


    var errorNombre by mutableStateOf<String?>(null)
        private set

    var errorRut by mutableStateOf<String?>(null)
        private set

    var errorCorreo by mutableStateOf<String?>(null)
        private set

    var errorContrasena by mutableStateOf<String?>(null)
        private set

    var errorConfirmarContrasena by mutableStateOf<String?>(null)
        private set

    var errorNombreEmprendimiento by mutableStateOf<String?>(null)
        private set

    var errorRubro by mutableStateOf<String?>(null)
        private set

    var errorRegion by mutableStateOf<String?>(null)
        private set

    var errorComuna by mutableStateOf<String?>(null)
        private set

    var errorEdad by mutableStateOf<String?>(null)
        private set

    var errorGenero by mutableStateOf<String?>(null)
        private set


    fun validarFormulario(
        nombre: String,
        rut: String,
        correo: String,
        contrasena: String,
        confirmarContrasena: String,
        nombreEmprendimiento: String,
        rubro: String,
        region: String,
        comuna: String,
        edad: String,
        genero: String
    ): Boolean {

        errorNombre =
            validarNombre(
                nombre
            )

        errorRut =
            validarRut(
                rut
            )

        errorCorreo =
            validarCorreo(
                correo
            )

        errorContrasena =
            validarContrasena(
                contrasena
            )

        errorConfirmarContrasena =
            validarConfirmarContrasena(
                contrasena,
                confirmarContrasena
            )

        errorNombreEmprendimiento =
            validarNombreEmprendimiento(
                nombreEmprendimiento
            )

        errorRubro =
            validarRubro(
                rubro
            )

        errorRegion =
            validarRegion(
                region
            )

        errorComuna =
            validarComuna(
                comuna
            )

        errorEdad =
            validarEdad(
                edad
            )

        errorGenero =
            validarGenero(
                genero
            )

        return errorNombre == null &&
                errorRut == null &&
                errorCorreo == null &&
                errorContrasena == null &&
                errorConfirmarContrasena == null &&
                errorNombreEmprendimiento == null &&
                errorRubro == null &&
                errorRegion == null &&
                errorComuna == null &&
                errorEdad == null &&
                errorGenero == null
    }


    fun registrar(
        nombre: String,
        rut: String,
        correo: String,
        contrasena: String,
        nombreEmprendimiento: String,
        rubro: String,
        region: String,
        comuna: String,
        edad: String,
        genero: String,
        isOnline: Boolean
    ) {

        val correoNormalizado =
            correo
                .trim()
                .lowercase()


        val rutNormalizado =
            rut
                .trim()
                .uppercase()


        if (
            !isOnline
        ) {

            uiState =
                RegistroUiState(
                    error =
                        "No tienes conexión a internet"
                )

            return
        }


        if (
            correoNormalizado ==
            "admin@textilcircular.cl"
        ) {

            uiState =
                RegistroUiState(
                    error =
                        "Este correo está reservado para el administrador"
                )

            return
        }


        val edadNumero =
            edad.toIntOrNull()


        if (
            edadNumero == null
        ) {

            uiState =
                RegistroUiState(
                    error =
                        "La edad ingresada no es válida"
                )

            return
        }


        val request =
            RegistroRequestDTO(
                rut = rutNormalizado,
                nombre = nombre.trim(),
                correo = correoNormalizado,
                contrasena = contrasena,
                nombreEmprendimiento =
                    nombreEmprendimiento.trim(),
                rubro = rubro.trim(),
                region = region.trim(),
                comuna = comuna.trim(),
                edad = edadNumero,
                genero = genero.trim()
            )


        uiState =
            RegistroUiState(
                cargando = true
            )


        viewModelScope.launch {

            val resultado =
                authRepository.registrar(
                    request
                )


            resultado
                .onSuccess {
                        respuesta ->

                    uiState =
                        RegistroUiState(
                            cargando = false,
                            resultado = respuesta
                        )
                }
                .onFailure {
                        error ->

                    uiState =
                        RegistroUiState(
                            cargando = false,
                            error =
                                error.message
                                    ?: "No se pudo crear la cuenta"
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


    fun limpiarResultado() {

        uiState =
            uiState.copy(
                resultado = null
            )
    }


    private fun validarNombre(
        nombre: String
    ): String? {

        if (
            nombre.isBlank()
        ) {
            return "Debes ingresar tu nombre completo"
        }

        return null
    }


    private fun validarRut(
        rut: String
    ): String? {

        if (
            rut.isBlank()
        ) {
            return "Debes ingresar tu RUT"
        }

        if (
            !rutValido(
                rut
            )
        ) {
            return "El RUT ingresado no es válido"
        }

        return null
    }


    private fun validarCorreo(
        correo: String
    ): String? {

        if (
            correo.isBlank()
        ) {
            return "Debes ingresar tu correo electrónico"
        }

        if (
            !correo.contains("@")
        ) {
            return "El correo debe contener @"
        }


        val partes =
            correo.split("@")


        if (
            partes.size != 2
        ) {
            return "Ingresa un correo electrónico válido"
        }


        if (
            partes[0].isBlank()
        ) {
            return "Ingresa un correo electrónico válido"
        }


        if (
            !partes[1].contains(".")
        ) {
            return "El correo debe incluir un dominio válido"
        }


        val formatoCorreo =
            Regex(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            )


        if (
            !formatoCorreo.matches(
                correo
            )
        ) {
            return "Ingresa un correo electrónico válido"
        }


        return null
    }


    private fun validarContrasena(
        contrasena: String
    ): String? {

        if (
            contrasena.isBlank()
        ) {
            return "Debes ingresar una contraseña"
        }


        if (
            contrasena.length < 7
        ) {
            return "La contraseña debe tener al menos 7 caracteres"
        }


        if (
            !contrasena.any {
                it.isLetter()
            }
        ) {
            return "La contraseña debe contener al menos una letra"
        }


        if (
            !contrasena.any {
                it.isDigit()
            }
        ) {
            return "La contraseña debe contener al menos un número"
        }


        if (
            !contrasena.any {

                !it.isLetterOrDigit() &&
                        !it.isWhitespace()
            }
        ) {
            return "La contraseña debe contener al menos un carácter especial"
        }


        return null
    }


    private fun validarConfirmarContrasena(
        contrasena: String,
        confirmarContrasena: String
    ): String? {

        if (
            confirmarContrasena.isBlank()
        ) {
            return "Debes confirmar tu contraseña"
        }


        if (
            contrasena !=
            confirmarContrasena
        ) {
            return "Las contraseñas no coinciden"
        }


        return null
    }


    private fun validarNombreEmprendimiento(
        nombreEmprendimiento: String
    ): String? {

        if (
            nombreEmprendimiento.isBlank()
        ) {
            return "Debes ingresar el nombre del emprendimiento"
        }

        return null
    }


    private fun validarRubro(
        rubro: String
    ): String? {

        if (
            rubro.isBlank()
        ) {
            return "Debes indicar el rubro o actividad"
        }

        return null
    }


    private fun validarRegion(
        region: String
    ): String? {

        if (
            region.isBlank()
        ) {
            return "Debes seleccionar una región"
        }

        return null
    }


    private fun validarComuna(
        comuna: String
    ): String? {

        if (
            comuna.isBlank()
        ) {
            return "Debes seleccionar una comuna"
        }

        return null
    }


    private fun validarEdad(
        edad: String
    ): String? {

        if (
            edad.isBlank()
        ) {
            return "Debes ingresar tu edad"
        }


        val edadNumero =
            edad.toIntOrNull()


        if (
            edadNumero == null
        ) {
            return "La edad ingresada no es válida"
        }


        if (
            edadNumero < 1
        ) {
            return "La edad debe ser mayor o igual a 1"
        }


        return null
    }


    private fun validarGenero(
        genero: String
    ): String? {

        if (
            genero.isBlank()
        ) {
            return "Debes seleccionar una opción"
        }

        return null
    }


    private fun rutValido(
        rut: String
    ): Boolean {

        val rutLimpio =
            rut
                .uppercase()
                .replace(".", "")
                .replace("-", "")


        if (
            rutLimpio.length < 2
        ) {
            return false
        }


        val cuerpo =
            rutLimpio
                .dropLast(1)


        val digitoIngresado =
            rutLimpio
                .last()


        if (
            !cuerpo.all {
                it.isDigit()
            }
        ) {
            return false
        }


        var suma = 0

        var multiplicador = 2


        for (
        numero in cuerpo.reversed()
        ) {

            suma +=
                numero.digitToInt() *
                        multiplicador


            multiplicador++


            if (
                multiplicador > 7
            ) {
                multiplicador = 2
            }
        }


        val resto =
            11 - (
                    suma % 11
                    )


        val digitoCalculado =
            when (
                resto
            ) {

                11 ->
                    '0'

                10 ->
                    'K'

                else ->
                    resto
                        .toString()
                        .first()
            }


        return digitoIngresado ==
                digitoCalculado
    }
}


class RegistroViewModelFactory(
    private val authRepository:
    AuthRepository
) : ViewModelProvider.Factory {

    @Suppress(
        "UNCHECKED_CAST"
    )

    override fun <
            T : ViewModel
            > create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                RegistroViewModel::class.java
            )
        ) {

            return RegistroViewModel(
                authRepository
            ) as T
        }


        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}