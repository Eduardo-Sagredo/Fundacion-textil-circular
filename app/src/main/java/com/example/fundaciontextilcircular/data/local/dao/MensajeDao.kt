package com.example.fundaciontextilcircular.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fundaciontextilcircular.data.local.entities.MensajeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MensajeDao {

    // =========================
    // INSERTAR MENSAJE
    // =========================

    @Insert
    suspend fun insertarMensaje(
        mensaje: MensajeEntity
    )


    // =========================
    // HISTORIAL DE UN CHAT
    // =========================

    @Query(
        """
        SELECT *
        FROM mensajes
        WHERE chatId = :chatId
        ORDER BY timestamp ASC
        """
    )
    fun obtenerMensajesPorChat(
        chatId: String
    ): Flow<List<MensajeEntity>>


    // =========================
    // ÚLTIMO MENSAJE DEL CHAT
    // =========================

    @Query(
        """
        SELECT *
        FROM mensajes
        WHERE chatId = :chatId
        ORDER BY timestamp DESC
        LIMIT 1
        """
    )
    fun observarUltimoMensaje(
        chatId: String
    ): Flow<MensajeEntity?>


    // =========================
    // CANTIDAD DE MENSAJES
    // NO LEÍDOS DE UN CHAT
    // =========================

    @Query(
        """
        SELECT COUNT(*)
        FROM mensajes
        WHERE chatId = :chatId
        AND esDeUsuario = 0
        AND leido = 0
        """
    )
    fun observarCantidadNoLeidos(
        chatId: String
    ): Flow<Int>


    // =========================
    // TOTAL DE MENSAJES
    // NO LEÍDOS
    // =========================

    @Query(
        """
        SELECT COUNT(*)
        FROM mensajes
        WHERE esDeUsuario = 0
        AND leido = 0
        """
    )
    fun observarTotalNoLeidos():
            Flow<Int>


    // =========================
    // MARCAR CHAT COMO LEÍDO
    // =========================

    @Query(
        """
        UPDATE mensajes
        SET leido = 1
        WHERE chatId = :chatId
        AND esDeUsuario = 0
        """
    )
    suspend fun marcarChatComoLeido(
        chatId: String
    )


    // =========================
    // MENSAJES PENDIENTES
    // DE UN CHAT
    // =========================

    @Query(
        """
        SELECT *
        FROM mensajes
        WHERE chatId = :chatId
        AND estaPendiente = 1
        ORDER BY timestamp ASC
        """
    )
    suspend fun obtenerMensajesPendientesPorChat(
        chatId: String
    ): List<MensajeEntity>


    // =========================
    // TODOS LOS MENSAJES
    // PENDIENTES
    // =========================

    @Query(
        """
        SELECT *
        FROM mensajes
        WHERE estaPendiente = 1
        ORDER BY timestamp ASC
        """
    )
    suspend fun obtenerTodosLosMensajesPendientes():
            List<MensajeEntity>


    // =========================
    // MARCAR COMO ENVIADO
    // =========================

    @Query(
        """
        UPDATE mensajes
        SET estaPendiente = 0
        WHERE id = :mensajeId
        """
    )
    suspend fun marcarComoEnviado(
        mensajeId: Int
    )

    // ===================================
    // ESTADO DEL CHAT (CERRADO/ABIERTO)
    // ===================================

    // Inserta o actualiza el estado general del chat
    @Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    suspend fun guardarChat(chat: com.example.fundaciontextilcircular.data.local.entities.ChatEntity)

    @Query(
        """
        SELECT cerrado 
        FROM chats 
        WHERE chatId = :chatId
        """
    )
    fun observarEstadoChat(chatId: String): Flow<Boolean?>

    // =========================
    // OBTENER TODOS LOS CHATS (ADMIN)
    // =========================

    @Query(
        """
        SELECT *
        FROM chats
        """
    )
    fun obtenerTodosLosChats(): Flow<List<com.example.fundaciontextilcircular.data.local.entities.ChatEntity>>

    // =========================
    // CERRAR UN CHAT (ADMIN)
    // =========================

    @Query(
        """
        UPDATE chats 
        SET cerrado = 1 
        WHERE chatId = :chatId
        """
    )
    suspend fun cerrarChat(chatId: String)

    // =========================
    // OBTENER TICKETS DEL USUARIO
    // =========================
    @Query(
        """
        SELECT * 
        FROM chats 
        WHERE chatId LIKE '%' || :correoLimpio || '%'
        """
    )
    fun obtenerMisTickets(correoLimpio: String): Flow<List<com.example.fundaciontextilcircular.data.local.entities.ChatEntity>>

    // =========================
    // CONTAR RESPUESTAS DE ADMIN
    // =========================
    @Query("""
        SELECT COUNT(*) 
        FROM mensajes 
        WHERE chatId = :chatId AND esDeUsuario = 0
    """)
    suspend fun contarRespuestasAdmin(chatId: String): Int

    // =========================
    // OBTENER MENSAJES SÍNCRONO PARA CONTEO
    // =========================
    @Query(
        """
        SELECT *
        FROM mensajes
        WHERE chatId = :chatId
        """
    )
    suspend fun obtenerMensajesPorChatSincrono(chatId: String): List<MensajeEntity>
}