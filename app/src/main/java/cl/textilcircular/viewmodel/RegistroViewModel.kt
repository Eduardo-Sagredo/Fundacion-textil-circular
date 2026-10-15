package cl.textilcircular.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegistroViewModel : ViewModel() {

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

        errorNombre = validarNombre(nombre)

        errorRut = validarRut(rut)

        errorCorreo = validarCorreo(correo)

        errorContrasena =
            validarContrasena(contrasena)

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
            validarRubro(rubro)

        errorRegion =
            validarRegion(region)

        errorComuna =
            validarComuna(comuna)

        errorEdad =
            validarEdad(edad)

        errorGenero =
            validarGenero(genero)


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


    // NOMBRE

    private fun validarNombre(
        nombre: String
    ): String? {

        if (nombre.isBlank()) {
            return "Debes ingresar tu nombre completo"
        }

        return null
    }


    // RUT

    private fun validarRut(
        rut: String
    ): String? {

        if (rut.isBlank()) {
            return "Debes ingresar tu RUT"
        }

        if (!rutValido(rut)) {
            return "El RUT ingresado no es válido"
        }

        return null
    }


    // CORREO

    private fun validarCorreo(
        correo: String
    ): String? {

        if (correo.isBlank()) {
            return "Debes ingresar tu correo electrónico"
        }

        if (!correo.contains("@")) {
            return "El correo debe contener @"
        }

        val partes = correo.split("@")

        if (partes.size != 2) {
            return "Ingresa un correo electrónico válido"
        }

        if (partes[0].isBlank()) {
            return "Ingresa un correo electrónico válido"
        }

        if (!partes[1].contains(".")) {
            return "El correo debe incluir un dominio válido"
        }

        val formatoCorreo = Regex(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )

        if (!formatoCorreo.matches(correo)) {
            return "Ingresa un correo electrónico válido"
        }

        return null
    }


    // CONTRASEÑA

    private fun validarContrasena(
        contrasena: String
    ): String? {

        if (contrasena.isBlank()) {
            return "Debes ingresar una contraseña"
        }

        if (contrasena.length < 7) {
            return "La contraseña debe tener al menos 7 caracteres"
        }

        if (!contrasena.any { it.isLetter() }) {
            return "La contraseña debe contener al menos una letra"
        }

        if (!contrasena.any { it.isDigit() }) {
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


    // CONFIRMAR CONTRASEÑA

    private fun validarConfirmarContrasena(
        contrasena: String,
        confirmarContrasena: String
    ): String? {

        if (confirmarContrasena.isBlank()) {
            return "Debes confirmar tu contraseña"
        }

        if (contrasena != confirmarContrasena) {
            return "Las contraseñas no coinciden"
        }

        return null
    }


    // NOMBRE DEL EMPRENDIMIENTO

    private fun validarNombreEmprendimiento(
        nombreEmprendimiento: String
    ): String? {

        if (nombreEmprendimiento.isBlank()) {
            return "Debes ingresar el nombre del emprendimiento"
        }

        return null
    }


    // RUBRO

    private fun validarRubro(
        rubro: String
    ): String? {

        if (rubro.isBlank()) {
            return "Debes indicar el rubro o actividad"
        }

        return null
    }


    // REGIÓN

    private fun validarRegion(
        region: String
    ): String? {

        if (region.isBlank()) {
            return "Debes seleccionar una región"
        }

        return null
    }


    // COMUNA

    private fun validarComuna(
        comuna: String
    ): String? {

        if (comuna.isBlank()) {
            return "Debes seleccionar una comuna"
        }

        return null
    }


    // EDAD

    private fun validarEdad(
        edad: String
    ): String? {

        if (edad.isBlank()) {
            return "Debes ingresar tu edad"
        }

        val edadNumero =
            edad.toIntOrNull()

        if (edadNumero == null) {
            return "La edad ingresada no es válida"
        }

        if (edadNumero < 1) {
            return "La edad debe ser mayor o igual a 1"
        }

        return null
    }


    // GÉNERO

    private fun validarGenero(
        genero: String
    ): String? {

        if (genero.isBlank()) {
            return "Debes seleccionar una opción"
        }

        return null
    }


    // VALIDAR DÍGITO VERIFICADOR DEL RUT

    private fun rutValido(
        rut: String
    ): Boolean {

        val rutLimpio = rut
            .uppercase()
            .replace(".", "")
            .replace("-", "")

        if (rutLimpio.length < 2) {
            return false
        }

        val cuerpo =
            rutLimpio.dropLast(1)

        val digitoIngresado =
            rutLimpio.last()

        if (!cuerpo.all { it.isDigit() }) {
            return false
        }

        var suma = 0

        var multiplicador = 2


        for (numero in cuerpo.reversed()) {

            suma +=
                numero.digitToInt() *
                        multiplicador

            multiplicador++

            if (multiplicador > 7) {
                multiplicador = 2
            }
        }


        val resto =
            11 - (suma % 11)


        val digitoCalculado =
            when (resto) {

                11 -> '0'

                10 -> 'K'

                else ->
                    resto
                        .toString()
                        .first()
            }


        return digitoIngresado ==
                digitoCalculado
    }
}