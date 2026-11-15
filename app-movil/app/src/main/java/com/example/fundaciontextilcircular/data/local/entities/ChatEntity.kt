package com.example.fundaciontextilcircular.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey
    val chatId: String,
    val cerrado: Boolean = false
)