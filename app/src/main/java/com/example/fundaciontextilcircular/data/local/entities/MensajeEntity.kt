package com.example.fundaciontextilcircular.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mensajes")
data class MensajeEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val contenido : String,
    val esDeUsuario: Boolean,
    val estaPendiente: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)