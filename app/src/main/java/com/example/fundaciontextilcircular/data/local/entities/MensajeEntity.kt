package com.example.fundaciontextilcircular.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mensajes")
data class MensajeEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    val chatId: String,

    val contenido: String,

    val esDeUsuario: Boolean,

    val estaPendiente: Boolean,

    // true = el mensaje ya fue leído
    // false = hay un mensaje nuevo
    val leido: Boolean = true,

    val timestamp: Long =
        System.currentTimeMillis()
)