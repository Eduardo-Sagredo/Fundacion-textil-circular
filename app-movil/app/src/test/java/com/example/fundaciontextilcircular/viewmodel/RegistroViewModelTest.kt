package com.example.fundaciontextilcircular.viewmodel

import com.example.fundaciontextilcircular.data.remote.dto.RegistroResponseDTO
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
class RegistroViewModelTest {

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
    // 1. FORMULARIO CORRECTO
    // =========================================

    @Test
    fun `formulario correcto pasa todas las validaciones`() {

        val viewModel =
            RegistroViewModel(
                authRepository
            )


        val resultado =
            viewModel.validarFormulario(

                nombre =
                    "Usuario Prueba",

                rut =
                    "11111111-1",

                correo =
                    "usuario@correo.cl",

                contrasena =
                    "Clave123!",

                confirmarContrasena =
                    "Clave123!",

                nombreEmprendimiento =
                    "Textil Prueba",

                rubro =
                    "Ropa",

                region =
                    "Valparaiso",

                comuna =
                    "Papudo",

                edad =
                    "30",

                genero =
                    "Masculino"
            )


        assertTrue(
            resultado
        )


        assertNull(
            viewModel.errorNombre
        )

        assertNull(
            viewModel.errorRut
        )

        assertNull(
            viewModel.errorCorreo
        )

        assertNull(
            viewModel.errorContrasena
        )

        assertNull(
            viewModel.errorConfirmarContrasena
        )

        assertNull(
            viewModel.errorNombreEmprendimiento
        )

        assertNull(
            viewModel.errorRubro
        )

        assertNull(
            viewModel.errorRegion
        )

        assertNull(
            viewModel.errorComuna
        )

        assertNull(
            viewModel.errorEdad
        )

        assertNull(
            viewModel.errorGenero
        )
    }


    // =========================================
    // 2. FORMULARIO VACÍO
    // =========================================

    @Test
    fun `formulario vacio muestra errores`() {

        val viewModel =
            RegistroViewModel(
                authRepository
            )


        val resultado =
            viewModel.validarFormulario(

                nombre = "",

                rut = "",

                correo = "",

                contrasena = "",

                confirmarContrasena = "",

                nombreEmprendimiento = "",

                rubro = "",

                region = "",

                comuna = "",

                edad = "",

                genero = ""
            )


        assertFalse(
            resultado
        )


        assertNotNull(
            viewModel.errorNombre
        )

        assertNotNull(
            viewModel.errorRut
        )

        assertNotNull(
            viewModel.errorCorreo
        )

        assertNotNull(
            viewModel.errorContrasena
        )

        assertNotNull(
            viewModel.errorConfirmarContrasena
        )

        assertNotNull(
            viewModel.errorNombreEmprendimiento
        )

        assertNotNull(
            viewModel.errorRubro
        )

        assertNotNull(
            viewModel.errorRegion
        )

        assertNotNull(
            viewModel.errorComuna
        )

        assertNotNull(
            viewModel.errorEdad
        )

        assertNotNull(
            viewModel.errorGenero
        )
    }


    // =========================================
    // 3. RUT INVÁLIDO
    // =========================================

    @Test
    fun `rut invalido no permite registro`() {

        val viewModel =
            RegistroViewModel(
                authRepository
            )


        val resultado =
            viewModel.validarFormulario(

                nombre =
                    "Usuario Prueba",

                rut =
                    "12345678-9",

                correo =
                    "usuario@correo.cl",

                contrasena =
                    "Clave123!",

                confirmarContrasena =
                    "Clave123!",

                nombreEmprendimiento =
                    "Textil Prueba",

                rubro =
                    "Ropa",

                region =
                    "Valparaiso",

                comuna =
                    "Papudo",

                edad =
                    "30",

                genero =
                    "Masculino"
            )


        assertFalse(
            resultado
        )


        assertEquals(
            "El RUT ingresado no es válido",
            viewModel.errorRut
        )
    }


    // =========================================
    // 4. CONTRASEÑAS DIFERENTES
    // =========================================

    @Test
    fun `contrasenas diferentes no permiten registro`() {

        val viewModel =
            RegistroViewModel(
                authRepository
            )


        val resultado =
            viewModel.validarFormulario(

                nombre =
                    "Usuario Prueba",

                rut =
                    "11111111-1",

                correo =
                    "usuario@correo.cl",

                contrasena =
                    "Clave123!",

                confirmarContrasena =
                    "Otra123!",

                nombreEmprendimiento =
                    "Textil Prueba",

                rubro =
                    "Ropa",

                region =
                    "Valparaiso",

                comuna =
                    "Papudo",

                edad =
                    "30",

                genero =
                    "Masculino"
            )


        assertFalse(
            resultado
        )


        assertEquals(
            "Las contraseñas no coinciden",
            viewModel.errorConfirmarContrasena
        )
    }


    // =========================================
    // 5. SIN INTERNET
    // =========================================

    @Test
    fun `registro sin internet muestra error`() {

        val viewModel =
            RegistroViewModel(
                authRepository
            )


        viewModel.registrar(

            nombre =
                "Usuario Prueba",

            rut =
                "11111111-1",

            correo =
                "usuario@correo.cl",

            contrasena =
                "Clave123!",

            nombreEmprendimiento =
                "Textil Prueba",

            rubro =
                "Ropa",

            region =
                "Valparaiso",

            comuna =
                "Papudo",

            edad =
                "30",

            genero =
                "Masculino",

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


        assertNull(
            viewModel.uiState.resultado
        )


        coVerify(
            exactly = 0
        ) {

            authRepository.registrar(
                any()
            )
        }
    }


    // =========================================
    // 6. REGISTRO EXITOSO
    // =========================================

    @Test
    fun `registro exitoso guarda resultado del backend`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            val respuesta =
                RegistroResponseDTO(

                    mensaje =
                        "Cuenta creada correctamente",

                    id =
                        41L,

                    correo =
                        "usuario@correo.cl",

                    estado =
                        "PENDIENTE"
                )


            coEvery {

                authRepository.registrar(
                    any()
                )

            } returns
                    Result.success(
                        respuesta
                    )


            val viewModel =
                RegistroViewModel(
                    authRepository
                )


            viewModel.registrar(

                nombre =
                    "Usuario Prueba",

                rut =
                    "11111111-1",

                correo =
                    "USUARIO@CORREO.CL",

                contrasena =
                    "Clave123!",

                nombreEmprendimiento =
                    "Textil Prueba",

                rubro =
                    "Ropa",

                region =
                    "Valparaiso",

                comuna =
                    "Papudo",

                edad =
                    "30",

                genero =
                    "Masculino",

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
                "PENDIENTE",
                viewModel.uiState
                    .resultado
                    ?.estado
            )


            coVerify(
                exactly = 1
            ) {

                authRepository.registrar(

                    match {

                        it.correo ==
                                "usuario@correo.cl" &&

                                it.rut ==
                                "11111111-1" &&

                                it.nombre ==
                                "Usuario Prueba" &&

                                it.edad ==
                                30
                    }
                )
            }
        }


    // =========================================
    // 7. ERROR DEL BACKEND
    // =========================================

    @Test
    fun `error del backend se muestra en registro`() =
        runTest(
            mainDispatcherRule.testDispatcher
        ) {

            coEvery {

                authRepository.registrar(
                    any()
                )

            } returns
                    Result.failure(

                        Exception(
                            "El correo ya está registrado"
                        )
                    )


            val viewModel =
                RegistroViewModel(
                    authRepository
                )


            viewModel.registrar(

                nombre =
                    "Usuario Prueba",

                rut =
                    "11111111-1",

                correo =
                    "usuario@correo.cl",

                contrasena =
                    "Clave123!",

                nombreEmprendimiento =
                    "Textil Prueba",

                rubro =
                    "Ropa",

                region =
                    "Valparaiso",

                comuna =
                    "Papudo",

                edad =
                    "30",

                genero =
                    "Masculino",

                isOnline =
                    true
            )


            advanceUntilIdle()


            assertFalse(
                viewModel.uiState.cargando
            )


            assertEquals(
                "El correo ya está registrado",
                viewModel.uiState.error
            )


            assertNull(
                viewModel.uiState.resultado
            )
        }
}