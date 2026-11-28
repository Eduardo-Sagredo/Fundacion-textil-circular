package com.example.fundaciontextilcircular.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextilcircular.data.local.entities.ChatEntity
import com.example.fundaciontextilcircular.data.local.entities.MensajeEntity
import com.example.fundaciontextilcircular.data.repository.ChatRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModel(
    private val repository: ChatRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    // =========================
    // CHAT ACTUAL
    // =========================
    private val _chatIdActual = MutableStateFlow("general")
    val chatIdActual: StateFlow<String> = _chatIdActual

    // =========================
    // HISTORIAL
    // =========================
    val historial: StateFlow<List<MensajeEntity>> =
        _chatIdActual
            .flatMapLatest { chatId ->
                repository.obtenerHistorialChat(chatId)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // =========================
    // INTERNET
    // =========================
    val isOnline: StateFlow<Boolean> =
        networkMonitor
            .isOnline
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false
            )

    // =========================
    // ESTADO DE CHAT CERRADO
    // =========================
    val chatCerrado: StateFlow<Boolean> = _chatIdActual
        .flatMapLatest { chatId ->
            repository.observarEstadoChat(chatId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // =========================
    // TODOS LOS CHATS (ADMIN)
    // =========================
    val todosLosChats: StateFlow<List<ChatEntity>> = repository
        .obtenerTodosLosChats()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var esAdmin: Boolean = false

    fun cerrarChatAdministrador() {
        viewModelScope.launch {
            repository.cerrarChat(_chatIdActual.value)
        }
    }

    // =========================
    // CUANDO VUELVE INTERNET (SUBIDA Y BAJADA)
    // =========================
    init {
        viewModelScope.launch {
            networkMonitor
                .isOnline
                .collect { online ->
                    if (online) {
                        repository.sincronizarPendientes()
                        if (esAdmin) {
                            repository.refrescarTodosLosChats()
                        }
                        if (_chatIdActual.value.isNotBlank()) {
                            repository.refrescarMensajesChat(_chatIdActual.value)
                        }
                    }
                }
        }
    }

    // =========================
    // SELECCIONAR CHAT Y DESCARGAR MENSAJES NUEVOS
    // =========================
    fun seleccionarChat(chatId: String) {
        _chatIdActual.value = chatId
        viewModelScope.launch {
            repository.refrescarMensajesChat(chatId)
        }
    }

    // =========================
    // REFRESCOS MANUALES DESDE LA UI
    // =========================
    fun refrescarBandejaAdmin() {
        viewModelScope.launch {
            repository.refrescarTodosLosChats()
        }
    }

    fun refrescarHistorialActual() {
        viewModelScope.launch {
            repository.refrescarMensajesChat(_chatIdActual.value)
        }
    }

    // =========================
    // ENVIAR MENSAJE (USUARIO O ADMIN)
    // =========================
    fun enviarMensaje(texto: String) {
        if (texto.isBlank()) return

        viewModelScope.launch {
            repository.guardarMensajeLocal(
                chatId = _chatIdActual.value,
                contenido = texto.trim(),
                esDeUsuario = !esAdmin
            )
        }
    }

    // =========================
    // MARCAR COMO LEÍDO
    // =========================
    fun marcarChatComoLeido(chatId: String) {
        viewModelScope.launch {
            repository.marcarChatComoLeido(chatId)
        }
    }
}