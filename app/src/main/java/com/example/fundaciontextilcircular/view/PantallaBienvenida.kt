package com.example.fundaciontextilcircular.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fundaciontextilcircular.R
import com.example.fundaciontextilcircular.ui.theme.AmarilloTextil
import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil

@Composable
fun PantallaBienvenida(
    modifier: Modifier = Modifier,
    onIniciarSesion: () -> Unit = {},
    onCrearCuenta: () -> Unit = {}
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        // Imagen de fondo
        Image(
            painter = painterResource(
                id = R.drawable.comunidad_moda_sostenible
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Capa oscura para que los textos se puedan leer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.35f)
                )
        )

        // Contenido de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
            ) {

                Text(
                    text = "ECONOMÍA CIRCULAR TEXTIL",
                    color = TurquesaTextil,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .background(
                            color = NegroTextil.copy(alpha = 0.75f),
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
                    text = "TEXTIL",
                    color = BlancoTextil,
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = "CIRCULAR",
                    color = AmarilloTextil,
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = "Conecta, crea y transforma.",
                    color = BlancoTextil,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Un espacio para personas emprendedoras que forman parte de la comunidad Textil Circular.",
                    color = BlancoTextil,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = onIniciarSesion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TurquesaTextil,
                        contentColor = NegroTextil
                    )
                ) {

                    Text(
                        text = "INICIAR SESIÓN",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                OutlinedButton(
                    onClick = onCrearCuenta,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BlancoTextil
                    )
                ) {

                    Text(
                        text = "CREAR CUENTA",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}