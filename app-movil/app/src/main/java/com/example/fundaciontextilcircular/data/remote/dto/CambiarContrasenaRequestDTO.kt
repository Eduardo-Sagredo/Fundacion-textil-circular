package com.example.fundaciontextilcircular.data.remote.dto

data class CambiarContrasenaRequestDTO(

    val correo: String,

    val contrasenaActual: String,

    val nuevaContrasena: String
)