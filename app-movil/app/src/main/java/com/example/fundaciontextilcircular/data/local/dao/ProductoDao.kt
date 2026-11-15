package com.example.fundaciontextilcircular.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fundaciontextilcircular.data.local.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Insert(
        onConflict = OnConflictStrategy.ABORT
    )
    suspend fun insertarProducto(
        producto: ProductoEntity
    )


    @Query(
        """
        SELECT *
        FROM productos
        ORDER BY fechaCreacion DESC
        """
    )
    fun obtenerTodos():
            Flow<List<ProductoEntity>>


    @Query(
        """
        SELECT *
        FROM productos
        WHERE correoEmprendedora = :correo
        ORDER BY fechaCreacion DESC
        """
    )
    fun obtenerPorEmprendedora(
        correo: String
    ): Flow<List<ProductoEntity>>


    @Query(
        """
        SELECT *
        FROM productos
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun obtenerPorId(
        id: Int
    ): ProductoEntity?


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
            imagenRuta = :imagenRuta
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
    // MARCAR VENDIDO
    // =========================

    @Query(
        """
        UPDATE productos
        SET estado = 'VENDIDO'
        WHERE id = :id
        """
    )
    suspend fun marcarComoVendido(
        id: Int
    )


    // =========================
    // VOLVER A DISPONIBLE
    // =========================

    @Query(
        """
        UPDATE productos
        SET estado = 'DISPONIBLE'
        WHERE id = :id
        """
    )
    suspend fun marcarComoDisponible(
        id: Int
    )


    // =========================
    // ELIMINAR
    // =========================

    @Delete
    suspend fun eliminarProducto(
        producto: ProductoEntity
    )
}