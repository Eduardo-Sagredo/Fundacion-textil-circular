package com.example.fundaciontextilcircular.viewmodel

import com.example.fundaciontextilcircular.data.remote.dto.LoginResponseDTO
import com.example.fundaciontextilcircular.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule =
        MainDispatcherRule()


    private lateinit var authRepository:
            AuthRepository


    @Before
    fun configurar() {

        authRepository =
            mockk()
    }


    // =========================================
    // 1. VALIDACIÓN CAMPOS VACÍOS
    // =========================================

    @Test
    fun `no permite login con campos vacios`() {

        val viewModel =
            LoginViewModel(
                authRepository
            )


        viewModel.iniciarSesion(

            correo = "",

            contrasena = "",

            isOnline = true
        )


        assertEquals(
            "Debes ingresar correo y contraseña",
            viewModel.uiState.error
        )


        assertFalse(
            viewModel.uiState.cargando
        )


        assertNull(
            viewModel.uiState.resultado
        )


        coVerify(
            exactly = 0
        ) {

            authRepository.login(
                any(),
                any()
            )
        }
    }


    // =========================================
    // 2. SIN INTERNET
    // =========================================

    @Test
    fun `no permite login cuando no hay internet`() {

        val viewModel =
            LoginViewModel(
                authRepository
            )


        viewModel.iniciarSesion(

            correo =
                "prueba@correo.cl",

            contrasena =
                "Clave123!",

            isOnline =
                false
        )


        assertEquals(
            "No tienes conexión a internet",
            viewModel.uiState.error
        )


        assertFalse(
            viewModel.uiState.cargando
        )


        assertNull(
            viewModel.uiState.resultado
        )


        coVerify(
            exactly = 0
        ) {

            authRepository.login(
                any(),
                any()
            )
        }
    }


    // =========================================
    // 3. LOGIN EXITOSO
    // =========================================

    @Test
    fun `login exitoso devuelve usuario aprobado`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            val respuesta =
                LoginResponseDTO(

                    exito =
                        true,

                    mensaje =
                        "Inicio de sesión correcto",

                    id =
                        1L,

                    nombre =
                        "Usuario Prueba",

                    correo =
                        "prueba@correo.cl",

                    rol =
                        "EMPRENDEDORA",

                    estado =
                        "APROBADA",

                    motivoRechazo =
                        null,

                    puedeIngresar =
                        true
                )


            coEvery {

                authRepository.login(

                    "prueba@correo.cl",

                    "Clave123!"
                )

            } returns
                    Result.success(
                        respuesta
                    )


            val viewModel =
                LoginViewModel(
                    authRepository
                )


            viewModel.iniciarSesion(

                correo =
                    "prueba@correo.cl",

                contrasena =
                    "Clave123!",

                isOnline =
                    true
            )


            advanceUntilIdle()


            assertFalse(
                viewModel.uiState.cargando
            )


            assertNull(
                viewModel.uiState.error
            )


            assertEquals(
                respuesta,
                viewModel.uiState.resultado
            )


            assertEquals(
                "APROBADA",
                viewModel.uiState
                    .resultado
                    ?.estado
            )


            assertEquals(
                true,
                viewModel.uiState
                    .resultado
                    ?.puedeIngresar
            )


            coVerify(
                exactly = 1
            ) {

                authRepository.login(

                    "prueba@correo.cl",

                    "Clave123!"
                )
            }
        }


    // =========================================
    // 4. ERROR DEL SERVIDOR
    // =========================================

    @Test
    fun `muestra error cuando falla el servidor`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            coEvery {

                authRepository.login(

                    "prueba@correo.cl",

                    "Clave123!"
                )

            } returns
                    Result.failure(

                        Exception(
                            "No se pudo conectar con el servidor"
                        )
                    )


            val viewModel =
                LoginViewModel(
                    authRepository
                )


            viewModel.iniciarSesion(

                correo =
                    "prueba@correo.cl",

                contrasena =
                    "Clave123!",

                isOnline =
                    true
            )


            advanceUntilIdle()


            assertFalse(
                viewModel.uiState.cargando
            )


            assertEquals(
                "No se pudo conectar con el servidor",
                viewModel.uiState.error
            )


            assertNull(
                viewModel.uiState.resultado
            )
        }


    // =========================================
    // 5. NORMALIZA EL CORREO
    // =========================================

    @Test
    fun `login elimina espacios y convierte correo a minusculas`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            val respuesta =
                LoginResponseDTO(

                    exito =
                        true,

                    mensaje =
                        "Inicio de sesión correcto",

                    id =
                        1L,

                    nombre =
                        "Usuario Prueba",

                    correo =
                        "prueba@correo.cl",

                    rol =
                        "EMPRENDEDORA",

                    estado =
                        "APROBADA",

                    motivoRechazo =
                        null,

                    puedeIngresar =
                        true
                )


            coEvery {

                authRepository.login(

                    "prueba@correo.cl",

                    "Clave123!"
                )

            } returns
                    Result.success(
                        respuesta
                    )


            val viewModel =
                LoginViewModel(
                    authRepository
                )


            viewModel.iniciarSesion(

                correo =
                    "  PRUEBA@CORREO.CL  ",

                contrasena =
                    "Clave123!",

                isOnline =
                    true
            )


            advanceUntilIdle()


            coVerify(
                exactly = 1
            ) {

                authRepository.login(

                    "prueba@correo.cl",

                    "Clave123!"
                )
            }


            assertNull(
                viewModel.uiState.error
            )


            assertEquals(
                respuesta,
                viewModel.uiState.resultado
            )
        }


    // =========================================
    // 6. LIMPIAR RESULTADO
    // =========================================

    @Test
    fun `limpiar resultado elimina usuario del estado`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            val respuesta =
                LoginResponseDTO(

                    exito =
                        true,

                    mensaje =
                        "Inicio de sesión correcto",

                    id =
                        1L,

                    nombre =
                        "Usuario Prueba",

                    correo =
                        "prueba@correo.cl",

                    rol =
                        "EMPRENDEDORA",

                    estado =
                        "APROBADA",

                    motivoRechazo =
                        null,

                    puedeIngresar =
                        true
                )


            coEvery {

                authRepository.login(
                    "prueba@correo.cl",
                    "Clave123!"
                )

            } returns
                    Result.success(
                        respuesta
                    )


            val viewModel =
                LoginViewModel(
                    authRepository
                )


            viewModel.iniciarSesion(

                correo =
                    "prueba@correo.cl",

                contrasena =
                    "Clave123!",

                isOnline =
                    true
            )


            advanceUntilIdle()


            assertEquals(
                respuesta,
                viewModel.uiState.resultado
            )


            viewModel.limpiarResultado()


            assertNull(
                viewModel.uiState.resultado
            )
        }


    // =========================================
    // 7. LIMPIAR ERROR
    // =========================================

    @Test
    fun `limpiar error elimina mensaje de error`() {

        val viewModel =
            LoginViewModel(
                authRepository
            )


        viewModel.iniciarSesion(

            correo =
                "",

            contrasena =
                "",

            isOnline =
                true
        )


        assertEquals(
            "Debes ingresar correo y contraseña",
            viewModel.uiState.error
        )


        viewModel.limpiarError()


        assertNull(
            viewModel.uiState.error
        )
    }
}


// =========================================
// REGLA PARA DISPATCHERS MAIN
// =========================================

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(

    val testDispatcher:
    TestDispatcher =
        StandardTestDispatcher()

) : TestWatcher() {

    override fun starting(
        description: Description
    ) {

        Dispatchers.setMain(
            testDispatcher
        )
    }


    override fun finished(
        description: Description
    ) {

        Dispatchers.resetMain()
    }
}