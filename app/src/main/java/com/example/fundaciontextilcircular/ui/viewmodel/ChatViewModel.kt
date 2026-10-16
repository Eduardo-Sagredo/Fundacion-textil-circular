package com.example.fundaciontextilcircular.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextilcircular.data.repository.ChatRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    // Convertimos el Flow del historial en un Estado que la pantalla pueda dibujar
    val historial = repository.historialChat.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Escuchamos el estado del internet en tiempo real
    val isOnline = networkMonitor.isOnline.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    init {
        // Al iniciar el ViewModel, dejamos un "espía" vigilando el internet
        viewModelScope.launch {
            networkMonitor.isOnline.collect { online ->
                if (online) {
                    // Si detecta que volvió la conexión, empuja los mensajes atascados
                    repository.sincronizarPendientes()
                }
            }
        }
    }

    // Función que llamará el botón de "Enviar" en la pantalla
    fun enviarMensaje(texto: String) {
        if (texto.isBlank()) return

        viewModelScope.launch {
            // 1. Se guarda rapidísimo de forma local (Offline-First)
            repository.guardarMensajeLocal(texto)

            // 2. Si casualmente hay internet ahora mismo, forzamos el envío de inmediato
            if (isOnline.value) {
                repository.sincronizarPendientes()
            }
        }
    }
}