package com.example.fundaciontextilcircular.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "emprendedora",
    indices = [
        Index(
            value = ["correo"],
            unique = true
        )
    ]
)
data class EmprendedoraEntity(

    @PrimaryKey
    val rut: String,

    val nombre: String,

    val correo: String,

    val contrasena: String,

    val nombreEmprendimiento: String,

    val rubro: String,

    val region: String,

    val comuna: String,

    val edad: Int,

    val genero: String,

    val rol: String = "EMPRENDEDORA",

    val estado: String = "PENDIENTE",

    val motivoRechazo: String? = null
)