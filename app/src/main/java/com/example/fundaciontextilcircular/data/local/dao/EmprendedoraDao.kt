package com.example.fundaciontextilcircular.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmprendedoraDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarEmprendedora(
        emprendedora: EmprendedoraEntity
    )

    @Query("""
        SELECT *
        FROM emprendedora
        WHERE rut = :rut
        LIMIT 1
    """)
    suspend fun obtenerPorRut(
        rut: String
    ): EmprendedoraEntity?

    @Query("""
        SELECT *
        FROM emprendedora
        ORDER BY nombre ASC
    """)
    fun obtenerTodas():
            Flow<List<EmprendedoraEntity>>

    @Query("""
        SELECT *
        FROM emprendedora
        WHERE estado = 'PENDIENTE'
        ORDER BY nombre ASC
    """)
    fun obtenerPendientes():
            Flow<List<EmprendedoraEntity>>

    @Query("""
        SELECT *
        FROM emprendedora
        WHERE estado = 'APROBADA'
        ORDER BY nombre ASC
    """)
    fun obtenerAprobadas():
            Flow<List<EmprendedoraEntity>>

    @Query("""
        SELECT *
        FROM emprendedora
        WHERE estado = 'RECHAZADA'
        ORDER BY nombre ASC
    """)
    fun obtenerRechazadas():
            Flow<List<EmprendedoraEntity>>

    @Query("""
        SELECT *
        FROM emprendedora
        WHERE correo = :correo
        LIMIT 1
    """)
    suspend fun obtenerPorCorreo(
        correo: String
    ): EmprendedoraEntity?

    @Query("""
        SELECT *
        FROM emprendedora
        WHERE correo = :correo
        LIMIT 1
    """)
    fun observarPorCorreo(
        correo: String
    ): Flow<EmprendedoraEntity?>

    @Query("""
        SELECT COUNT(*)
        FROM emprendedora
        WHERE correo = :correo
    """)
    suspend fun existeCorreo(
        correo: String
    ): Int

    @Query("""
        SELECT COUNT(*)
        FROM emprendedora
        WHERE rut = :rut
    """)
    suspend fun existeRut(
        rut: String
    ): Int

    @Query("""
        UPDATE emprendedora
        SET estado = 'APROBADA',
            motivoRechazo = NULL
        WHERE rut = :rut
    """)
    suspend fun aprobarCuenta(
        rut: String
    )

    @Query("""
        UPDATE emprendedora
        SET estado = 'RECHAZADA',
            motivoRechazo = :motivo
        WHERE rut = :rut
    """)
    suspend fun rechazarCuenta(
        rut: String,
        motivo: String
    )

    @Query("""
        SELECT *
        FROM emprendedora
        WHERE correo = :correo
        AND contrasena = :contrasena
        LIMIT 1
    """)
    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): EmprendedoraEntity?

    @Query("""
        DELETE FROM emprendedora
        WHERE rut = :rut
    """)
    suspend fun eliminarCuenta(
        rut: String
    )
}