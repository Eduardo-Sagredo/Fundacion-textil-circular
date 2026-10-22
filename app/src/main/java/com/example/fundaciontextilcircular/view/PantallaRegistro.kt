package com.example.fundaciontextilcircular.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity
import com.example.fundaciontextilcircular.data.utils.LocationHelper.obtenerUbicacionSugerida
import com.example.fundaciontextilcircular.ui.theme.BlancoTextil
import com.example.fundaciontextilcircular.ui.theme.NegroTextil
import com.example.fundaciontextilcircular.ui.theme.RojoErrorTextil
import com.example.fundaciontextilcircular.ui.theme.TurquesaTextil
import com.example.fundaciontextilcircular.viewmodel.RegistroViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.launch

@Composable
fun PantallaRegistro(
    modifier: Modifier = Modifier,
    onRegistrar: (String) -> Unit = {},
    onVolver: () -> Unit = {},
    registroViewModel: RegistroViewModel = viewModel()
) {

    val context = LocalContext.current

    val scope =
        rememberCoroutineScope()

    val dao =
        remember(context) {

            TextilDatabase
                .getDatabase(context)
                .emprendedoraDao()
        }

    val auth =
        remember {

            FirebaseAuth.getInstance()
        }


    var errorRegistro by remember {
        mutableStateOf<String?>(null)
    }

    var registrando by remember {
        mutableStateOf(false)
    }


    var nombre by remember {
        mutableStateOf("")
    }

    var rut by remember {
        mutableStateOf("")
    }

    var correo by remember {
        mutableStateOf("")
    }

    var contrasena by remember {
        mutableStateOf("")
    }

    var confirmarContrasena by remember {
        mutableStateOf("")
    }

    var nombreEmprendimiento by remember {
        mutableStateOf("")
    }

    var rubro by remember {
        mutableStateOf("")
    }

    var edad by remember {
        mutableStateOf("")
    }

    var genero by remember {
        mutableStateOf("")
    }

    var mostrarContrasena by remember {
        mutableStateOf(false)
    }

    var mostrarConfirmarContrasena by remember {
        mutableStateOf(false)
    }

    var menuRegionAbierto by remember {
        mutableStateOf(false)
    }

    var busquedaRegion by remember {
        mutableStateOf("")
    }

    var menuComunaAbierto by remember {
        mutableStateOf(false)
    }

    var busquedaComuna by remember {
        mutableStateOf("")
    }

    var menuGeneroAbierto by remember {
        mutableStateOf(false)
    }

    var intentoRegistro by remember {
        mutableStateOf(false)
    }

    var regionGps by remember { mutableStateOf("") }
    var comunaGps by remember { mutableStateOf("") }
    var mostrarDialogoAjustes by remember { mutableStateOf(false) }
    var estaBuscandoUbicacion by remember { mutableStateOf(true) }
    var errorUbicacion by remember { mutableStateOf<String?>(null) }



    val opcionesGenero =
        listOf(
            "Femenino",
            "Masculino",
            "No binario",
            "Prefiero no responder"
        )

    if (mostrarDialogoAjustes) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoAjustes = false },
            title = {
                Text("Permiso obligatorio", color = TurquesaTextil)
            },
            text = {
                Text(
                    "Para poder registrarte necesitamos acceder a tu ubicación. Por favor, actívala en los ajustes de la aplicación.",
                    color = BlancoTextil
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoAjustes = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Text("Acceder a ajustes", color = TurquesaTextil)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoAjustes = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            containerColor = NegroTextil
        )
    }

    val lanzadorPermisos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permisos ->
            val permisoOtorgado = permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (permisoOtorgado) {
                obtenerUbicacionSugerida(
                    context = context,
                    onResultado = { comunaEncontrada, regionEncontrada ->
                        comunaGps = comunaEncontrada
                        regionGps = regionEncontrada
                        estaBuscandoUbicacion = false
                    },
                    onError = { mensaje ->
                        errorUbicacion = mensaje
                        estaBuscandoUbicacion = false
                    }
                )
            } else {
                errorUbicacion = "Permiso de ubicación denegado"
                estaBuscandoUbicacion = false
                mostrarDialogoAjustes = true
            }
        }
    )

    LaunchedEffect(Unit) {
        estaBuscandoUbicacion = true
        errorUbicacion = null

        val tienePermisoFino = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val tienePermisoGrueso = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (tienePermisoFino || tienePermisoGrueso) {
            obtenerUbicacionSugerida(
                context = context,
                onResultado = { comunaEncontrada, regionEncontrada ->
                    comunaGps = comunaEncontrada
                    regionGps = regionEncontrada
                    estaBuscandoUbicacion = false
                },
                onError = { mensaje ->
                    errorUbicacion = mensaje
                    estaBuscandoUbicacion = false
                }
            )
        } else {
            lanzadorPermisos.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val cicloDeVida = LocalLifecycleOwner.current

    DisposableEffect(cicloDeVida) {
        val observador = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) {

                val tienePermisoFino = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                val tienePermisoGrueso = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

                if (tienePermisoFino || tienePermisoGrueso) {
                    estaBuscandoUbicacion = true
                    errorUbicacion = null

                    obtenerUbicacionSugerida(
                        context = context,
                        onResultado = { comunaEncontrada, regionEncontrada ->
                            comunaGps = comunaEncontrada
                            regionGps = regionEncontrada
                            estaBuscandoUbicacion = false
                        },
                        onError = { mensaje ->
                            errorUbicacion = mensaje
                            estaBuscandoUbicacion = false
                        }
                    )
                }
            }
        }

        cicloDeVida.lifecycle.addObserver(observador)

        onDispose {
            cicloDeVida.lifecycle.removeObserver(observador)
        }
    }

    fun validarNuevamente() {

        if (intentoRegistro) {

            registroViewModel
                .validarFormulario(
                    nombre = nombre,
                    rut = rut,
                    correo = correo,
                    contrasena = contrasena,
                    confirmarContrasena =
                        confirmarContrasena,
                    nombreEmprendimiento =
                        nombreEmprendimiento,
                    rubro = rubro,
                    region = regionGps,
                    comuna = comunaGps,
                    edad = edad,
                    genero = genero
                )
        }
    }


    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(NegroTextil)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(24.dp)
    ) {

        Spacer(
            modifier =
                Modifier.height(50.dp)
        )


        Text(
            text = "CREAR",
            color = BlancoTextil,
            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )


        Text(
            text = "CUENTA",
            color = TurquesaTextil,
            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        Text(
            text =
                "Completa tus datos para formar parte de la comunidad Textil Circular.",
            color = BlancoTextil,
            style =
                MaterialTheme
                    .typography
                    .bodyLarge
        )


        Spacer(
            modifier =
                Modifier.height(32.dp)
        )


        // =========================
        // DATOS PERSONALES
        // =========================

        Text(
            text =
                "Datos personales",
            color =
                TurquesaTextil,
            style =
                MaterialTheme
                    .typography
                    .titleMedium
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        // NOMBRE

        CampoRegistro(
            valor =
                nombre,

            onValorCambio = {

                nombre =
                    it

                errorRegistro =
                    null

                validarNuevamente()
            },

            etiqueta =
                "Nombre completo",

            error =
                registroViewModel
                    .errorNombre
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        // RUT

        OutlinedTextField(
            value =
                rut,

            onValueChange = {
                    nuevoValor ->

                rut =
                    nuevoValor
                        .uppercase()
                        .filter {

                            it.isDigit() ||
                                    it == 'K'
                        }
                        .take(9)

                errorRegistro =
                    null

                validarNuevamente()
            },

            modifier =
                Modifier
                    .fillMaxWidth(),

            label = {

                Text("RUT")
            },

            leadingIcon = {

                Icon(
                    imageVector =
                        Icons.Default.Person,

                    contentDescription =
                        "RUT"
                )
            },

            singleLine =
                true,

            isError =
                registroViewModel
                    .errorRut != null,

            supportingText = {

                registroViewModel
                    .errorRut
                    ?.let {

                        Text(
                            text =
                                it
                        )
                    }
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Ascii
                ),

            visualTransformation =
                RutVisualTransformation(),

            colors =
                coloresCampoRegistro()
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        // CORREO

        OutlinedTextField(
            value =
                correo,

            onValueChange = {

                correo =
                    it

                errorRegistro =
                    null

                validarNuevamente()
            },

            modifier =
                Modifier
                    .fillMaxWidth(),

            label = {

                Text(
                    "Correo electrónico"
                )
            },

            leadingIcon = {

                Icon(
                    imageVector =
                        Icons.Default.Email,

                    contentDescription =
                        "Correo electrónico"
                )
            },

            singleLine =
                true,

            isError =
                registroViewModel
                    .errorCorreo != null,

            supportingText = {

                registroViewModel
                    .errorCorreo
                    ?.let {

                        Text(
                            text =
                                it
                        )
                    }
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Email
                ),

            colors =
                coloresCampoRegistro()
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        // CONTRASEÑA

        OutlinedTextField(
            value =
                contrasena,

            onValueChange = {

                contrasena =
                    it

                errorRegistro =
                    null

                validarNuevamente()
            },

            modifier =
                Modifier
                    .fillMaxWidth(),

            label = {

                Text(
                    "Contraseña"
                )
            },

            leadingIcon = {

                Icon(
                    imageVector =
                        Icons.Default.Lock,

                    contentDescription =
                        "Contraseña"
                )
            },

            trailingIcon = {

                IconButton(
                    onClick = {

                        mostrarContrasena =
                            !mostrarContrasena
                    }
                ) {

                    Icon(
                        imageVector =
                            if (
                                mostrarContrasena
                            ) {

                                Icons.Default
                                    .VisibilityOff

                            } else {

                                Icons.Default
                                    .Visibility
                            },

                        contentDescription =
                            "Mostrar u ocultar contraseña"
                    )
                }
            },

            singleLine =
                true,

            isError =
                registroViewModel
                    .errorContrasena != null,

            supportingText = {

                registroViewModel
                    .errorContrasena
                    ?.let {

                        Text(
                            text =
                                it
                        )
                    }
            },

            visualTransformation =
                if (
                    mostrarContrasena
                ) {

                    VisualTransformation.None

                } else {

                    PasswordVisualTransformation()
                },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password
                ),

            colors =
                coloresCampoRegistro()
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        // CONFIRMAR CONTRASEÑA

        OutlinedTextField(
            value =
                confirmarContrasena,

            onValueChange = {

                confirmarContrasena =
                    it

                errorRegistro =
                    null

                validarNuevamente()
            },

            modifier =
                Modifier
                    .fillMaxWidth(),

            label = {

                Text(
                    "Confirmar contraseña"
                )
            },

            leadingIcon = {

                Icon(
                    imageVector =
                        Icons.Default.Lock,

                    contentDescription =
                        "Confirmar contraseña"
                )
            },

            trailingIcon = {

                IconButton(
                    onClick = {

                        mostrarConfirmarContrasena =
                            !mostrarConfirmarContrasena
                    }
                ) {

                    Icon(
                        imageVector =
                            if (
                                mostrarConfirmarContrasena
                            ) {

                                Icons.Default
                                    .VisibilityOff

                            } else {

                                Icons.Default
                                    .Visibility
                            },

                        contentDescription =
                            "Mostrar u ocultar confirmación de contraseña"
                    )
                }
            },

            singleLine =
                true,

            isError =
                registroViewModel
                    .errorConfirmarContrasena !=
                        null,

            supportingText = {

                registroViewModel
                    .errorConfirmarContrasena
                    ?.let {

                        Text(
                            text =
                                it
                        )
                    }
            },

            visualTransformation =
                if (
                    mostrarConfirmarContrasena
                ) {

                    VisualTransformation.None

                } else {

                    PasswordVisualTransformation()
                },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password
                ),

            colors =
                coloresCampoRegistro()
        )


        Spacer(
            modifier =
                Modifier.height(28.dp)
        )


        // =========================
        // DATOS DEL EMPRENDIMIENTO
        // =========================

        Text(
            text =
                "Datos del emprendimiento",

            color =
                TurquesaTextil,

            style =
                MaterialTheme
                    .typography
                    .titleMedium
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        CampoRegistro(
            valor =
                nombreEmprendimiento,

            onValorCambio = {

                nombreEmprendimiento =
                    it

                errorRegistro =
                    null

                validarNuevamente()
            },

            etiqueta =
                "Nombre del emprendimiento",

            error =
                registroViewModel
                    .errorNombreEmprendimiento
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        CampoRegistro(
            valor =
                rubro,

            onValorCambio = {

                rubro =
                    it

                errorRegistro =
                    null

                validarNuevamente()
            },

            etiqueta =
                "Rubro o actividad",

            error =
                registroViewModel
                    .errorRubro
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        // REGIÓN
        OutlinedTextField(
            value = if (estaBuscandoUbicacion) "Detectando región..." else if (regionGps.isEmpty()) "Región no detectada" else regionGps,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Región (Automática)") },
            readOnly = true,
            enabled = false,
            colors = coloresCampoRegistro()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // COMUNA
        OutlinedTextField(
            value = if (estaBuscandoUbicacion) "Detectando comuna..." else if (comunaGps.isEmpty()) "Comuna no detectada" else comunaGps,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Comuna (Automática)") },
            readOnly = true,
            enabled = false,
            colors = coloresCampoRegistro()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // MENSAJE DE ADVERTENCIA
        Text(
            text = "* Si esta no es tu ubicación, comunícate con un administrador.",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 4.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))


        // =========================
        // INFORMACIÓN ESTADÍSTICA
        // =========================

        Text(
            text =
                "Información estadística",

            color =
                TurquesaTextil,

            style =
                MaterialTheme
                    .typography
                    .titleMedium
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        // EDAD

        OutlinedTextField(
            value =
                edad,

            onValueChange = {

                edad =
                    it
                        .filter {
                                caracter ->

                            caracter.isDigit()
                        }
                        .take(3)

                errorRegistro =
                    null

                validarNuevamente()
            },

            modifier =
                Modifier
                    .fillMaxWidth(),

            label = {

                Text("Edad")
            },

            singleLine =
                true,

            isError =
                registroViewModel
                    .errorEdad != null,

            supportingText = {

                registroViewModel
                    .errorEdad
                    ?.let {

                        Text(
                            text =
                                it
                        )
                    }
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Number
                ),

            colors =
                coloresCampoRegistro()
        )


        Spacer(
            modifier =
                Modifier.height(24.dp)
        )


        // GÉNERO

        SelectorGenero(
            generoSeleccionado =
                genero,

            menuAbierto =
                menuGeneroAbierto,

            opciones =
                opcionesGenero,

            error =
                registroViewModel
                    .errorGenero,

            onAbrirMenu = {

                menuGeneroAbierto =
                    true
            },

            onCerrarMenu = {

                menuGeneroAbierto =
                    false
            },

            onGeneroSeleccionado = {

                genero =
                    it

                menuGeneroAbierto =
                    false

                errorRegistro =
                    null

                validarNuevamente()
            }
        )


        Spacer(
            modifier =
                Modifier.height(32.dp)
        )


        // =========================
        // ERROR DE REGISTRO
        // =========================

        errorRegistro
            ?.let {

                Text(
                    text =
                        it,

                    color =
                        RojoErrorTextil,

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium
                )


                Spacer(
                    modifier =
                        Modifier
                            .height(12.dp)
                )
            }


        // =========================
        // CREAR CUENTA
        // =========================

        Button(
            onClick = {

                intentoRegistro =
                    true

                errorRegistro =
                    null


                val formularioValido =
                    registroViewModel
                        .validarFormulario(

                            nombre =
                                nombre,

                            rut =
                                rut,

                            correo =
                                correo,

                            contrasena =
                                contrasena,

                            confirmarContrasena =
                                confirmarContrasena,

                            nombreEmprendimiento =
                                nombreEmprendimiento,

                            rubro =
                                rubro,

                            region =
                                regionGps,

                            comuna =
                                comunaGps,

                            edad =
                                edad,

                            genero =
                                genero
                        )


                if (
                    formularioValido
                ) {

                    registrando =
                        true


                    scope.launch {


                        val correoNormalizado =
                            correo
                                .trim()
                                .lowercase()


                        val rutNormalizado =
                            rut
                                .trim()
                                .uppercase()


                        // =========================
                        // CORREO ADMIN RESERVADO
                        // =========================

                        if (
                            correoNormalizado ==
                            "admin@textilcircular.cl"
                        ) {

                            errorRegistro =
                                "Este correo está reservado para el administrador"

                            registrando =
                                false

                            return@launch
                        }


                        // =========================
                        // VERIFICAR CORREO EN ROOM
                        // =========================

                        val correoExiste =
                            dao.existeCorreo(
                                correoNormalizado
                            ) > 0


                        if (
                            correoExiste
                        ) {

                            errorRegistro =
                                "No se puede crear la cuenta porque ya existe una cuenta registrada con ese correo"

                            registrando =
                                false

                            return@launch
                        }


                        // =========================
                        // VERIFICAR RUT EN ROOM
                        // =========================

                        val rutExiste =
                            dao.existeRut(
                                rutNormalizado
                            ) > 0


                        if (
                            rutExiste
                        ) {

                            errorRegistro =
                                "No se puede crear la cuenta porque ya existe una cuenta registrada con ese RUT"

                            registrando =
                                false

                            return@launch
                        }


                        // =========================
                        // CREAR CUENTA EN FIREBASE
                        // =========================

                        auth
                            .createUserWithEmailAndPassword(
                                correoNormalizado,
                                contrasena
                            )
                            .addOnCompleteListener {
                                    task ->


                                if (
                                    task.isSuccessful
                                ) {


                                    val usuarioFirebase =
                                        auth.currentUser


                                    scope.launch {

                                        try {


                                            // =========================
                                            // GUARDAR DATOS EN ROOM
                                            // =========================

                                            val nuevaEmprendedora =
                                                EmprendedoraEntity(

                                                    rut =
                                                        rutNormalizado,

                                                    nombre =
                                                        nombre
                                                            .trim(),

                                                    correo =
                                                        correoNormalizado,


                                                    // TEMPORAL:
                                                    // SE MANTIENE HASTA
                                                    // MIGRAR TODO EL LOGIN
                                                    // A FIREBASE.

                                                    contrasena =
                                                        contrasena,

                                                    nombreEmprendimiento =
                                                        nombreEmprendimiento
                                                            .trim(),

                                                    rubro =
                                                        rubro
                                                            .trim(),

                                                    region =
                                                        regionGps,

                                                    comuna =
                                                        comunaGps,

                                                    edad =
                                                        edad
                                                            .toInt(),

                                                    genero =
                                                        genero,

                                                    rol =
                                                        "EMPRENDEDORA",

                                                    estado =
                                                        "PENDIENTE",

                                                    motivoRechazo =
                                                        null
                                                )


                                            dao
                                                .insertarEmprendedora(
                                                    nuevaEmprendedora
                                                )


                                            // FIREBASE INICIA SESIÓN
                                            // AUTOMÁTICAMENTE AL CREAR
                                            // UNA CUENTA.
                                            //
                                            // COMO LA CUENTA ESTÁ
                                            // PENDIENTE, LA CERRAMOS.

                                            auth.signOut()


                                            errorRegistro =
                                                null

                                            registrando =
                                                false


                                            // ENVIAMOS EL CORREO
                                            // A MAINACTIVITY.

                                            onRegistrar(
                                                correoNormalizado
                                            )


                                        } catch (
                                            e: Exception
                                        ) {


                                            // SI ROOM FALLA DESPUÉS
                                            // DE CREAR FIREBASE,
                                            // INTENTAMOS ELIMINAR
                                            // LA CUENTA FIREBASE.

                                            usuarioFirebase
                                                ?.delete()
                                                ?.addOnCompleteListener {

                                                    auth.signOut()
                                                }


                                            registrando =
                                                false


                                            errorRegistro =
                                                "No se pudo guardar la cuenta. Inténtalo nuevamente."
                                        }
                                    }


                                } else {


                                    registrando =
                                        false


                                    errorRegistro =
                                        when (
                                            task.exception
                                        ) {


                                            is FirebaseAuthUserCollisionException ->

                                                "Ya existe una cuenta registrada con este correo"


                                            is FirebaseAuthWeakPasswordException ->

                                                "La contraseña no cumple los requisitos de seguridad"


                                            is FirebaseAuthInvalidCredentialsException ->

                                                "El correo electrónico no es válido"


                                            else ->

                                                "No se pudo crear la cuenta. Revisa tu conexión e inténtalo nuevamente."
                                        }
                                }
                            }
                    }
                }
            },

            enabled =
                !registrando && !estaBuscandoUbicacion && errorUbicacion == null && regionGps.isNotBlank() && comunaGps.isNotBlank(),

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),

            colors =
                ButtonDefaults
                    .buttonColors(

                        containerColor =
                            TurquesaTextil,

                        contentColor =
                            NegroTextil,

                        disabledContainerColor =
                            TurquesaTextil.copy(alpha = 0.4f),

                        disabledContentColor =
                            NegroTextil.copy(alpha = 0.6f)
                    )
        ) {

            Text(
                text =
                    if (
                        registrando
                    ) {

                        "CREANDO CUENTA..."

                    } else {

                        "CREAR CUENTA"
                    },

                style =
                    MaterialTheme
                        .typography
                        .labelLarge
            )
        }


        Spacer(
            modifier =
                Modifier
                    .height(12.dp)
        )


        TextButton(
            onClick =
                onVolver,

            modifier =
                Modifier
                    .fillMaxWidth()
        ) {

            Text(
                text =
                    "Volver",

                color =
                    TurquesaTextil
            )
        }


        Spacer(
            modifier =
                Modifier
                    .height(30.dp)
        )
    }
}


// =========================
// CAMPO REGISTRO
// =========================

@Composable
fun CampoRegistro(
    valor: String,
    onValorCambio: (String) -> Unit,
    etiqueta: String,
    error: String? = null
) {

    OutlinedTextField(
        value =
            valor,

        onValueChange =
            onValorCambio,

        modifier =
            Modifier
                .fillMaxWidth(),

        label = {

            Text(
                etiqueta
            )
        },

        leadingIcon = {

            Icon(
                imageVector =
                    Icons.Default.Person,

                contentDescription =
                    null
            )
        },

        singleLine =
            true,

        isError =
            error != null,

        supportingText = {

            error
                ?.let {

                    Text(
                        text =
                            it
                    )
                }
        },

        colors =
            coloresCampoRegistro()
    )
}


// =========================
// SELECTOR REGIÓN
// =========================

@Composable
fun SelectorRegion(
    regionSeleccionada: String,
    busqueda: String,
    menuAbierto: Boolean,
    regiones: List<String>,
    error: String?,
    onAbrirMenu: () -> Unit,
    onCerrarMenu: () -> Unit,
    onBusquedaCambio: (String) -> Unit,
    onRegionSeleccionada: (String) -> Unit
) {

    val regionesFiltradas =
        regiones
            .filter {

                it.contains(
                    busqueda,
                    ignoreCase =
                        true
                )
            }


    Column(
        modifier =
            Modifier
                .fillMaxWidth()
    ) {

        Text(
            text =
                "Región",

            color =
                BlancoTextil,

            style =
                MaterialTheme
                    .typography
                    .bodyLarge
        )


        Spacer(
            modifier =
                Modifier
                    .height(8.dp)
        )


        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
        ) {

            OutlinedButton(
                onClick =
                    onAbrirMenu,

                modifier =
                    Modifier
                        .fillMaxWidth(),

                border =
                    BorderStroke(

                        width =
                            1.dp,

                        color =
                            if (
                                error !=
                                null
                            ) {

                                RojoErrorTextil

                            } else {

                                Color.Gray
                            }
                    )
            ) {

                Text(
                    text =
                        if (
                            regionSeleccionada
                                .isEmpty()
                        ) {

                            "Seleccionar región"

                        } else {

                            regionSeleccionada
                        },

                    color =
                        BlancoTextil
                )
            }


            DropdownMenu(
                expanded =
                    menuAbierto,

                onDismissRequest =
                    onCerrarMenu,

                modifier =
                    Modifier
                        .fillMaxWidth(0.90f)
                        .heightIn(
                            max =
                                400.dp
                        )
            ) {

                OutlinedTextField(
                    value =
                        busqueda,

                    onValueChange =
                        onBusquedaCambio,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),

                    label = {

                        Text(
                            "Buscar región"
                        )
                    },

                    singleLine =
                        true
                )


                regionesFiltradas
                    .forEach {
                            region ->

                        DropdownMenuItem(
                            text = {

                                Text(
                                    region
                                )
                            },

                            onClick = {

                                onRegionSeleccionada(
                                    region
                                )
                            }
                        )
                    }


                if (
                    regionesFiltradas
                        .isEmpty()
                ) {

                    Text(
                        text =
                            "No se encontraron regiones",

                        modifier =
                            Modifier
                                .padding(16.dp)
                    )
                }
            }
        }


        error
            ?.let {

                Text(
                    text =
                        it,

                    color =
                        RojoErrorTextil,

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,

                    modifier =
                        Modifier
                            .padding(
                                start =
                                    16.dp,

                                top =
                                    4.dp
                            )
                )
            }
    }
}


// =========================
// SELECTOR COMUNA
// =========================

@Composable
fun SelectorComuna(
    comunaSeleccionada: String,
    regionSeleccionada: String,
    busqueda: String,
    menuAbierto: Boolean,
    comunas: List<String>,
    error: String?,
    onAbrirMenu: () -> Unit,
    onCerrarMenu: () -> Unit,
    onBusquedaCambio: (String) -> Unit,
    onComunaSeleccionada: (String) -> Unit
) {

    val comunasFiltradas =
        comunas
            .filter {

                it.contains(
                    busqueda,
                    ignoreCase =
                        true
                )
            }


    val regionSeleccionadaValida =
        regionSeleccionada
            .isNotEmpty()


    Column(
        modifier =
            Modifier
                .fillMaxWidth()
    ) {

        Text(
            text =
                "Comuna",

            color =
                BlancoTextil,

            style =
                MaterialTheme
                    .typography
                    .bodyLarge
        )


        Spacer(
            modifier =
                Modifier
                    .height(8.dp)
        )


        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
        ) {

            OutlinedButton(
                onClick =
                    onAbrirMenu,

                enabled =
                    regionSeleccionadaValida,

                modifier =
                    Modifier
                        .fillMaxWidth(),

                border =
                    BorderStroke(

                        width =
                            1.dp,

                        color =
                            if (
                                error !=
                                null
                            ) {

                                RojoErrorTextil

                            } else {

                                Color.Gray
                            }
                    )
            ) {

                Text(
                    text =
                        when {

                            !regionSeleccionadaValida ->

                                "Primero selecciona una región"


                            comunaSeleccionada
                                .isEmpty() ->

                                "Seleccionar comuna"


                            else ->

                                comunaSeleccionada
                        },

                    color =
                        if (
                            regionSeleccionadaValida
                        ) {

                            BlancoTextil

                        } else {

                            Color.Gray
                        }
                )
            }


            DropdownMenu(
                expanded =
                    menuAbierto &&
                            regionSeleccionadaValida,

                onDismissRequest =
                    onCerrarMenu,

                modifier =
                    Modifier
                        .fillMaxWidth(0.90f)
                        .heightIn(
                            max =
                                400.dp
                        )
            ) {

                OutlinedTextField(
                    value =
                        busqueda,

                    onValueChange =
                        onBusquedaCambio,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),

                    label = {

                        Text(
                            "Buscar comuna"
                        )
                    },

                    singleLine =
                        true
                )


                comunasFiltradas
                    .forEach {
                            comuna ->

                        DropdownMenuItem(
                            text = {

                                Text(
                                    comuna
                                )
                            },

                            onClick = {

                                onComunaSeleccionada(
                                    comuna
                                )
                            }
                        )
                    }


                if (
                    comunasFiltradas
                        .isEmpty()
                ) {

                    Text(
                        text =
                            "No se encontraron comunas",

                        modifier =
                            Modifier
                                .padding(16.dp)
                    )
                }
            }
        }


        error
            ?.let {

                Text(
                    text =
                        it,

                    color =
                        RojoErrorTextil,

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,

                    modifier =
                        Modifier
                            .padding(
                                start =
                                    16.dp,

                                top =
                                    4.dp
                            )
                )
            }
    }
}


// =========================
// SELECTOR GÉNERO
// =========================

@Composable
fun SelectorGenero(
    generoSeleccionado: String,
    menuAbierto: Boolean,
    opciones: List<String>,
    error: String?,
    onAbrirMenu: () -> Unit,
    onCerrarMenu: () -> Unit,
    onGeneroSeleccionado: (String) -> Unit
) {

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
    ) {

        Text(
            text =
                "Género",

            color =
                BlancoTextil,

            style =
                MaterialTheme
                    .typography
                    .bodyLarge
        )


        Spacer(
            modifier =
                Modifier
                    .height(8.dp)
        )


        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
        ) {

            OutlinedButton(
                onClick =
                    onAbrirMenu,

                modifier =
                    Modifier
                        .fillMaxWidth(),

                border =
                    BorderStroke(

                        width =
                            1.dp,

                        color =
                            if (
                                error !=
                                null
                            ) {

                                RojoErrorTextil

                            } else {

                                Color.Gray
                            }
                    )
            ) {

                Text(
                    text =
                        if (
                            generoSeleccionado
                                .isEmpty()
                        ) {

                            "Seleccionar género"

                        } else {

                            generoSeleccionado
                        },

                    color =
                        BlancoTextil
                )
            }


            DropdownMenu(
                expanded =
                    menuAbierto,

                onDismissRequest =
                    onCerrarMenu
            ) {

                opciones
                    .forEach {
                            opcion ->

                        DropdownMenuItem(
                            text = {

                                Text(
                                    opcion
                                )
                            },

                            onClick = {

                                onGeneroSeleccionado(
                                    opcion
                                )
                            }
                        )
                    }
            }
        }


        error
            ?.let {

                Text(
                    text =
                        it,

                    color =
                        RojoErrorTextil,

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,

                    modifier =
                        Modifier
                            .padding(
                                start =
                                    16.dp,

                                top =
                                    4.dp
                            )
                )
            }
    }
}


// =========================
// COLORES DE CAMPOS
// =========================

@Composable
fun coloresCampoRegistro() =
    OutlinedTextFieldDefaults
        .colors(

            focusedTextColor =
                BlancoTextil,

            unfocusedTextColor =
                BlancoTextil,

            errorTextColor =
                BlancoTextil,

            disabledTextColor =
                Color.LightGray,

            disabledBorderColor =
                Color.DarkGray,

            disabledLabelColor =
                Color.Gray,

            focusedBorderColor =
                TurquesaTextil,

            unfocusedBorderColor =
                Color.Gray,

            errorBorderColor =
                RojoErrorTextil,

            focusedLabelColor =
                TurquesaTextil,

            unfocusedLabelColor =
                Color.LightGray,

            errorLabelColor =
                RojoErrorTextil,

            focusedLeadingIconColor =
                TurquesaTextil,

            unfocusedLeadingIconColor =
                Color.LightGray,

            errorLeadingIconColor =
                RojoErrorTextil,

            focusedTrailingIconColor =
                TurquesaTextil,

            unfocusedTrailingIconColor =
                Color.LightGray,

            errorTrailingIconColor =
                RojoErrorTextil,

            cursorColor =
                TurquesaTextil,

            errorCursorColor =
                TurquesaTextil,

            errorSupportingTextColor =
                RojoErrorTextil
        )


// =========================
// FORMATO VISUAL DEL RUT
// =========================

class RutVisualTransformation :
    VisualTransformation {

    override fun filter(
        text: AnnotatedString
    ): TransformedText {

        val rut =
            text.text


        if (
            rut.length <
            8
        ) {

            return TransformedText(

                text =
                    AnnotatedString(
                        rut
                    ),

                offsetMapping =
                    OffsetMapping.Identity
            )
        }


        val cuerpo =
            rut.dropLast(1)


        val digitoVerificador =
            rut.last()


        val rutFormateado =
            "$cuerpo-$digitoVerificador"


        val posicionGuion =
            cuerpo.length


        val offsetMapping =
            object :
                OffsetMapping {


                override fun originalToTransformed(
                    offset: Int
                ): Int {

                    return if (
                        offset <
                        posicionGuion
                    ) {

                        offset

                    } else {

                        offset + 1
                    }
                }


                override fun transformedToOriginal(
                    offset: Int
                ): Int {

                    return if (
                        offset <=
                        posicionGuion
                    ) {

                        offset

                    } else {

                        offset - 1
                    }
                }
            }


        return TransformedText(

            text =
                AnnotatedString(
                    rutFormateado
                ),

            offsetMapping =
                offsetMapping
        )
    }
}