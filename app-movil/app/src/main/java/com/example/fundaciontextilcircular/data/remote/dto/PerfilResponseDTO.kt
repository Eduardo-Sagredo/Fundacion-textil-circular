package com.example.fundaciontextilcircular.data.remote.dto

data class PerfilResponseDTO(

    val id: Long,

    val rut: String,

    val nombre: String,

    val correo: String,

    val nombreEmprendimiento: String,

    val rubro: String,

    val region: String,

    val comuna: String,

    val edad: Int,

    val genero: String,

    val rol: String,

    val estado: String,

    val motivoRechazo: String?
)