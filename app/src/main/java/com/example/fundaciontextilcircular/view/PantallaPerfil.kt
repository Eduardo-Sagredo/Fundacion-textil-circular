package com.example.fundaciontextilcircular.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil

@Composable
fun PantallaPerfil(
    correo: String,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val dao = remember(context) {
        TextilDatabase
            .getDatabase(context)
            .emprendedoraDao()
    }

    val cuenta by dao
        .observarPorCorreo(correo)
        .collectAsState(initial = null)


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NegroTextil)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp)
    ) {

        Spacer(
            modifier = Modifier.height(30.dp)
        )


        Text(
            text = "MI",
            color = BlancoTextil,
            style =
                MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "PERFIL",
            color = TurquesaTextil,
            style =
                MaterialTheme.typography.headlineLarge
        )


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        if (cuenta == null) {

            Text(
                text = "Cargando información...",
                color = BlancoTextil,
                style =
                    MaterialTheme.typography.bodyLarge
            )

        } else {

            val emprendedora = cuenta!!


            // DATOS PERSONALES

            Text(
                text = "Datos personales",
                color = TurquesaTextil,
                style =
                    MaterialTheme.typography.titleMedium
            )


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            TarjetaDatoPerfil(
                titulo = "Nombre",
                valor = emprendedora.nombre
            )


            TarjetaDatoPerfil(
                titulo = "RUT",
                valor = emprendedora.rut
            )


            TarjetaDatoPerfil(
                titulo = "Correo electrónico",
                valor = emprendedora.correo
            )


            TarjetaDatoPerfil(
                titulo = "Edad",
                valor = emprendedora.edad.toString()
            )


            TarjetaDatoPerfil(
                titulo = "Género",
                valor = emprendedora.genero
            )


            Spacer(
                modifier = Modifier.height(28.dp)
            )


            // EMPRENDIMIENTO

            Text(
                text = "Datos del emprendimiento",
                color = TurquesaTextil,
                style =
                    MaterialTheme.typography.titleMedium
            )


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            TarjetaDatoPerfil(
                titulo = "Nombre del emprendimiento",
                valor =
                    emprendedora.nombreEmprendimiento
            )


            TarjetaDatoPerfil(
                titulo = "Rubro o actividad",
                valor = emprendedora.rubro
            )


            TarjetaDatoPerfil(
                titulo = "Región",
                valor = emprendedora.region
            )


            TarjetaDatoPerfil(
                titulo = "Comuna",
                valor = emprendedora.comuna
            )


            Spacer(
                modifier = Modifier.height(28.dp)
            )


            // ESTADO

            Text(
                text = "Estado de la cuenta",
                color = TurquesaTextil,
                style =
                    MaterialTheme.typography.titleMedium
            )


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(12.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            TurquesaTextil
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(18.dp)
                ) {

                    Text(
                        text = "CUENTA ACTIVA",
                        color = NegroTextil,
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            "Tu cuenta está habilitada para utilizar Textil Circular.",
                        color = NegroTextil
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(30.dp)
            )
        }
    }
}


@Composable
fun TarjetaDatoPerfil(
    titulo: String,
    valor: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 10.dp
            ),

        shape =
            RoundedCornerShape(12.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFF1E1E1E)
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text = titulo,
                color = TurquesaTextil,
                style =
                    MaterialTheme
                        .typography
                        .labelLarge
            )


            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )


            Text(
                text = valor,
                color = BlancoTextil,
                style =
                    MaterialTheme
                        .typography
                        .bodyLarge
            )
        }
    }
}