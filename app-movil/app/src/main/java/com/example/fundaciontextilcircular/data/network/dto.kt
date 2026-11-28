package com.example.fundaciontextilcircular.data.network

// --- Mensajería ---
data class MensajeRequestDto(
    val chatId: String,
    val contenido: String,
    val esDeUsuario: Boolean,
    val leido: Boolean = false
)

data class MensajeResponseDto(
    val id: Long,
    val chatId: String,
    val contenido: String,
    val esDeUsuario: Boolean,
    val leido: Boolean,
    val fechaEnvio: String
)

data class ChatResponseDto(
    val chatId: String,
    val cerrado: Boolean,
    val fechaCreacion: String
)

// --- Productos ---
data class ProductoRequestDto(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val imagenUrl: String?,
    val disponible: Boolean = true,
    val usuarioId: Long?
)

data class ProductoResponseDto(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val imagenUrl: String?,
    val disponible: Boolean,
    val usuarioId: Long?,
    val fechaCreacion: String
)