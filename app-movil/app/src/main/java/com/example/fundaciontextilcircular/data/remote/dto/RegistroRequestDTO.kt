package com.example.fundaciontextilcircular.data.remote.dto

data class RegistroRequestDTO(
    val rut: String,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val nombreEmprendimiento: String,
    val rubro: String,
    val region: String,
    val comuna: String,
    val edad: Int,
    val genero: String
)