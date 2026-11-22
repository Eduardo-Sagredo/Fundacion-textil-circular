package com.example.fundaciontextilcircular.data.repository

import android.content.Context
import androidx.work.*
import com.example.fundaciontextilcircular.data.local.dao.MensajeDao
import com.example.fundaciontextilcircular.data.local.entities.ChatEntity
import com.example.fundaciontextilcircular.data.local.entities.MensajeEntity
import com.example.fundaciontextilcircular.data.network.RetrofitClient
import com.example.fundaciontextilcircular.data.network.TextilCircularApiService
import com.example.fundaciontextilcircular.data.network.MensajeRequestDto
import com.example.fundaciontextilcircular.data.worker.SincronizarMensajesWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ChatRepository(
    private val mensajeDao: MensajeDao,
    private val apiService: TextilCircularApiService = RetrofitClient.apiService,
    private val appContext: Context? = null
) {
    private val syncMutex = Mutex()

    // =========================
    // OBTENER TODOS LOS CHATS (ADMIN)
    // =========================
    fun obtenerTodosLosChats(): Flow<List<ChatEntity>> {
        return mensajeDao.obtenerTodosLosChats()
    }

    suspend fun refrescarTodosLosChats() {
        try {
            val response = apiService.listarChats()
            if (response.isSuccessful) {
                response.body()?.forEach { chatDto ->
                    mensajeDao.guardarChat(
                        ChatEntity(
                            chatId = chatDto.chatId,
                            cerrado = chatDto.cerrado
                        )
                    )
                }
            }
        } catch (_: Exception) {

        }
    }

    // =========================
    // OBTENER HISTORIAL
    // =========================
    fun obtenerHistorialChat(chatId: String): Flow<List<MensajeEntity>> {
        return mensajeDao.obtenerMensajesPorChat(chatId)
    }

    suspend fun refrescarMensajesChat(chatId: String) {
        try {
            val response = apiService.obtenerMensajesPorChat(chatId)
            if (response.isSuccessful) {
                response.body()?.forEach { dto ->
                    if (!mensajeDao.existeRemoteId(dto.id)) {
                        mensajeDao.insertarMensaje(
                            MensajeEntity(
                                remoteId = dto.id,
                                chatId = dto.chatId,
                                contenido = dto.contenido,
                                esDeUsuario = dto.esDeUsuario,
                                estaPendiente = false,
                                leido = dto.leido
                            )
                        )
                    }
                }
            }
        } catch (_: Exception) {

        }
    }

    // =========================
    // GUARDAR MENSAJE LOCAL + DISPARAR ENVÍO
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
            estaPendiente = true,
            leido = true
        )
        mensajeDao.insertarMensaje(nuevoMensaje)

        val chat = ChatEntity(
            chatId = chatId,
            cerrado = false
        )
        mensajeDao.guardarChat(chat)
        sincronizarPendientes()
        if (mensajeDao.obtenerTodosLosMensajesPendientes().isNotEmpty()) {
            programarSincronizacionEnSegundoPlano()
        }
    }

    // =========================
    // MARCAR CHAT COMO LEÍDO
    // =========================
    suspend fun marcarChatComoLeido(chatId: String) {
        mensajeDao.marcarChatComoLeido(chatId)
    }

    // =========================
    // SINCRONIZAR PENDIENTES CONTRA SPRING BOOT
    // =========================
    suspend fun sincronizarPendientes() {


        syncMutex.withLock {
            val pendientes = mensajeDao.obtenerTodosLosMensajesPendientes()
            if (pendientes.isEmpty()) return

            for (mensajeLocal in pendientes) {
                try {
                    val request = MensajeRequestDto(
                        chatId = mensajeLocal.chatId,
                        contenido = mensajeLocal.contenido,
                        esDeUsuario = mensajeLocal.esDeUsuario,
                        leido = mensajeLocal.leido
                    )

                    val response = apiService.enviarMensaje(request)

                    if (response.isSuccessful) {
                        val mensajesServidor = response.body().orEmpty()

                        mensajesServidor.firstOrNull()?.let { confirmado ->
                            mensajeDao.confirmarEnvio(
                                localId = mensajeLocal.id,
                                remoteId = confirmado.id
                            )
                        }

                        mensajesServidor.drop(1).forEach { extraDto ->
                            if (!mensajeDao.existeRemoteId(extraDto.id)) {
                                mensajeDao.insertarMensaje(
                                    MensajeEntity(
                                        remoteId = extraDto.id,
                                        chatId = extraDto.chatId,
                                        contenido = extraDto.contenido,
                                        esDeUsuario = extraDto.esDeUsuario,
                                        estaPendiente = false,
                                        leido = extraDto.leido
                                    )
                                )
                            }
                        }
                    } else if (response.code() == 409) {
                        mensajeDao.guardarChat(
                            ChatEntity(chatId = mensajeLocal.chatId, cerrado = true)
                        )
                        mensajeDao.eliminarMensajePorId(mensajeLocal.id)
                    }
                } catch (_: Exception) {
                    break
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
        mensajeDao.guardarChat(
            ChatEntity(chatId = chatId, cerrado = true)
        )
        try {
            apiService.cerrarChat(chatId)
        } catch (_: Exception) {
        }
    }

    private fun programarSincronizacionEnSegundoPlano() {
        val context = appContext ?: return
        val restricciones = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<SincronizarMensajesWorker>()
            .setConstraints(restricciones)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "sync_mensajes_pendientes",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}