package com.example.fundaciontextilcircular.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _chatIdActual =
        MutableStateFlow(
            "general"
        )

    val chatIdActual:
            StateFlow<String> =
        _chatIdActual


    // =========================
    // HISTORIAL
    // =========================

    val historial:
            StateFlow<List<MensajeEntity>> =

        _chatIdActual
            .flatMapLatest {
                    chatId ->

                repository
                    .obtenerHistorialChat(
                        chatId
                    )
            }
            .stateIn(
                scope =
                    viewModelScope,

                started =
                    SharingStarted
                        .WhileSubscribed(
                            5000
                        ),

                initialValue =
                    emptyList()
            )


    // =========================
    // INTERNET
    // =========================

    val isOnline =
        networkMonitor
            .isOnline
            .stateIn(

                scope =
                    viewModelScope,

                started =
                    SharingStarted
                        .WhileSubscribed(
                            5000
                        ),

                initialValue =
                    false
            )


    // =========================
    // CUANDO VUELVE INTERNET
    // =========================

    init {

        viewModelScope.launch {

            networkMonitor
                .isOnline
                .collect {
                        online ->

                    if (online) {

                        repository
                            .sincronizarPendientes()
                    }
                }
        }
    }


    // =========================
    // SELECCIONAR CHAT
    // =========================

    fun seleccionarChat(
        chatId: String
    ) {

        _chatIdActual.value =
            chatId
    }


    // =========================
    // ENVIAR MENSAJE
    // =========================

    fun enviarMensaje(
        texto: String
    ) {

        if (
            texto.isBlank()
        ) {

            return
        }

        viewModelScope.launch {

            repository
                .guardarMensajeLocal(

                    chatId =
                        _chatIdActual.value,

                    contenido =
                        texto.trim()
                )

            if (
                isOnline.value
            ) {

                repository
                    .sincronizarPendientes()
            }
        }
    }


    // =========================
    // MARCAR COMO LEÍDO
    // =========================

    fun marcarChatComoLeido(
        chatId: String
    ) {

        viewModelScope.launch {

            repository
                .marcarChatComoLeido(
                    chatId
                )
        }
    }
}