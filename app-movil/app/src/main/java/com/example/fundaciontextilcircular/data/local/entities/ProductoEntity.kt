package com.example.fundaciontextilcircular.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val descripcion: String,

    val precio: Int,

    val tipo: String,

    val imagenRuta: String?,

    val correoEmprendedora: String,

    val nombreEmprendimiento: String,

    // DISPONIBLE o VENDIDO
    val estado: String = "DISPONIBLE",

    val fechaCreacion: Long =
        System.currentTimeMillis()
)