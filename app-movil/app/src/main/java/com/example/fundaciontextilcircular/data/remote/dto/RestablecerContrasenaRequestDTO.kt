package com.example.fundaciontextilcircular.data.remote.dto

data class RestablecerContrasenaRequestDTO(
    val token: String,
    val nuevaContrasena: String
)