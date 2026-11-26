package com.example.fundaciontextilcircular.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextilcircular.data.local.entities.ProductoEntity
import com.example.fundaciontextilcircular.data.repository.ProductoRepository
import com.example.fundaciontextilcircular.data.utils.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val repository: ProductoRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    // =========================
    // CATÁLOGO COMPLETO
    // =========================
    val productos: StateFlow<List<ProductoEntity>> = repository
        .obtenerTodos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // =========================
    // PRODUCTOS POR EMPRENDEDORA
    // =========================
    fun obtenerProductosEmprendedora(correo: String): Flow<List<ProductoEntity>> =
        repository.obtenerPorEmprendedora(correo)

    // =========================
    // ESTADO DE RED Y CARGA
    // =========================
    val isOnline: StateFlow<Boolean> = networkMonitor
        .isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    // =========================
    // SINCRONIZAR AL VOLVER RED
    // =========================
    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { online ->
                if (online) {
                    repository.sincronizarPendientes()
                    refrescarCatalogo()
                }
            }
        }
    }

    fun refrescarCatalogo() {
        viewModelScope.launch {
            _cargando.value = true
            repository.refrescarProductos()
            _cargando.value = false
        }
    }

    // =========================
    // OPERACIONES CRUD
    // =========================
    fun crearProducto(
        nombre: String,
        descripcion: String,
        precio: Int,
        tipo: String,
        imagenRuta: String?,
        correoEmprendedora: String,
        nombreEmprendimiento: String
    ) {
        if (nombre.isBlank() || precio <= 0) return

        viewModelScope.launch {
            repository.crearProducto(
                nombre = nombre,
                descripcion = descripcion,
                precio = precio,
                tipo = tipo,
                imagenRuta = imagenRuta,
                correoEmprendedora = correoEmprendedora,
                nombreEmprendimiento = nombreEmprendimiento
            )
        }
    }

    fun editarProducto(
        id: Int,
        nombre: String,
        descripcion: String,
        precio: Int,
        tipo: String,
        imagenRuta: String?
    ) {
        if (nombre.isBlank() || precio <= 0) return

        viewModelScope.launch {
            repository.actualizarProducto(
                id = id,
                nombre = nombre,
                descripcion = descripcion,
                precio = precio,
                tipo = tipo,
                imagenRuta = imagenRuta
            )
        }
    }

    fun marcarComoVendido(id: Int) {
        viewModelScope.launch {
            repository.marcarComoVendido(id)
        }
    }

    fun marcarComoDisponible(id: Int) {
        viewModelScope.launch {
            repository.marcarComoDisponible(id)
        }
    }

    fun eliminarProducto(producto: ProductoEntity) {
        viewModelScope.launch {
            repository.eliminarProducto(producto)
        }
    }
}