package com.example.fundaciontextilcircular.data.repository

import com.example.fundaciontextilcircular.data.local.dao.EmprendedoraDao
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity
import kotlinx.coroutines.flow.Flow

class EmprendedoraRepository(private val emprendedoraDao: EmprendedoraDao) {

    val todasLasEmprendedoras: Flow<List<EmprendedoraEntity>> = emprendedoraDao.obtenerTodas()

    suspend fun guardarEmprendedora(emprendedora: EmprendedoraEntity){
        emprendedoraDao.insertarEmprendedora(emprendedora)
    }

    suspend fun obtenerEmprendedoraRut(rut:String): EmprendedoraEntity?{
        return emprendedoraDao.obtenerPorRut(rut)
    }
}