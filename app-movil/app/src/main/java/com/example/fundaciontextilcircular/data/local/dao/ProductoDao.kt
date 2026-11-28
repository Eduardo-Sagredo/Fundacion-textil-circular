package com.example.fundaciontextilcircular.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fundaciontextilcircular.data.local.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarProducto(producto: ProductoEntity): Long

    @Update
    suspend fun actualizarEntidad(producto: ProductoEntity)

    @Query(
        """
        SELECT *
        FROM productos
        WHERE accionPendiente IS NULL OR accionPendiente != 'ELIMINAR'
        ORDER BY fechaCreacion DESC
        """
    )
    fun obtenerTodos(): Flow<List<ProductoEntity>>

    @Query(
        """
        SELECT *
        FROM productos
        WHERE correoEmprendedora = :correo
          AND (accionPendiente IS NULL OR accionPendiente != 'ELIMINAR')
        ORDER BY fechaCreacion DESC
        """
    )
    fun obtenerPorEmprendedora(correo: String): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): ProductoEntity?

    @Query("SELECT * FROM productos WHERE remoteId = :remoteId LIMIT 1")
    suspend fun obtenerPorRemoteId(remoteId: Long): ProductoEntity?

    @Query("SELECT * FROM productos WHERE accionPendiente IS NOT NULL ORDER BY id ASC")
    suspend fun obtenerPendientesSincronizacion(): List<ProductoEntity>

    // =========================
    // EDITAR
    // =========================
    @Query(
        """
        UPDATE productos
        SET nombre = :nombre,
            descripcion = :descripcion,
            precio = :precio,
            tipo = :tipo,
            imagenRuta = :imagenRuta,
            accionPendiente = CASE WHEN remoteId IS NULL THEN 'CREAR' ELSE 'ACTUALIZAR' END
        WHERE id = :id
        """
    )
    suspend fun actualizarProducto(
        id: Int,
        nombre: String,
        descripcion: String,
        precio: Int,
        tipo: String,
        imagenRuta: String?
    )

    // =========================
    // CAMBIAR DISPONIBILIDAD
    // =========================
    @Query(
        """
        UPDATE productos
        SET estado = 'VENDIDO',
            accionPendiente = CASE WHEN remoteId IS NULL THEN 'CREAR' ELSE 'ESTADO' END
        WHERE id = :id
        """
    )
    suspend fun marcarComoVendido(id: Int)

    @Query(
        """
        UPDATE productos
        SET estado = 'DISPONIBLE',
            accionPendiente = CASE WHEN remoteId IS NULL THEN 'CREAR' ELSE 'ESTADO' END
        WHERE id = :id
        """
    )
    suspend fun marcarComoDisponible(id: Int)

    // =========================
    // CONTROL DE SINCRONIZACIÓN
    // =========================
    @Query("UPDATE productos SET remoteId = :remoteId, accionPendiente = NULL WHERE id = :localId")
    suspend fun confirmarCreacionRemota(localId: Int, remoteId: Long)

    @Query("UPDATE productos SET accionPendiente = NULL WHERE id = :localId")
    suspend fun marcarSincronizado(localId: Int)

    @Query("UPDATE productos SET accionPendiente = 'ELIMINAR' WHERE id = :localId")
    suspend fun marcarParaEliminar(localId: Int)

    @Query("DELETE FROM productos WHERE id = :localId")
    suspend fun eliminarPorId(localId: Int)

    @Query("SELECT * FROM productos WHERE remoteId IS NOT NULL AND accionPendiente IS NULL")
    suspend fun obtenerSincronizados(): List<ProductoEntity>

    @Delete
    suspend fun eliminarProducto(producto: ProductoEntity)
}