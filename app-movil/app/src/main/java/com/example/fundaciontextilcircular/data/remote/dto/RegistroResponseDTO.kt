package com.example.fundaciontextilcircular.data.remote.dto

data class RegistroResponseDTO(
    val mensaje: String,
    val id: Long,
    val correo: String,
    val estado: String
)