package com.example.fundaciontextilcircular.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.ui.viewmodel.TestViewModel

@Composable
fun TestScreen(viewModel: TestViewModel) {
    val listaEmprendedoras by viewModel.emprendedorasGuardadas.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { viewModel.insertarEmprendedoraFalsa() }) {
            Text("Guardar Emprendedora en Base de Datos")
        }

        Text(text = "Emprendedoras en la Base de Datos:", modifier = Modifier.padding(top = 16.dp))

        LazyColumn {
            items(listaEmprendedoras) { emprendedora ->
                Text(text = "- ${emprendedora.nombre} (${emprendedora.nombreEmprendimiento}) - RUT: ${emprendedora.rut}")
            }
        }
    }
}