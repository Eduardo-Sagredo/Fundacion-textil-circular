package com.example.fundaciontextilcircular.ui.viewmodel

import com.example.fundaciontextilcircular.data.local.entities.ProductoEntity
import com.example.fundaciontextilcircular.data.repository.ProductoRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor
import com.example.fundaciontextilcircular.viewmodel.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
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
class ProductoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: ProductoRepository
    private lateinit var networkMonitor: NetworkMonitor
    private lateinit var isOnlineFlow: MutableStateFlow<Boolean>

    @Before
    fun configurar() {
        repository = mockk(relaxed = true)
        networkMonitor = mockk(relaxed = true)
        isOnlineFlow = MutableStateFlow(false)

        every { networkMonitor.isOnline } returns isOnlineFlow
        every { repository.obtenerTodos() } returns flowOf(emptyList())
    }

    // =========================================
    // 1. FLUJOS Y CONECTIVIDAD
    // =========================================

    @Test
    fun `productos e isOnline emiten valores del repositorio y monitor de red`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val productoMock = mockk<ProductoEntity>(relaxed = true)
            every { repository.obtenerTodos() } returns flowOf(listOf(productoMock))

            val viewModel = ProductoViewModel(repository, networkMonitor)

            val jobProductos = launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.productos.collect {}
            }
            val jobOnline = launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.isOnline.collect {}
            }

            advanceUntilIdle()

            assertEquals(listOf(productoMock), viewModel.productos.value)
            assertFalse(viewModel.isOnline.value)

            isOnlineFlow.value = true
            advanceUntilIdle()

            assertTrue(viewModel.isOnline.value)

            jobProductos.cancel()
            jobOnline.cancel()
        }

    @Test
    fun `init sincroniza pendientes y refresca catalogo cuando vuelve internet`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ProductoViewModel(repository, networkMonitor)
            advanceUntilIdle()

            // Sin red no debe sincronizar
            coVerify(exactly = 0) { repository.sincronizarPendientes() }

            // Al volver internet sincroniza y refresca
            isOnlineFlow.value = true
            advanceUntilIdle()

            coVerify(exactly = 1) { repository.sincronizarPendientes() }
            coVerify(exactly = 1) { repository.refrescarProductos() }
            assertFalse(viewModel.cargando.value)
        }

    @Test
    fun `refrescarCatalogo llama al repositorio y actualiza estado cargando`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ProductoViewModel(repository, networkMonitor)
            advanceUntilIdle()

            viewModel.refrescarCatalogo()
            advanceUntilIdle()

            coVerify(exactly = 1) { repository.refrescarProductos() }
            assertFalse(viewModel.cargando.value)
        }

    @Test
    fun `obtenerProductosEmprendedora filtra por correo en el repositorio`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val productoMock = mockk<ProductoEntity>(relaxed = true)
            every {
                repository.obtenerPorEmprendedora("emprendedora@correo.cl")
            } returns flowOf(listOf(productoMock))

            val viewModel = ProductoViewModel(repository, networkMonitor)
            val resultado = viewModel
                .obtenerProductosEmprendedora("emprendedora@correo.cl")
                .first()

            assertEquals(listOf(productoMock), resultado)
            verify(exactly = 1) {
                repository.obtenerPorEmprendedora("emprendedora@correo.cl")
            }
        }

    // =========================================
    // 2. OPERACIONES CRUD
    // =========================================

    @Test
    fun `crearProducto valido delega la creacion al repositorio`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ProductoViewModel(repository, networkMonitor)

            viewModel.crearProducto(
                nombre = "Bolso reciclado",
                descripcion = "Tela reutilizada",
                precio = 15000,
                tipo = "PRODUCTO",
                imagenRuta = "/ruta/img.jpg",
                correoEmprendedora = "emprendedora@correo.cl",
                nombreEmprendimiento = "Taller Circular"
            )
            advanceUntilIdle()

            coVerify(exactly = 1) {
                repository.crearProducto(
                    nombre = "Bolso reciclado",
                    descripcion = "Tela reutilizada",
                    precio = 15000,
                    tipo = "PRODUCTO",
                    imagenRuta = "/ruta/img.jpg",
                    correoEmprendedora = "emprendedora@correo.cl",
                    nombreEmprendimiento = "Taller Circular"
                )
            }
        }

    @Test
    fun `crearProducto con nombre vacio o precio invalido no llama al repositorio`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ProductoViewModel(repository, networkMonitor)

            viewModel.crearProducto(
                nombre = "   ",
                descripcion = "Desc",
                precio = 10000,
                tipo = "PRODUCTO",
                imagenRuta = null,
                correoEmprendedora = "a@b.cl",
                nombreEmprendimiento = "Taller"
            )

            viewModel.crearProducto(
                nombre = "Bolso",
                descripcion = "Desc",
                precio = 0,
                tipo = "PRODUCTO",
                imagenRuta = null,
                correoEmprendedora = "a@b.cl",
                nombreEmprendimiento = "Taller"
            )
            advanceUntilIdle()

            coVerify(exactly = 0) {
                repository.crearProducto(any(), any(), any(), any(), any(), any(), any())
            }
        }

    @Test
    fun `editarProducto valido actualiza datos en el repositorio`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ProductoViewModel(repository, networkMonitor)

            viewModel.editarProducto(
                id = 5,
                nombre = "Mochila actualizada",
                descripcion = "Nueva descripción",
                precio = 25000,
                tipo = "PRODUCTO",
                imagenRuta = "/ruta/nueva.jpg"
            )
            advanceUntilIdle()

            coVerify(exactly = 1) {
                repository.actualizarProducto(
                    id = 5,
                    nombre = "Mochila actualizada",
                    descripcion = "Nueva descripción",
                    precio = 25000,
                    tipo = "PRODUCTO",
                    imagenRuta = "/ruta/nueva.jpg"
                )
            }
        }

    @Test
    fun `editarProducto con nombre vacio o precio menor o igual a cero no actualiza`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ProductoViewModel(repository, networkMonitor)

            viewModel.editarProducto(5, "", "Desc", 10000, "PRODUCTO", null)
            viewModel.editarProducto(5, "Mochila", "Desc", -100, "PRODUCTO", null)
            advanceUntilIdle()

            coVerify(exactly = 0) {
                repository.actualizarProducto(any(), any(), any(), any(), any(), any())
            }
        }

    @Test
    fun `marcarComoVendido y marcarComoDisponible delegan al repositorio`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = ProductoViewModel(repository, networkMonitor)

            viewModel.marcarComoVendido(10)
            viewModel.marcarComoDisponible(10)
            advanceUntilIdle()

            coVerify(exactly = 1) { repository.marcarComoVendido(10) }
            coVerify(exactly = 1) { repository.marcarComoDisponible(10) }
        }

    @Test
    fun `eliminarProducto delega eliminacion al repositorio`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val productoMock = mockk<ProductoEntity>(relaxed = true)
            val viewModel = ProductoViewModel(repository, networkMonitor)

            viewModel.eliminarProducto(productoMock)
            advanceUntilIdle()

            coVerify(exactly = 1) { repository.eliminarProducto(productoMock) }
        }
}