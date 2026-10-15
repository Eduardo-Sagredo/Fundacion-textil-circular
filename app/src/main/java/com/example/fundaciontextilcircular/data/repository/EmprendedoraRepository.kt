package com.example.fundaciontextilcircular.data.repository

import com.example.fundaciontextilcircular.data.local.dao.EmprendedoraDao
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity
import kotlinx.coroutines.flow.Flow

class EmprendedoraRepository(
    private val emprendedoraDao: EmprendedoraDao
) {

    val todasLasEmprendedoras:
            Flow<List<EmprendedoraEntity>> =
        emprendedoraDao.obtenerTodas()

    val solicitudesPendientes:
            Flow<List<EmprendedoraEntity>> =
        emprendedoraDao.obtenerPendientes()

    val cuentasAprobadas:
            Flow<List<EmprendedoraEntity>> =
        emprendedoraDao.obtenerAprobadas()

    val cuentasRechazadas:
            Flow<List<EmprendedoraEntity>> =
        emprendedoraDao.obtenerRechazadas()

    suspend fun guardarEmprendedora(
        emprendedora: EmprendedoraEntity
    ) {
        emprendedoraDao
            .insertarEmprendedora(
                emprendedora
            )
    }

    suspend fun obtenerEmprendedoraRut(
        rut: String
    ): EmprendedoraEntity? {

        return emprendedoraDao
            .obtenerPorRut(rut)
    }

    suspend fun obtenerPorCorreo(
        correo: String
    ): EmprendedoraEntity? {

        return emprendedoraDao
            .obtenerPorCorreo(correo)
    }

    fun observarPorCorreo(
        correo: String
    ): Flow<EmprendedoraEntity?> {

        return emprendedoraDao
            .observarPorCorreo(correo)
    }

    suspend fun existeCorreo(
        correo: String
    ): Boolean {

        return emprendedoraDao
            .existeCorreo(correo) > 0
    }

    suspend fun existeRut(
        rut: String
    ): Boolean {

        return emprendedoraDao
            .existeRut(rut) > 0
    }

    suspend fun aprobarCuenta(
        rut: String
    ) {

        emprendedoraDao
            .aprobarCuenta(rut)
    }

    suspend fun rechazarCuenta(
        rut: String,
        motivo: String
    ) {

        emprendedoraDao
            .rechazarCuenta(
                rut,
                motivo
            )
    }

    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): EmprendedoraEntity? {

        return emprendedoraDao
            .iniciarSesion(
                correo,
                contrasena
            )
    }

    suspend fun eliminarCuenta(
        rut: String
    ) {

        emprendedoraDao
            .eliminarCuenta(rut)
    }
}