package com.example.fundaciontextilcircular.viewmodel

import com.example.fundaciontextilcircular.data.remote.dto.AdminSolicitudDTO
import com.example.fundaciontextilcircular.data.repository.AdminRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class AdminViewModelTest {

    @get:Rule
    val mainDispatcherRule =
        MainDispatcherRule()


    private lateinit var adminRepository:
            AdminRepository


    private lateinit var solicitud:
            AdminSolicitudDTO


    @Before
    fun configurar() {

        adminRepository =
            mockk()


        solicitud =
            AdminSolicitudDTO(

                id = 41L,
                rut = "111111111",
                nombre = "Usuario Android",
                correo = "android@correo.cl",
                nombreEmprendimiento = "Textil Android",
                rubro = "Ropa",
                region = "California",
                comuna = "Mountain View",
                edad = 30,
                genero = "Masculino",
                rol = "EMPRENDEDORA",
                estado = "PENDIENTE",
                motivoRechazo = null
            )
    }


    @Test
    fun `carga solicitudes pendientes correctamente`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            coEvery {

                adminRepository
                    .obtenerSolicitudes()

            } returns
                    Result.success(
                        listOf(
                            solicitud
                        )
                    )


            val viewModel =
                AdminViewModel(
                    adminRepository
                )


            viewModel.cargarSolicitudes(
                isOnline = true
            )


            advanceUntilIdle()


            assertFalse(
                viewModel.uiState.cargando
            )


            assertNull(
                viewModel.uiState.error
            )


            assertEquals(
                1,
                viewModel.uiState
                    .solicitudes
                    .size
            )


            assertEquals(
                "android@correo.cl",
                viewModel.uiState
                    .solicitudes
                    .first()
                    .correo
            )
        }


    @Test
    fun `sin internet no carga solicitudes`() {

        val viewModel =
            AdminViewModel(
                adminRepository
            )


        viewModel.cargarSolicitudes(
            isOnline = false
        )


        assertEquals(
            "No tienes conexión a internet",
            viewModel.uiState.error
        )


        coVerify(
            exactly = 0
        ) {

            adminRepository
                .obtenerSolicitudes()
        }
    }


    @Test
    fun `aprobar solicitud elimina usuario de pendientes`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            coEvery {

                adminRepository
                    .obtenerSolicitudes()

            } returns
                    Result.success(
                        listOf(
                            solicitud
                        )
                    )


            coEvery {

                adminRepository
                    .aprobarSolicitud(
                        41L
                    )

            } returns
                    Result.success(

                        solicitud.copy(
                            estado =
                                "APROBADA"
                        )
                    )


            val viewModel =
                AdminViewModel(
                    adminRepository
                )


            viewModel.cargarSolicitudes(
                true
            )


            advanceUntilIdle()


            viewModel.aprobarSolicitud(

                id =
                    41L,

                isOnline =
                    true
            )


            advanceUntilIdle()


            assertTrue(
                viewModel.uiState
                    .solicitudes
                    .isEmpty()
            )


            assertEquals(
                "Solicitud aprobada correctamente",
                viewModel.uiState.mensaje
            )
        }


    @Test
    fun `aprobar sin internet muestra error`() {

        val viewModel =
            AdminViewModel(
                adminRepository
            )


        viewModel.aprobarSolicitud(

            id =
                41L,

            isOnline =
                false
        )


        assertEquals(
            "No tienes conexión a internet",
            viewModel.uiState.error
        )


        coVerify(
            exactly = 0
        ) {

            adminRepository
                .aprobarSolicitud(
                    any()
                )
        }
    }


    @Test
    fun `error del backend al aprobar se muestra`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            coEvery {

                adminRepository
                    .aprobarSolicitud(
                        41L
                    )

            } returns
                    Result.failure(

                        Exception(
                            "No se pudo aprobar la solicitud"
                        )
                    )


            val viewModel =
                AdminViewModel(
                    adminRepository
                )


            viewModel.aprobarSolicitud(

                id =
                    41L,

                isOnline =
                    true
            )


            advanceUntilIdle()


            assertEquals(
                "No se pudo aprobar la solicitud",
                viewModel.uiState.error
            )
        }


    @Test
    fun `rechazar solicitud elimina usuario de pendientes`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            coEvery {

                adminRepository
                    .obtenerSolicitudes()

            } returns
                    Result.success(
                        listOf(
                            solicitud
                        )
                    )


            coEvery {

                adminRepository
                    .rechazarSolicitud(

                        41L,

                        "Datos no verificados"
                    )

            } returns
                    Result.success(

                        solicitud.copy(

                            estado =
                                "RECHAZADA",

                            motivoRechazo =
                                "Datos no verificados"
                        )
                    )


            val viewModel =
                AdminViewModel(
                    adminRepository
                )


            viewModel.cargarSolicitudes(
                true
            )


            advanceUntilIdle()


            viewModel.rechazarSolicitud(

                id =
                    41L,

                motivo =
                    "Datos no verificados",

                isOnline =
                    true
            )


            advanceUntilIdle()


            assertTrue(
                viewModel.uiState
                    .solicitudes
                    .isEmpty()
            )


            assertEquals(
                "Solicitud rechazada correctamente",
                viewModel.uiState.mensaje
            )
        }


    @Test
    fun `rechazo con motivo vacio muestra error`() {

        val viewModel =
            AdminViewModel(
                adminRepository
            )


        viewModel.rechazarSolicitud(

            id =
                41L,

            motivo =
                "",

            isOnline =
                true
        )


        assertEquals(
            "Debes indicar el motivo del rechazo",
            viewModel.uiState.error
        )
    }


    @Test
    fun `rechazar motivo mayor a 500 caracteres muestra error`() {

        val viewModel =
            AdminViewModel(
                adminRepository
            )


        val motivoLargo =
            "A".repeat(
                501
            )


        viewModel.rechazarSolicitud(

            id =
                41L,

            motivo =
                motivoLargo,

            isOnline =
                true
        )


        assertEquals(
            "El motivo no puede superar los 500 caracteres",
            viewModel.uiState.error
        )


        coVerify(
            exactly = 0
        ) {

            adminRepository
                .rechazarSolicitud(
                    any(),
                    any()
                )
        }
    }


    @Test
    fun `rechazar sin internet muestra error`() {

        val viewModel =
            AdminViewModel(
                adminRepository
            )


        viewModel.rechazarSolicitud(

            id =
                41L,

            motivo =
                "Datos no verificados",

            isOnline =
                false
        )


        assertEquals(
            "No tienes conexión a internet",
            viewModel.uiState.error
        )
    }


    @Test
    fun `error del backend al rechazar se muestra`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            coEvery {

                adminRepository
                    .rechazarSolicitud(

                        41L,

                        "Datos no verificados"
                    )

            } returns
                    Result.failure(

                        Exception(
                            "No se pudo rechazar la solicitud"
                        )
                    )


            val viewModel =
                AdminViewModel(
                    adminRepository
                )


            viewModel.rechazarSolicitud(

                id =
                    41L,

                motivo =
                    "Datos no verificados",

                isOnline =
                    true
            )


            advanceUntilIdle()


            assertEquals(
                "No se pudo rechazar la solicitud",
                viewModel.uiState.error
            )
        }


    @Test
    fun `error del servidor al cargar solicitudes se muestra`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            coEvery {

                adminRepository
                    .obtenerSolicitudes()

            } returns
                    Result.failure(

                        Exception(
                            "No se pudo conectar con el servidor"
                        )
                    )


            val viewModel =
                AdminViewModel(
                    adminRepository
                )


            viewModel.cargarSolicitudes(
                true
            )


            advanceUntilIdle()


            assertEquals(
                "No se pudo conectar con el servidor",
                viewModel.uiState.error
            )
        }


    // =========================================
    // LIMPIAR ERROR
    // =========================================

    @Test
    fun `limpiar error elimina mensaje de error`() {

        val viewModel =
            AdminViewModel(
                adminRepository
            )


        viewModel.cargarSolicitudes(
            isOnline =
                false
        )


        assertEquals(
            "No tienes conexión a internet",
            viewModel.uiState.error
        )


        viewModel.limpiarError()


        assertNull(
            viewModel.uiState.error
        )
    }


    // =========================================
    // LIMPIAR MENSAJE
    // =========================================

    @Test
    fun `limpiar mensaje elimina mensaje exitoso`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            coEvery {

                adminRepository
                    .aprobarSolicitud(
                        41L
                    )

            } returns
                    Result.success(

                        solicitud.copy(
                            estado =
                                "APROBADA"
                        )
                    )


            val viewModel =
                AdminViewModel(
                    adminRepository
                )


            viewModel.aprobarSolicitud(

                id =
                    41L,

                isOnline =
                    true
            )


            advanceUntilIdle()


            assertEquals(
                "Solicitud aprobada correctamente",
                viewModel.uiState.mensaje
            )


            viewModel.limpiarMensaje()


            assertNull(
                viewModel.uiState.mensaje
            )
        }
}