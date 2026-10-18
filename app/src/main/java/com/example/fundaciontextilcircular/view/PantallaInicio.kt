package com.example.fundaciontextilcircular.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.ui.theme.AmarilloTextil
import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil

@Composable
fun PantallaInicio(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BlancoTextil)
            .verticalScroll(rememberScrollState())
    ) {

        // Sección principal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NegroTextil)
                .padding(
                    horizontal = 24.dp,
                    vertical = 32.dp
                )
        ) {

            Text(
                text = "ECONOMÍA CIRCULAR TEXTIL — CHILE",
                color = TurquesaTextil,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "LA ROPA",
                color = BlancoTextil,
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = "TIENE EL PODER",
                color = BlancoTextil,
                style = MaterialTheme.typography.headlineLarge
            )

            Row {

                Text(
                    text = "DE ",
                    color = BlancoTextil,
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = "TEJER",
                    color = TurquesaTextil,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "COMUNIDAD.",
                color = NegroTextil,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .background(
                        color = AmarilloTextil,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    )
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text = "Conectamos a la comunidad con personas emprendedoras locales que transforman la forma en que consumimos ropa.",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Sección bienvenida
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            Text(
                text = "Te damos la bienvenida",
                color = NegroTextil,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Gestiona tu perfil, publica tus productos y mantente conectado con la comunidad de Textil Circular.",
                color = NegroTextil,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text = "Tu espacio Textil Circular",
                color = TurquesaTextil,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Desde aquí podrás administrar tu catálogo, revisar tus mensajes y mantener actualizada la información de tu emprendimiento.",
                color = NegroTextil,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )
        }
    }
}