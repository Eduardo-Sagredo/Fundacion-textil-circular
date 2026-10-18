package com.example.fundaciontextilcircular.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fundaciontextilcircular.data.local.entities.MensajeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MensajeDao {


    //Guardar un nuevo mensaje
    @Insert
    suspend fun insertarMensaje(mensaje: MensajeEntity)

    //Obtener todo el chat en tiempo real ordenado por fecha
    @Query("SELECT * FROM mensajes ORDER BY timestamp ASC")
    fun obtenerHistorialChat(): Flow<List<MensajeEntity>>

    //Obtener solo los mensajes que se quedaron sin enviar por estar sin conexion
    @Query("SELECT * FROM mensajes WHERE estaPendiente = 1")
    suspend fun obtenerMensajesPendientes(): List<MensajeEntity>

    //Actualizar un mensaje específico para marcarlo como enviado
    @Query("UPDATE mensajes SET estaPendiente = 0 WHERE id = :mensajeId")
    suspend fun marcarComoEnviado(mensajeId: Int)


}