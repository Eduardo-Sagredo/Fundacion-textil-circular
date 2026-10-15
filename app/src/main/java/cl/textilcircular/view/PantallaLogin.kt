package cl.textilcircular.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cl.textilcircular.ui.theme.BlancoTextil
import cl.textilcircular.ui.theme.NegroTextil
import cl.textilcircular.ui.theme.TurquesaTextil

@Composable
fun PantallaLogin(
    modifier: Modifier = Modifier,
    onIngresar: () -> Unit = {},
    onCrearCuenta: () -> Unit = {},
    onVolver: () -> Unit = {}
) {

    var correo by remember {
        mutableStateOf("")
    }

    var contrasena by remember {
        mutableStateOf("")
    }

    var mostrarContrasena by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NegroTextil)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Spacer(
            modifier = Modifier.height(60.dp)
        )

        Text(
            text = "INICIAR",
            color = BlancoTextil,
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "SESIÓN",
            color = TurquesaTextil,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Ingresa tus datos para acceder a tu espacio Textil Circular.",
            color = BlancoTextil,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Correo electrónico")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Correo electrónico"
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = BlancoTextil,
                unfocusedTextColor = BlancoTextil,
                focusedBorderColor = TurquesaTextil,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = TurquesaTextil,
                unfocusedLabelColor = Color.LightGray,
                focusedLeadingIconColor = TurquesaTextil,
                unfocusedLeadingIconColor = Color.LightGray,
                cursorColor = TurquesaTextil
            )
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = contrasena,
            onValueChange = {
                contrasena = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Contraseña")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Contraseña"
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        mostrarContrasena = !mostrarContrasena
                    }
                ) {

                    Icon(
                        imageVector = if (mostrarContrasena) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = if (mostrarContrasena) {
                            "Ocultar contraseña"
                        } else {
                            "Mostrar contraseña"
                        }
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (mostrarContrasena) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = BlancoTextil,
                unfocusedTextColor = BlancoTextil,
                focusedBorderColor = TurquesaTextil,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = TurquesaTextil,
                unfocusedLabelColor = Color.LightGray,
                focusedLeadingIconColor = TurquesaTextil,
                unfocusedLeadingIconColor = Color.LightGray,
                focusedTrailingIconColor = TurquesaTextil,
                unfocusedTrailingIconColor = Color.LightGray,
                cursorColor = TurquesaTextil
            )
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Button(
            onClick = onIngresar,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = TurquesaTextil,
                contentColor = NegroTextil
            )
        ) {

            Text(
                text = "INGRESAR",
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        TextButton(
            onClick = onCrearCuenta,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "¿No tienes una cuenta? Crear cuenta",
                color = BlancoTextil
            )
        }

        TextButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Volver",
                color = TurquesaTextil
            )
        }
    }
}