package com.example.fundaciontextilcircular.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mensajes")
data class MensajeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val remoteId: Long? = null,

    val chatId: String,

    val contenido: String,

    val esDeUsuario: Boolean,

    val estaPendiente: Boolean = true,

    val leido: Boolean = false,

    val timestamp: Long =
        System.currentTimeMillis()
)