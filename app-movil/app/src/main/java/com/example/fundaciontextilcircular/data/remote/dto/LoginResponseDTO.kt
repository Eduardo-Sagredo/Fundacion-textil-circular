package com.example.fundaciontextilcircular.data.remote.dto

data class LoginResponseDTO(
    val exito: Boolean,
    val mensaje: String,
    val id: Long,
    val nombre: String,
    val correo: String,
    val rol: String,
    val estado: String,
    val motivoRechazo: String?,
    val puedeIngresar: Boolean
)