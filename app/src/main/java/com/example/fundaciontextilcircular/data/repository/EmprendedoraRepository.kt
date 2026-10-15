package com.example.fundaciontextilcircular.data.repository

import com.example.fundaciontextilcircular.data.local.dao.EmprendedoraDao
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity
import kotlinx.coroutines.flow.Flow

class EmprendedoraRepository(
    private val emprendedoraDao: EmprendedoraDao
) {

    // TODAS LAS EMPRENDEDORAS
    val todasLasEmprendedoras:
            Flow<List<EmprendedoraEntity>> =
        emprendedoraDao.obtenerTodas()


    // SOLICITUDES PENDIENTES
    val solicitudesPendientes:
            Flow<List<EmprendedoraEntity>> =
        emprendedoraDao.obtenerPendientes()


    // GUARDAR NUEVA EMPRENDEDORA
    suspend fun guardarEmprendedora(
        emprendedora: EmprendedoraEntity
    ) {
        emprendedoraDao.insertarEmprendedora(
            emprendedora
        )
    }


    // BUSCAR POR RUT
    suspend fun obtenerEmprendedoraRut(
        rut: String
    ): EmprendedoraEntity? {

        return emprendedoraDao.obtenerPorRut(
            rut
        )
    }


    // BUSCAR POR CORREO
    suspend fun obtenerPorCorreo(
        correo: String
    ): EmprendedoraEntity? {

        return emprendedoraDao.obtenerPorCorreo(
            correo
        )
    }


    // OBSERVAR ESTADO DE CUENTA
    fun observarPorCorreo(
        correo: String
    ): Flow<EmprendedoraEntity?> {

        return emprendedoraDao.observarPorCorreo(
            correo
        )
    }


    // COMPROBAR CORREO DUPLICADO
    suspend fun existeCorreo(
        correo: String
    ): Boolean {

        return emprendedoraDao.existeCorreo(
            correo
        ) > 0
    }


    // COMPROBAR RUT DUPLICADO
    suspend fun existeRut(
        rut: String
    ): Boolean {

        return emprendedoraDao.existeRut(
            rut
        ) > 0
    }


    // APROBAR CUENTA
    suspend fun aprobarCuenta(
        rut: String
    ) {

        emprendedoraDao.aprobarCuenta(
            rut
        )
    }


    // RECHAZAR CUENTA
    suspend fun rechazarCuenta(
        rut: String,
        motivo: String
    ) {

        emprendedoraDao.rechazarCuenta(
            rut,
            motivo
        )
    }


    // INICIAR SESIÓN
    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): EmprendedoraEntity? {

        return emprendedoraDao.iniciarSesion(
            correo,
            contrasena
        )
    }
}