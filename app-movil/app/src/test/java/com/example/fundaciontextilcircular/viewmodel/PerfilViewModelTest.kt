package com.example.fundaciontextilcircular.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fundaciontextilcircular.data.remote.dto.PerfilResponseDTO
import com.example.fundaciontextilcircular.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PerfilViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var authRepository: AuthRepository

    @Before
    fun configurar() {
        authRepository = mockk()
    }

    // =========================================
    // 1. CARGAR PERFIL
    // =========================================

    @Test
    fun `cargarPerfil exitoso guarda los datos en el estado`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val perfilMock = mockk<PerfilResponseDTO>(relaxed = true)

            coEvery {
                authRepository.obtenerPerfil("prueba@correo.cl")
            } returns Result.success(perfilMock)

            val viewModel = PerfilViewModel(authRepository)

            viewModel.cargarPerfil(
                correo = "prueba@correo.cl",
                isOnline = true
            )

            advanceUntilIdle()

            assertFalse(viewModel.uiState.cargando)
            assertNull(viewModel.uiState.error)
            assertEquals(perfilMock, viewModel.uiState.perfil)
            coVerify(exactly = 1) {
                authRepository.obtenerPerfil("prueba@correo.cl")
            }
        }

    @Test
    fun `cargarPerfil sin internet muestra error y no llama al servidor`() {
        val viewModel = PerfilViewModel(authRepository)

        viewModel.cargarPerfil(
            correo = "prueba@correo.cl",
            isOnline = false
        )

        assertFalse(viewModel.uiState.cargando)
        assertNotNull(viewModel.uiState.error)
        assertNull(viewModel.uiState.perfil)
        coVerify(exactly = 0) {
            authRepository.obtenerPerfil(any())
        }
    }

    @Test
    fun `cargarPerfil muestra error cuando falla el servidor`() =
        runTest(mainDispatcherRule.testDispatcher) {
            coEvery {
                authRepository.obtenerPerfil("prueba@correo.cl")
            } returns Result.failure(Exception("No se pudo cargar el perfil"))

            val viewModel = PerfilViewModel(authRepository)

            viewModel.cargarPerfil(
                correo = "prueba@correo.cl",
                isOnline = true
            )

            advanceUntilIdle()

            assertFalse(viewModel.uiState.cargando)
            assertEquals("No se pudo cargar el perfil", viewModel.uiState.error)
            assertNull(viewModel.uiState.perfil)
        }

    // =========================================
    // 2. VALIDACIONES CAMBIAR CONTRASEÑA
    // =========================================

    @Test
    fun `cambiarContrasena con campos vacios muestra error`() {
        val viewModel = PerfilViewModel(authRepository)

        viewModel.cambiarContrasena(
            correo = "prueba@correo.cl",
            contrasenaActual = "",
            nuevaContrasena = "",
            confirmarContrasena = "",
            isOnline = true
        )

        assertNotNull(viewModel.uiState.error)
        coVerify(exactly = 0) {
            authRepository.cambiarContrasena(any(), any(), any())
        }
    }

    @Test
    fun `cambiarContrasena corta menor a 7 caracteres muestra error`() {
        val viewModel = PerfilViewModel(authRepository)

        viewModel.cambiarContrasena(
            correo = "prueba@correo.cl",
            contrasenaActual = "Clave123!",
            nuevaContrasena = "Ab1!",
            confirmarContrasena = "Ab1!",
            isOnline = true
        )

        assertNotNull(viewModel.uiState.error)
        coVerify(exactly = 0) {
            authRepository.cambiarContrasena(any(), any(), any())
        }
    }

    @Test
    fun `cambiarContrasena sin letra numero o simbolo muestra error`() {
        val viewModel = PerfilViewModel(authRepository)

        // Sin letra
        viewModel.cambiarContrasena(
            correo = "prueba@correo.cl",
            contrasenaActual = "Clave123!",
            nuevaContrasena = "1234567!",
            confirmarContrasena = "1234567!",
            isOnline = true
        )
        assertNotNull(viewModel.uiState.error)

        // Sin número
        viewModel.cambiarContrasena(
            correo = "prueba@correo.cl",
            contrasenaActual = "Clave123!",
            nuevaContrasena = "ClaveSinNum!",
            confirmarContrasena = "ClaveSinNum!",
            isOnline = true
        )
        assertNotNull(viewModel.uiState.error)

        // Sin carácter especial
        viewModel.cambiarContrasena(
            correo = "prueba@correo.cl",
            contrasenaActual = "Clave123!",
            nuevaContrasena = "Clave1234",
            confirmarContrasena = "Clave1234",
            isOnline = true
        )
        assertNotNull(viewModel.uiState.error)

        coVerify(exactly = 0) {
            authRepository.cambiarContrasena(any(), any(), any())
        }
    }

    @Test
    fun `cambiarContrasena cuando no coinciden muestra error`() {
        val viewModel = PerfilViewModel(authRepository)

        viewModel.cambiarContrasena(
            correo = "prueba@correo.cl",
            contrasenaActual = "Clave123!",
            nuevaContrasena = "NuevaClave1!",
            confirmarContrasena = "OtraClave1!",
            isOnline = true
        )

        assertNotNull(viewModel.uiState.error)
        coVerify(exactly = 0) {
            authRepository.cambiarContrasena(any(), any(), any())
        }
    }

    @Test
    fun `cambiarContrasena igual a la actual muestra error`() {
        val viewModel = PerfilViewModel(authRepository)

        viewModel.cambiarContrasena(
            correo = "prueba@correo.cl",
            contrasenaActual = "Clave123!",
            nuevaContrasena = "Clave123!",
            confirmarContrasena = "Clave123!",
            isOnline = true
        )

        assertNotNull(viewModel.uiState.error)
        coVerify(exactly = 0) {
            authRepository.cambiarContrasena(any(), any(), any())
        }
    }

    @Test
    fun `cambiarContrasena sin internet muestra error`() {
        val viewModel = PerfilViewModel(authRepository)

        viewModel.cambiarContrasena(
            correo = "prueba@correo.cl",
            contrasenaActual = "Clave123!",
            nuevaContrasena = "NuevaClave1!",
            confirmarContrasena = "NuevaClave1!",
            isOnline = false
        )

        assertNotNull(viewModel.uiState.error)
        coVerify(exactly = 0) {
            authRepository.cambiarContrasena(any(), any(), any())
        }
    }

    // =========================================
    // 3. CAMBIAR CONTRASEÑA RESPUESTA SERVIDOR
    // =========================================

    @Test
    fun `cambiarContrasena exitoso actualiza mensaje en estado`() =
        runTest(mainDispatcherRule.testDispatcher) {
            coEvery {
                authRepository.cambiarContrasena(
                    "prueba@correo.cl",
                    "Clave123!",
                    "NuevaClave1!"
                )
            } returns Result.success("Contraseña actualizada correctamente")

            val viewModel = PerfilViewModel(authRepository)

            viewModel.cambiarContrasena(
                correo = "prueba@correo.cl",
                contrasenaActual = "Clave123!",
                nuevaContrasena = "NuevaClave1!",
                confirmarContrasena = "NuevaClave1!",
                isOnline = true
            )

            advanceUntilIdle()

            assertFalse(viewModel.uiState.cambiandoContrasena)
            assertNull(viewModel.uiState.error)
            assertEquals("Contraseña actualizada correctamente", viewModel.uiState.mensaje)
        }

    @Test
    fun `cambiarContrasena con error del servidor muestra error`() =
        runTest(mainDispatcherRule.testDispatcher) {
            coEvery {
                authRepository.cambiarContrasena(
                    "prueba@correo.cl",
                    "Clave123!",
                    "NuevaClave1!"
                )
            } returns Result.failure(Exception("La contraseña actual es incorrecta"))

            val viewModel = PerfilViewModel(authRepository)

            viewModel.cambiarContrasena(
                correo = "prueba@correo.cl",
                contrasenaActual = "Clave123!",
                nuevaContrasena = "NuevaClave1!",
                confirmarContrasena = "NuevaClave1!",
                isOnline = true
            )

            advanceUntilIdle()

            assertFalse(viewModel.uiState.cambiandoContrasena)
            assertNull(viewModel.uiState.mensaje)
            assertEquals("La contraseña actual es incorrecta", viewModel.uiState.error)
        }

    // =========================================
    // 4. LIMPIAR MENSAJES Y FACTORY
    // =========================================

    @Test
    fun `limpiarMensajes borra error y mensaje exitoso`() {
        val viewModel = PerfilViewModel(authRepository)

        viewModel.cargarPerfil(correo = "prueba@correo.cl", isOnline = false)
        assertNotNull(viewModel.uiState.error)

        viewModel.limpiarMensajes()

        assertNull(viewModel.uiState.error)
        assertNull(viewModel.uiState.mensaje)
    }

    @Test
    fun `PerfilViewModelFactory crea instancia correctamente`() {
        val factory = PerfilViewModelFactory(authRepository)
        val creado = factory.create(PerfilViewModel::class.java)

        assertTrue(creado is PerfilViewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `PerfilViewModelFactory lanza excepcion para ViewModel desconocido`() {
        class ViewModelFalso : ViewModel()
        val factory = PerfilViewModelFactory(authRepository)

        factory.create(ViewModelFalso::class.java)
    }
}