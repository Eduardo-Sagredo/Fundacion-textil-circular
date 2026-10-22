package com.example.fundaciontextilcircular.data.repository

import com.example.fundaciontextilcircular.data.local.dao.MensajeDao
import com.example.fundaciontextilcircular.data.local.entities.MensajeEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepository(
    private val mensajeDao: MensajeDao
) {

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
        contenido: String
    ) {

        val nuevoMensaje =
            MensajeEntity(

                chatId =
                    chatId,

                contenido =
                    contenido,

                esDeUsuario =
                    true,

                estaPendiente =
                    true,

                // El mensaje que yo envío
                // no cuenta como no leído

                leido =
                    true
            )

        mensajeDao
            .insertarMensaje(
                nuevoMensaje
            )
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
    }


    // =========================
    // SINCRONIZAR
    // =========================

    suspend fun sincronizarPendientes() {

        val pendientes =
            mensajeDao
                .obtenerTodosLosMensajesPendientes()

        if (
            pendientes.isNotEmpty()
        ) {

            delay(1500)

            pendientes.forEach {
                    mensaje ->

                mensajeDao
                    .marcarComoEnviado(
                        mensaje.id
                    )


                // =========================
                // RESPUESTA RECIBIDA
                // =========================

                val respuestaAutomatica =
                    MensajeEntity(

                        chatId =
                            mensaje.chatId,

                        contenido =
                            "Hemos recibido tu mensaje. Una administradora te responderá a la brevedad.",

                        esDeUsuario =
                            false,

                        estaPendiente =
                            false,

                        leido =
                            false
                    )

                mensajeDao
                    .insertarMensaje(
                        respuestaAutomatica
                    )
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