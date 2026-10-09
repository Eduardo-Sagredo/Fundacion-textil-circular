package com.example.fundaciontextilcircular.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emprendedora")
data class EmprendedoraEntity(
    @PrimaryKey val rut: String,
    val nombre: String,
    val nombreEmprendimiento: String,
    val rubro: String,
    val region: String,
    val comuna: String,
    val genero: String,
    val rangoEtario: String
) {

}