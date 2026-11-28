package com.example.fundaciontextilcircular.data.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.fundaciontextilcircular.data.local.dao.ProductoDao
import com.example.fundaciontextilcircular.data.local.entities.ProductoEntity
import com.example.fundaciontextilcircular.data.network.ProductoRequestDto
import com.example.fundaciontextilcircular.data.network.RetrofitClient
import com.example.fundaciontextilcircular.data.network.TextilCircularApiService
import com.example.fundaciontextilcircular.data.worker.SincronizarProductosWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit

class ProductoRepository(
    private val productoDao: ProductoDao,
    private val appContext: Context,
    private val apiService: TextilCircularApiService = RetrofitClient.apiService
) {
    private val syncMutex = Mutex()

    // =========================
    // LECTURAS REACTIVAS (ROOM)
    // =========================
    fun obtenerTodos(): Flow<List<ProductoEntity>> = productoDao.obtenerTodos()

    fun obtenerPorEmprendedora(correo: String): Flow<List<ProductoEntity>> =
        productoDao.obtenerPorEmprendedora(correo)

    suspend fun obtenerPorId(id: Int): ProductoEntity? = productoDao.obtenerPorId(id)

    // =========================
    // DESCARGAR CATÁLOGO (GET)
    // =========================
    suspend fun refrescarProductos(): Boolean {
        return try {
            val response = apiService.listarProductos()
            if (!response.isSuccessful) return false

            val remotos = response.body().orEmpty()
            val remoteIdsVigentes = remotos.map { it.id }.toSet()

            for (dto in remotos) {
                val local = productoDao.obtenerPorRemoteId(dto.id)
                if (local == null) {
                    productoDao.insertarProducto(
                        ProductoEntity(
                            remoteId = dto.id,
                            nombre = dto.nombre,
                            descripcion = dto.descripcion,
                            precio = dto.precio.toInt(),
                            tipo = dto.categoria,
                            imagenRuta = dto.imagenUrl,
                            correoEmprendedora = "",
                            nombreEmprendimiento = "Fundación Textil Circular",
                            estado = if (dto.disponible) "DISPONIBLE" else "VENDIDO",
                            accionPendiente = null
                        )
                    )
                } else if (local.accionPendiente == null) {
                    productoDao.actualizarEntidad(
                        local.copy(
                            nombre = dto.nombre,
                            descripcion = dto.descripcion,
                            precio = dto.precio.toInt(),
                            tipo = dto.categoria,
                            imagenRuta = dto.imagenUrl,
                            estado = if (dto.disponible) "DISPONIBLE" else "VENDIDO"
                        )
                    )
                }
            }


            productoDao.obtenerSincronizados().forEach { local ->
                if (local.remoteId != null && local.remoteId !in remoteIdsVigentes) {
                    productoDao.eliminarPorId(local.id)
                }
            }

            true
        } catch (_: Exception) {
            false
        }
    }

    // =========================
    // CREAR PRODUCTO (OFFLINE-FIRST)
    // =========================
    suspend fun crearProducto(
        nombre: String,
        descripcion: String,
        precio: Int,
        tipo: String,
        imagenRuta: String?,
        correoEmprendedora: String,
        nombreEmprendimiento: String
    ) {
        val nuevo = ProductoEntity(
            nombre = nombre.trim(),
            descripcion = descripcion.trim(),
            precio = precio,
            tipo = tipo.trim(),
            imagenRuta = imagenRuta,
            correoEmprendedora = correoEmprendedora.trim(),
            nombreEmprendimiento = nombreEmprendimiento.trim(),
            estado = "DISPONIBLE",
            accionPendiente = "CREAR"
        )
        productoDao.insertarProducto(nuevo)
        dispararSincronizacion()
    }

    // =========================
    // EDITAR PRODUCTO (PUT)
    // =========================
    suspend fun actualizarProducto(
        id: Int,
        nombre: String,
        descripcion: String,
        precio: Int,
        tipo: String,
        imagenRuta: String?
    ) {
        productoDao.actualizarProducto(
            id = id,
            nombre = nombre.trim(),
            descripcion = descripcion.trim(),
            precio = precio,
            tipo = tipo.trim(),
            imagenRuta = imagenRuta
        )
        dispararSincronizacion()
    }

    // =========================
    // DISPONIBILIDAD (PATCH)
    // =========================
    suspend fun marcarComoVendido(id: Int) {
        productoDao.marcarComoVendido(id)
        dispararSincronizacion()
    }

    suspend fun marcarComoDisponible(id: Int) {
        productoDao.marcarComoDisponible(id)
        dispararSincronizacion()
    }

    // =========================
    // ELIMINAR PRODUCTO (DELETE)
    // =========================
    suspend fun eliminarProducto(producto: ProductoEntity) {
        if (producto.remoteId == null) {
            productoDao.eliminarPorId(producto.id)
            return
        }
        productoDao.marcarParaEliminar(producto.id)
        dispararSincronizacion()
    }

    // =========================
    // COLA DE SINCRONIZACIÓN
    // =========================
    private suspend fun dispararSincronizacion() {
        sincronizarPendientes()
        if (productoDao.obtenerPendientesSincronizacion().isNotEmpty()) {
            programarSincronizacionEnSegundoPlano()
        }
    }

    suspend fun sincronizarPendientes() {
        syncMutex.withLock {
            val pendientes = productoDao.obtenerPendientesSincronizacion()
            if (pendientes.isEmpty()) return

            for (item in pendientes) {
                try {
                    when (item.accionPendiente) {
                        "CREAR" -> {
                            val request = ProductoRequestDto(
                                nombre = item.nombre,
                                descripcion = item.descripcion,
                                precio = item.precio.toDouble(),
                                categoria = item.tipo,
                                imagenUrl = item.imagenRuta,
                                disponible = item.estado == "DISPONIBLE",
                                usuarioId = null
                            )
                            val response = apiService.crearProducto(request)
                            if (response.isSuccessful) {
                                response.body()?.let { creado ->
                                    productoDao.confirmarCreacionRemota(item.id, creado.id)
                                }
                            }
                        }

                        "ACTUALIZAR" -> {
                            val remoteId = item.remoteId ?: continue
                            val request = ProductoRequestDto(
                                nombre = item.nombre,
                                descripcion = item.descripcion,
                                precio = item.precio.toDouble(),
                                categoria = item.tipo,
                                imagenUrl = item.imagenRuta,
                                disponible = item.estado == "DISPONIBLE",
                                usuarioId = null
                            )
                            val response = apiService.actualizarProducto(remoteId, request)
                            if (response.isSuccessful) {
                                productoDao.marcarSincronizado(item.id)
                            } else if (response.code() == 404) {
                                productoDao.eliminarPorId(item.id)
                            }
                        }

                        "ESTADO" -> {
                            val remoteId = item.remoteId ?: continue
                            val disponible = item.estado == "DISPONIBLE"
                            val response = apiService.cambiarDisponibilidad(remoteId, disponible)
                            if (response.isSuccessful) {
                                productoDao.marcarSincronizado(item.id)
                            } else if (response.code() == 404) {
                                productoDao.eliminarPorId(item.id)
                            }
                        }

                        "ELIMINAR" -> {
                            val remoteId = item.remoteId
                            if (remoteId == null) {
                                productoDao.eliminarPorId(item.id)
                            } else {
                                val response = apiService.eliminarProducto(remoteId)
                                if (response.isSuccessful || response.code() == 404) {
                                    productoDao.eliminarPorId(item.id)
                                }
                            }
                        }
                    }
                } catch (_: Exception) {
                    break
                }
            }
        }
    }

    private fun programarSincronizacionEnSegundoPlano() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<SincronizarProductosWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            "sync_productos_pendientes",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
}