package com.example.fundaciontextilcircular.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmprendedoraDao {

    // REGISTRAR EMPRENDEDORA
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarEmprendedora(
        emprendedora: EmprendedoraEntity
    )

    // BUSCAR POR RUT
    @Query(
        """
        SELECT *
        FROM emprendedora
        WHERE rut = :rut
        LIMIT 1
        """
    )
    suspend fun obtenerPorRut(
        rut: String
    ): EmprendedoraEntity?

    // OBTENER TODAS
    @Query(
        """
        SELECT *
        FROM emprendedora
        ORDER BY nombre ASC
        """
    )
    fun obtenerTodas():
            Flow<List<EmprendedoraEntity>>

    // SOLICITUDES PENDIENTES
    @Query(
        """
        SELECT *
        FROM emprendedora
        WHERE estado = 'PENDIENTE'
        ORDER BY nombre ASC
        """
    )
    fun obtenerPendientes():
            Flow<List<EmprendedoraEntity>>

    // BUSCAR POR CORREO
    @Query(
        """
        SELECT *
        FROM emprendedora
        WHERE correo = :correo
        LIMIT 1
        """
    )
    suspend fun obtenerPorCorreo(
        correo: String
    ): EmprendedoraEntity?

    // OBSERVAR ESTADO DE UNA CUENTA
    @Query(
        """
        SELECT *
        FROM emprendedora
        WHERE correo = :correo
        LIMIT 1
        """
    )
    fun observarPorCorreo(
        correo: String
    ): Flow<EmprendedoraEntity?>

    // COMPROBAR CORREO DUPLICADO
    @Query(
        """
        SELECT COUNT(*)
        FROM emprendedora
        WHERE correo = :correo
        """
    )
    suspend fun existeCorreo(
        correo: String
    ): Int

    // COMPROBAR RUT DUPLICADO
    @Query(
        """
        SELECT COUNT(*)
        FROM emprendedora
        WHERE rut = :rut
        """
    )
    suspend fun existeRut(
        rut: String
    ): Int

    // APROBAR CUENTA
    @Query(
        """
        UPDATE emprendedora
        SET estado = 'APROBADA',
            motivoRechazo = NULL
        WHERE rut = :rut
        """
    )
    suspend fun aprobarCuenta(
        rut: String
    )

    // RECHAZAR CUENTA
    @Query(
        """
        UPDATE emprendedora
        SET estado = 'RECHAZADA',
            motivoRechazo = :motivo
        WHERE rut = :rut
        """
    )
    suspend fun rechazarCuenta(
        rut: String,
        motivo: String
    )

    // INICIO DE SESIÓN
    @Query(
        """
        SELECT *
        FROM emprendedora
        WHERE correo = :correo
        AND contrasena = :contrasena
        LIMIT 1
        """
    )
    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): EmprendedoraEntity?
}