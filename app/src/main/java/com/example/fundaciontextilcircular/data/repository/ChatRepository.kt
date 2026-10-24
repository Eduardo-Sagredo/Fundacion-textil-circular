package com.example.fundaciontextilcircular.data.repository

import com.example.fundaciontextilcircular.data.local.dao.MensajeDao
import com.example.fundaciontextilcircular.data.local.entities.ChatEntity
import com.example.fundaciontextilcircular.data.local.entities.MensajeEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ChatRepository(
    private val mensajeDao: MensajeDao
) {
    private val syncMutex = Mutex()

    // =========================
    // OBTENER TODOS LOS CHATS (ADMIN)
    // =========================
    fun obtenerTodosLosChats(): Flow<List<ChatEntity>> {
        return mensajeDao.obtenerTodosLosChats()
    }

    // =========================
    // OBTENER HISTORIAL
    // =========================

    fun obtenerHistorialChat(
        chatId: String
    ): Flow<List<MensajeEntity>> {

        return mensajeDao
            .obtenerMensajesPorChat(
                chatId
            )
    }


    // =========================
    // GUARDAR MENSAJE LOCAL
    // =========================

    suspend fun guardarMensajeLocal(
        chatId: String,
        contenido: String,
        esDeUsuario: Boolean = true
    ) {

        val nuevoMensaje = MensajeEntity(
            chatId = chatId,
            contenido = contenido,
            esDeUsuario = esDeUsuario,
            estaPendiente = esDeUsuario,
            leido = true
        )
        mensajeDao.insertarMensaje(nuevoMensaje)

        val chat = ChatEntity(
            chatId = chatId,
            cerrado = false
        )
        mensajeDao.guardarChat(chat)
    }


    // =========================
    // MARCAR CHAT COMO LEÍDO
    // =========================

    suspend fun marcarChatComoLeido(
        chatId: String
    ) {

        mensajeDao
            .marcarChatComoLeido(
                chatId
            )

        val chat = ChatEntity(
            chatId = chatId,
            cerrado = false
        )
        mensajeDao.guardarChat(chat)
    }


    // =========================
    // SINCRONIZAR
    // =========================
    suspend fun sincronizarPendientes() {

        syncMutex.withLock {
            val pendientes = mensajeDao.obtenerTodosLosMensajesPendientes()

            if (pendientes.isNotEmpty()) {
                delay(300)

                val chatsAfectados = mutableSetOf<String>()

                pendientes.forEach { mensajeLocal ->
                    mensajeDao.marcarComoEnviado(mensajeLocal.id)
                    chatsAfectados.add(mensajeLocal.chatId)
                }

                chatsAfectados.forEach { idDelChat ->

                    val historialDelChat = mensajeDao.obtenerMensajesPorChatSincrono(idDelChat)
                    val cantidadRespuestas = historialDelChat.count { !it.esDeUsuario }

                    if (cantidadRespuestas == 0) {
                        val respuestaAutomatica = MensajeEntity(
                            chatId = idDelChat,
                            contenido = "Hemos recibido tu mensaje. Una administradora te responderá a la brevedad.",
                            esDeUsuario = false,
                            estaPendiente = false,
                            leido = false
                        )
                        mensajeDao.insertarMensaje(respuestaAutomatica)
                    }
                }
            }
        }
    }


    // =========================
    // ESTADO DE CHAT CERRADO
    // =========================

    fun observarEstadoChat(chatId: String): Flow<Boolean> {
        return mensajeDao.observarEstadoChat(chatId).map { cerrado ->
            cerrado ?: false
        }
    }

    suspend fun cerrarChat(chatId: String) {
        val chat = com.example.fundaciontextilcircular.data.local.entities.ChatEntity(
            chatId = chatId,
            cerrado = true
        )
        mensajeDao.guardarChat(chat)
    }
}