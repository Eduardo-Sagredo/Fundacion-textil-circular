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

                id =
                    41L,

                rut =
                    "111111111",

                nombre =
                    "Usuario Android",

                correo =
                    "android@correo.cl",

                nombreEmprendimiento =
                    "Textil Android",

                rubro =
                    "Ropa",

                region =
                    "California",

                comuna =
                    "Mountain View",

                edad =
                    30,

                genero =
                    "Masculino",

                rol =
                    "EMPRENDEDORA",

                estado =
                    "PENDIENTE",

                motivoRechazo =
                    null
            )
    }


    // =========================================
    // 1. CARGAR SOLICITUDES
    // =========================================

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


            coVerify(
                exactly = 1
            ) {

                adminRepository
                    .obtenerSolicitudes()
            }
        }


    // =========================================
    // 2. CARGAR SIN INTERNET
    // =========================================

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


        assertTrue(
            viewModel.uiState
                .solicitudes
                .isEmpty()
        )


        coVerify(
            exactly = 0
        ) {

            adminRepository
                .obtenerSolicitudes()
        }
    }


    // =========================================
    // 3. APROBAR SOLICITUD
    // =========================================

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


            val solicitudAprobada =
                solicitud.copy(
                    estado =
                        "APROBADA"
                )


            coEvery {

                adminRepository
                    .aprobarSolicitud(
                        41L
                    )

            } returns
                    Result.success(
                        solicitudAprobada
                    )


            val viewModel =
                AdminViewModel(
                    adminRepository
                )


            viewModel.cargarSolicitudes(
                isOnline = true
            )


            advanceUntilIdle()


            assertEquals(
                1,
                viewModel.uiState
                    .solicitudes
                    .size
            )


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


            assertNull(
                viewModel.uiState.error
            )


            coVerify(
                exactly = 1
            ) {

                adminRepository
                    .aprobarSolicitud(
                        41L
                    )
            }
        }


    // =========================================
    // 4. APROBAR SIN INTERNET
    // =========================================

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


        assertFalse(
            viewModel.uiState.cargando
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


    // =========================================
    // 5. ERROR DEL BACKEND AL APROBAR
    // =========================================

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


            assertFalse(
                viewModel.uiState.cargando
            )


            assertEquals(
                "No se pudo aprobar la solicitud",
                viewModel.uiState.error
            )


            assertNull(
                viewModel.uiState.mensaje
            )


            coVerify(
                exactly = 1
            ) {

                adminRepository
                    .aprobarSolicitud(
                        41L
                    )
            }
        }


    // =========================================
    // 6. RECHAZAR SOLICITUD
    // =========================================

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


            val solicitudRechazada =
                solicitud.copy(

                    estado =
                        "RECHAZADA",

                    motivoRechazo =
                        "Datos no verificados"
                )


            coEvery {

                adminRepository
                    .rechazarSolicitud(

                        41L,

                        "Datos no verificados"
                    )

            } returns
                    Result.success(
                        solicitudRechazada
                    )


            val viewModel =
                AdminViewModel(
                    adminRepository
                )


            viewModel.cargarSolicitudes(
                isOnline = true
            )


            advanceUntilIdle()


            assertEquals(
                1,
                viewModel.uiState
                    .solicitudes
                    .size
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


            assertTrue(
                viewModel.uiState
                    .solicitudes
                    .isEmpty()
            )


            assertEquals(
                "Solicitud rechazada correctamente",
                viewModel.uiState.mensaje
            )


            assertNull(
                viewModel.uiState.error
            )


            coVerify(
                exactly = 1
            ) {

                adminRepository
                    .rechazarSolicitud(

                        41L,

                        "Datos no verificados"
                    )
            }
        }


    // =========================================
    // 7. MOTIVO VACÍO
    // =========================================

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


    // =========================================
    // 8. RECHAZAR SIN INTERNET
    // =========================================

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


        assertFalse(
            viewModel.uiState.cargando
        )


        assertEquals(
            "No tienes conexión a internet",
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


    // =========================================
    // 9. ERROR DEL BACKEND AL RECHAZAR
    // =========================================

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


            assertFalse(
                viewModel.uiState.cargando
            )


            assertEquals(
                "No se pudo rechazar la solicitud",
                viewModel.uiState.error
            )


            assertNull(
                viewModel.uiState.mensaje
            )


            coVerify(
                exactly = 1
            ) {

                adminRepository
                    .rechazarSolicitud(

                        41L,

                        "Datos no verificados"
                    )
            }
        }


    // =========================================
    // 10. ERROR AL CARGAR SOLICITUDES
    // =========================================

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
                isOnline = true
            )


            advanceUntilIdle()


            assertFalse(
                viewModel.uiState.cargando
            )


            assertEquals(
                "No se pudo conectar con el servidor",
                viewModel.uiState.error
            )


            assertTrue(
                viewModel.uiState
                    .solicitudes
                    .isEmpty()
            )
        }
}