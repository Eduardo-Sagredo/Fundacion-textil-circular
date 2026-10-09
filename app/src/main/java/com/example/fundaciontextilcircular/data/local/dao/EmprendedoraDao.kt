package com.example.fundaciontextilcircular.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmprendedoraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEmprendedora(emprendedora: EmprendedoraEntity)

     @Query("SELECT * FROM emprendedora WHERE rut = :rut")
     suspend fun obtenerPorRut(rut: String): EmprendedoraEntity?

     @Query("SELECT * FROM emprendedora")
     fun obtenerTodas(): Flow<List<EmprendedoraEntity>>
}