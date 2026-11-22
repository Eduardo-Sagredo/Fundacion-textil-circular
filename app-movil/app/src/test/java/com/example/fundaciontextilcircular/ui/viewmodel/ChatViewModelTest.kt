package com.example.fundaciontextilcircular.ui.viewmodel

import com.example.fundaciontextilcircular.data.local.entities.ChatEntity
import com.example.fundaciontextilcircular.data.local.entities.MensajeEntity
import com.example.fundaciontextilcircular.data.repository.ChatRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor
import com.example.fundaciontextilcircular.viewmodel.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: ChatRepository
    private lateinit var networkMonitor: NetworkMonitor
    private lateinit var isOnlineFlow: MutableStateFlow<Boolean>

    @Before
    fun configurar() {
        repository = mockk(relaxed = true)
        networkMonitor = mockk(relaxed = true)
        isOnlineFlow = MutableStateFlow(false)

        every { networkMonitor.isOnline } returns isOnlineFlow
        every { repository.obtenerHistorialChat(any()) } returns flowOf(emptyList())
        every { repository.observarEstadoChat(any()) } returns flowOf(false)
        every { repository.obtenerTodosLosChats() } returns flowOf(emptyList())
    }

    // =========================================
    // 1. FLUJOS Y SINCRONIZACIÓN DE RED
    // =========================================

    @Test
    fun `flujos iniciales exponen historial estado cerrado chats e internet`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val mensajeMock = mockk<MensajeEntity>(relaxed = true)
            val chatMock = mockk<ChatEntity>(relaxed = true)

            every { repository.obtenerHistorialChat("general") } returns flowOf(listOf(mensajeMock))
            every { repository.observarEstadoChat("general") } returns flowOf(true)
            every { repository.obtenerTodosLosChats() } returns flowOf(listOf(chatMock))

            val viewModel = ChatViewModel(repository, networkMonitor)

            val jobHistorial = launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.historial.collect {}
            }
            val jobCerrado = launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.chatCerrado.collect {}
            }
            val jobChats = launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.todosLosChats.collect {}
            }
            val jobOnline = launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.isOnline.collect {}
            }

            advanceUntilIdle()

            assertEquals("general", viewModel.chatIdActual.value)
            assertEquals(listOf(mensajeMock), viewModel.historial.value)
            assertTrue(viewModel.chatCerrado.value)
            assertEquals(listOf(chatMock), viewModel.todosLosChats.value)
            assertFalse(viewModel.isOnline.value)

            jobHistorial.cancel()
            jobCerrado.cancel()
            jobChats.cancel()
            jobOnline.cancel()
        }

    @Test
    fun `init cuando vuelve internet como usuario sincroniza y refresca chat actual`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ChatViewModel(repository, networkMonitor)
            viewModel.esAdmin = false
            advanceUntilIdle()

            isOnlineFlow.value = true
            advanceUntilIdle()

            coVerify(exactly = 1) { repository.sincronizarPendientes() }
            coVerify(exactly = 1) { repository.refrescarMensajesChat("general") }
            coVerify(exactly = 0) { repository.refrescarTodosLosChats() }
        }

    @Test
    fun `init cuando vuelve internet como admin refresca todos los chats`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ChatViewModel(repository, networkMonitor)
            viewModel.esAdmin = true
            advanceUntilIdle()

            isOnlineFlow.value = true
            advanceUntilIdle()

            coVerify(exactly = 1) { repository.sincronizarPendientes() }
            coVerify(exactly = 1) { repository.refrescarTodosLosChats() }
            coVerify(exactly = 1) { repository.refrescarMensajesChat("general") }
        }

    @Test
    fun `init con chatId en blanco no descarga mensajes al reconectar`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ChatViewModel(repository, networkMonitor)
            viewModel.seleccionarChat("")
            advanceUntilIdle()

            isOnlineFlow.value = true
            advanceUntilIdle()

            coVerify(exactly = 1) { repository.sincronizarPendientes() }
            // Solo la llamada de seleccionarChat(""), no una segunda por reconexión
            coVerify(exactly = 1) { repository.refrescarMensajesChat("") }
        }

    // =========================================
    // 2. ACCIONES DEL CHAT
    // =========================================

    @Test
    fun `seleccionarChat actualiza chatIdActual y descarga mensajes`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ChatViewModel(repository, networkMonitor)

            viewModel.seleccionarChat("ticket_42")
            advanceUntilIdle()

            assertEquals("ticket_42", viewModel.chatIdActual.value)
            coVerify(exactly = 1) { repository.refrescarMensajesChat("ticket_42") }
        }

    @Test
    fun `enviarMensaje como usuario guarda texto recortado con esDeUsuario true`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ChatViewModel(repository, networkMonitor)
            viewModel.esAdmin = false

            viewModel.enviarMensaje("  Hola, necesito ayuda  ")
            advanceUntilIdle()

            coVerify(exactly = 1) {
                repository.guardarMensajeLocal(
                    chatId = "general",
                    contenido = "Hola, necesito ayuda",
                    esDeUsuario = true
                )
            }
        }

    @Test
    fun `enviarMensaje como admin guarda con esDeUsuario false`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ChatViewModel(repository, networkMonitor)
            viewModel.esAdmin = true
            viewModel.seleccionarChat("ticket_10")

            viewModel.enviarMensaje("Consulta resuelta")
            advanceUntilIdle()

            coVerify(exactly = 1) {
                repository.guardarMensajeLocal(
                    chatId = "ticket_10",
                    contenido = "Consulta resuelta",
                    esDeUsuario = false
                )
            }
        }

    @Test
    fun `enviarMensaje vacio o solo espacios no guarda en el repositorio`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ChatViewModel(repository, networkMonitor)

            viewModel.enviarMensaje("")
            viewModel.enviarMensaje("     ")
            advanceUntilIdle()

            coVerify(exactly = 0) {
                repository.guardarMensajeLocal(any(), any(), any())
            }
        }

    @Test
    fun `cerrarChatAdministrador cierra el chat seleccionado`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ChatViewModel(repository, networkMonitor)
            viewModel.seleccionarChat("ticket_99")

            viewModel.cerrarChatAdministrador()
            advanceUntilIdle()

            coVerify(exactly = 1) { repository.cerrarChat("ticket_99") }
        }

    @Test
    fun `refrescarBandejaAdmin refrescarHistorialActual y marcarChatComoLeido delegan al repositorio`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ChatViewModel(repository, networkMonitor)
            viewModel.seleccionarChat("chat_abc")
            advanceUntilIdle()

            viewModel.refrescarBandejaAdmin()
            viewModel.refrescarHistorialActual()
            viewModel.marcarChatComoLeido("chat_abc")
            advanceUntilIdle()

            coVerify(exactly = 1) { repository.refrescarTodosLosChats() }
            coVerify(exactly = 2) { repository.refrescarMensajesChat("chat_abc") }
            coVerify(exactly = 1) { repository.marcarChatComoLeido("chat_abc") }
        }
}