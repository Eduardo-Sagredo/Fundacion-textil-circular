package com.example.fundaciontextilcircular.data.repository

import com.example.fundaciontextilcircular.data.local.dao.MensajeDao
import com.example.fundaciontextilcircular.data.local.entities.MensajeEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class ChatRepository(private val mensajeDao: MensajeDao) {

    //Se Expone el historial completo para que la pantalla lo lea
    val historialChat: Flow<List<MensajeEntity>> = mensajeDao.obtenerHistorialChat()

    // Guardar un mensaje escrito por la emprendedora
    suspend fun guardarMensajeLocal(contenido: String) {
        val nuevoMensaje = MensajeEntity(
            contenido = contenido,
            esDeUsuario = true,
            estaPendiente = true // Siempre nace como "pendiente" por diseño Offline-First
        )
        mensajeDao.insertarMensaje(nuevoMensaje)
    }

    //Motor de sincronización (Simulación de Backend)
    suspend fun sincronizarPendientes() {
        val pendientes = mensajeDao.obtenerMensajesPendientes()

        if (pendientes.isNotEmpty()) {
            // Simulacion de un pequeño retraso de red de 1.5 segundos para que se vea realista
            delay(1500)

            pendientes.forEach { mensaje ->
                // A) Marcamos el mensaje de la usuaria como enviado (doble check)
                mensajeDao.marcarComoEnviado(mensaje.id)

                // B) Generamos la respuesta automática del sistema
                val respuestaAutomatica = MensajeEntity(
                    contenido = "Hemos recibido tu mensaje: '${mensaje.contenido}'. Una administradora te responderá a la brevedad.",
                    esDeUsuario = false,
                    estaPendiente = false // Las respuestas del sistema ya nacen entregadas
                )
                mensajeDao.insertarMensaje(respuestaAutomatica)
            }
        }
    }
}