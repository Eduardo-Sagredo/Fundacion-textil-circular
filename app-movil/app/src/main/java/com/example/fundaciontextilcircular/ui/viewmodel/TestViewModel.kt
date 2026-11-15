package com.example.fundaciontextilcircular.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity
import com.example.fundaciontextilcircular.data.repository.EmprendedoraRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TestViewModel(private val repository: EmprendedoraRepository) : ViewModel() {

    val emprendedorasGuardadas = repository.todasLasEmprendedoras
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertarEmprendedoraFalsa() {
        viewModelScope.launch {
            val nuevaEmprendedora = EmprendedoraEntity(
                rut = "12345678-9",
                nombre = "María Gómez",
                correo = "mar.gomez@gmail.com",
                contrasena = "Margom123",
                nombreEmprendimiento = "Tejidos María",
                rubro = "Reparación",
                region = "Valparaíso",
                comuna = "Viña del Mar",
                edad = 53,
                genero = "Femenino",
                rol = "EMPRENDEDORA",
                estado = "PENDIENTE",
                motivoRechazo = null


            )
            repository.guardarEmprendedora(nuevaEmprendedora)
        }
    }
}