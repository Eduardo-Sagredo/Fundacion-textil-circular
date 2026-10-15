package cl.textilcircular.view

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cl.textilcircular.ui.theme.BlancoTextil
import cl.textilcircular.ui.theme.NegroTextil
import cl.textilcircular.ui.theme.TurquesaTextil


// REGIONES DE CHILE

private val REGIONES_CHILE = listOf(
    "Arica y Parinacota",
    "Tarapacá",
    "Antofagasta",
    "Atacama",
    "Coquimbo",
    "Valparaíso",
    "Metropolitana de Santiago",
    "Libertador General Bernardo O'Higgins",
    "Maule",
    "Ñuble",
    "Biobío",
    "La Araucanía",
    "Los Ríos",
    "Los Lagos",
    "Aysén del General Carlos Ibáñez del Campo",
    "Magallanes y de la Antártica Chilena"
)


// COMUNAS SEGÚN REGIÓN

private val COMUNAS_POR_REGION = mapOf(

    "Arica y Parinacota" to listOf(
        "Arica",
        "Camarones",
        "General Lagos",
        "Putre"
    ),

    "Tarapacá" to listOf(
        "Alto Hospicio",
        "Camiña",
        "Colchane",
        "Huara",
        "Iquique",
        "Pica",
        "Pozo Almonte"
    ),

    "Antofagasta" to listOf(
        "Antofagasta",
        "Calama",
        "María Elena",
        "Mejillones",
        "Ollagüe",
        "San Pedro de Atacama",
        "Sierra Gorda",
        "Taltal",
        "Tocopilla"
    ),

    "Atacama" to listOf(
        "Alto del Carmen",
        "Caldera",
        "Chañaral",
        "Copiapó",
        "Diego de Almagro",
        "Freirina",
        "Huasco",
        "Tierra Amarilla",
        "Vallenar"
    ),

    "Coquimbo" to listOf(
        "Andacollo",
        "Canela",
        "Combarbalá",
        "Coquimbo",
        "Illapel",
        "La Higuera",
        "La Serena",
        "Los Vilos",
        "Monte Patria",
        "Ovalle",
        "Paihuano",
        "Punitaqui",
        "Río Hurtado",
        "Salamanca",
        "Vicuña"
    ),

    "Valparaíso" to listOf(
        "Algarrobo",
        "Cabildo",
        "Calle Larga",
        "Cartagena",
        "Casablanca",
        "Catemu",
        "Concón",
        "El Quisco",
        "El Tabo",
        "Hijuelas",
        "Isla de Pascua",
        "Juan Fernández",
        "La Calera",
        "La Cruz",
        "La Ligua",
        "Limache",
        "Llaillay",
        "Los Andes",
        "Nogales",
        "Olmué",
        "Panquehue",
        "Papudo",
        "Petorca",
        "Puchuncaví",
        "Putaendo",
        "Quillota",
        "Quilpué",
        "Quintero",
        "Rinconada",
        "San Antonio",
        "San Esteban",
        "San Felipe",
        "Santa María",
        "Santo Domingo",
        "Valparaíso",
        "Villa Alemana",
        "Viña del Mar",
        "Zapallar"
    ),

    "Metropolitana de Santiago" to listOf(
        "Alhué",
        "Buin",
        "Calera de Tango",
        "Cerrillos",
        "Cerro Navia",
        "Colina",
        "Conchalí",
        "Curacaví",
        "El Bosque",
        "El Monte",
        "Estación Central",
        "Huechuraba",
        "Independencia",
        "Isla de Maipo",
        "La Cisterna",
        "La Florida",
        "La Granja",
        "Lampa",
        "La Pintana",
        "La Reina",
        "Las Condes",
        "Lo Barnechea",
        "Lo Espejo",
        "Lo Prado",
        "Macul",
        "Maipú",
        "María Pinto",
        "Melipilla",
        "Ñuñoa",
        "Padre Hurtado",
        "Paine",
        "Pedro Aguirre Cerda",
        "Peñaflor",
        "Peñalolén",
        "Pirque",
        "Providencia",
        "Pudahuel",
        "Puente Alto",
        "Quilicura",
        "Quinta Normal",
        "Recoleta",
        "Renca",
        "San Bernardo",
        "San Joaquín",
        "San José de Maipo",
        "San Miguel",
        "San Pedro",
        "San Ramón",
        "Santiago",
        "Talagante",
        "Tiltil",
        "Vitacura"
    ),

    "Libertador General Bernardo O'Higgins" to listOf(
        "Chépica",
        "Chimbarongo",
        "Codegua",
        "Coinco",
        "Coltauco",
        "Doñihue",
        "Graneros",
        "La Estrella",
        "Las Cabras",
        "Litueche",
        "Lolol",
        "Machalí",
        "Malloa",
        "Marchihue",
        "Mostazal",
        "Nancagua",
        "Navidad",
        "Olivar",
        "Palmilla",
        "Paredones",
        "Peralillo",
        "Peumo",
        "Pichidegua",
        "Pichilemu",
        "Placilla",
        "Pumanque",
        "Quinta de Tilcoco",
        "Rancagua",
        "Rengo",
        "Requínoa",
        "San Fernando",
        "Santa Cruz",
        "San Vicente"
    ),

    "Maule" to listOf(
        "Cauquenes",
        "Chanco",
        "Colbún",
        "Constitución",
        "Curepto",
        "Curicó",
        "Empedrado",
        "Hualañé",
        "Licantén",
        "Linares",
        "Longaví",
        "Maule",
        "Molina",
        "Parral",
        "Pelarco",
        "Pelluhue",
        "Pencahue",
        "Rauco",
        "Retiro",
        "Río Claro",
        "Romeral",
        "Sagrada Familia",
        "San Clemente",
        "San Javier",
        "San Rafael",
        "Talca",
        "Teno",
        "Vichuquén",
        "Villa Alegre",
        "Yerbas Buenas"
    ),

    "Ñuble" to listOf(
        "Bulnes",
        "Chillán",
        "Chillán Viejo",
        "Cobquecura",
        "Coelemu",
        "Coihueco",
        "El Carmen",
        "Ninhue",
        "Ñiquén",
        "Pemuco",
        "Pinto",
        "Portezuelo",
        "Quillón",
        "Quirihue",
        "Ránquil",
        "San Carlos",
        "San Fabián",
        "San Ignacio",
        "San Nicolás",
        "Treguaco",
        "Yungay"
    ),

    "Biobío" to listOf(
        "Alto Biobío",
        "Antuco",
        "Arauco",
        "Cabrero",
        "Cañete",
        "Chiguayante",
        "Concepción",
        "Contulmo",
        "Coronel",
        "Curanilahue",
        "Florida",
        "Hualpén",
        "Hualqui",
        "Laja",
        "Lebu",
        "Los Alamos",
        "Los Angeles",
        "Lota",
        "Mulchén",
        "Nacimiento",
        "Negrete",
        "Penco",
        "Quilaco",
        "Quilleco",
        "San Pedro de la Paz",
        "San Rosendo",
        "Santa Bárbara",
        "Santa Juana",
        "Talcahuano",
        "Tirúa",
        "Tomé",
        "Tucapel",
        "Yumbel"
    ),

    "La Araucanía" to listOf(
        "Angol",
        "Carahue",
        "Cholchol",
        "Collipulli",
        "Cunco",
        "Curacautín",
        "Curarrehue",
        "Ercilla",
        "Freire",
        "Galvarino",
        "Gorbea",
        "Lautaro",
        "Loncoche",
        "Lonquimay",
        "Los Sauces",
        "Lumaco",
        "Melipeuco",
        "Nueva Imperial",
        "Padre Las Casas",
        "Perquenco",
        "Pitrufquén",
        "Pucón",
        "Purén",
        "Renaico",
        "Saavedra",
        "Temuco",
        "Teodoro Schmidt",
        "Toltén",
        "Traiguén",
        "Victoria",
        "Vilcún",
        "Villarrica"
    ),

    "Los Ríos" to listOf(
        "Corral",
        "Futrono",
        "Lago Ranco",
        "Lanco",
        "La Unión",
        "Los Lagos",
        "Máfil",
        "Mariquina",
        "Paillaco",
        "Panguipulli",
        "Río Bueno",
        "Valdivia"
    ),

    "Los Lagos" to listOf(
        "Ancud",
        "Calbuco",
        "Castro",
        "Chaitén",
        "Chonchi",
        "Cochamó",
        "Curaco de Vélez",
        "Dalcahue",
        "Fresia",
        "Frutillar",
        "Futaleufú",
        "Hualaihué",
        "Llanquihue",
        "Los Muermos",
        "Maullín",
        "Osorno",
        "Palena",
        "Puerto Montt",
        "Puerto Octay",
        "Puerto Varas",
        "Puqueldón",
        "Purranque",
        "Puyehue",
        "Queilén",
        "Quellón",
        "Quemchi",
        "Quinchao",
        "Río Negro",
        "San Juan de la Costa",
        "San Pablo"
    ),

    "Aysén del General Carlos Ibáñez del Campo" to listOf(
        "Aysén",
        "Chile Chico",
        "Cisnes",
        "Cochrane",
        "Coyhaique",
        "Guaitecas",
        "Lago Verde",
        "O'Higgins",
        "Río Ibáñez",
        "Tortel"
    ),

    "Magallanes y de la Antártica Chilena" to listOf(
        "Antártica",
        "Cabo de Hornos",
        "Laguna Blanca",
        "Natales",
        "Porvenir",
        "Primavera",
        "Punta Arenas",
        "Río Verde",
        "San Gregorio",
        "Timaukel",
        "Torres del Paine"
    )
)


@Composable
fun PantallaRegistro(
    modifier: Modifier = Modifier,
    onRegistrar: () -> Unit = {},
    onVolver: () -> Unit = {}
) {

    var nombre by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }

    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    var nombreEmprendimiento by remember { mutableStateOf("") }
    var rubro by remember { mutableStateOf("") }

    var region by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }

    var edad by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }

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

    val opcionesGenero = listOf(
        "Femenino",
        "Masculino",
        "No binario",
        "Prefiero no responder"
    )

    val comunasDisponibles =
        COMUNAS_POR_REGION[region] ?: emptyList()


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NegroTextil)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Spacer(
            modifier = Modifier.height(50.dp)
        )

        Text(
            text = "CREAR",
            color = BlancoTextil,
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "CUENTA",
            color = TurquesaTextil,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Completa tus datos para formar parte de la comunidad Textil Circular.",
            color = BlancoTextil,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )


        // DATOS PERSONALES

        Text(
            text = "Datos personales",
            color = TurquesaTextil,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        CampoRegistro(
            valor = nombre,
            onValorCambio = {
                nombre = it
            },
            etiqueta = "Nombre completo"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // RUT

        OutlinedTextField(
            value = rut,
            onValueChange = { nuevoValor ->

                rut = nuevoValor
                    .uppercase()
                    .filter {
                        it.isDigit() || it == 'K'
                    }
                    .take(9)
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("RUT")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "RUT"
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii
            ),
            visualTransformation = RutVisualTransformation(),
            colors = coloresCampoRegistro()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // CORREO

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
            colors = coloresCampoRegistro()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // CONTRASEÑA

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
                        contentDescription = "Mostrar u ocultar contraseña"
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
            colors = coloresCampoRegistro()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // CONFIRMAR CONTRASEÑA

        OutlinedTextField(
            value = confirmarContrasena,
            onValueChange = {
                confirmarContrasena = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Confirmar contraseña")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Confirmar contraseña"
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
                        imageVector = if (mostrarConfirmarContrasena) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription =
                            "Mostrar u ocultar confirmación de contraseña"
                    )
                }
            },
            singleLine = true,
            visualTransformation =
                if (mostrarConfirmarContrasena) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            colors = coloresCampoRegistro()
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )


        // DATOS DEL EMPRENDIMIENTO

        Text(
            text = "Datos del emprendimiento",
            color = TurquesaTextil,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        CampoRegistro(
            valor = nombreEmprendimiento,
            onValorCambio = {
                nombreEmprendimiento = it
            },
            etiqueta = "Nombre del emprendimiento"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        CampoRegistro(
            valor = rubro,
            onValorCambio = {
                rubro = it
            },
            etiqueta = "Rubro o actividad"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // REGIÓN

        SelectorRegion(
            regionSeleccionada = region,
            busqueda = busquedaRegion,
            menuAbierto = menuRegionAbierto,
            regiones = REGIONES_CHILE,

            onAbrirMenu = {
                menuRegionAbierto = true
                busquedaRegion = ""
            },

            onCerrarMenu = {
                menuRegionAbierto = false
                busquedaRegion = ""
            },

            onBusquedaCambio = {
                busquedaRegion = it
            },

            onRegionSeleccionada = { nuevaRegion ->

                // Si cambia de región,
                // también borramos la comuna anterior.

                if (region != nuevaRegion) {
                    comuna = ""
                }

                region = nuevaRegion

                menuRegionAbierto = false
                busquedaRegion = ""

                menuComunaAbierto = false
                busquedaComuna = ""
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // COMUNA SEGÚN REGIÓN

        SelectorComuna(
            comunaSeleccionada = comuna,
            regionSeleccionada = region,
            busqueda = busquedaComuna,
            menuAbierto = menuComunaAbierto,
            comunas = comunasDisponibles,

            onAbrirMenu = {
                menuComunaAbierto = true
                busquedaComuna = ""
            },

            onCerrarMenu = {
                menuComunaAbierto = false
                busquedaComuna = ""
            },

            onBusquedaCambio = {
                busquedaComuna = it
            },

            onComunaSeleccionada = {
                comuna = it
                menuComunaAbierto = false
                busquedaComuna = ""
            }
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )


        // INFORMACIÓN ESTADÍSTICA

        Text(
            text = "Información estadística",
            color = TurquesaTextil,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // EDAD

        OutlinedTextField(
            value = edad,
            onValueChange = {
                edad = it
                    .filter { caracter ->
                        caracter.isDigit()
                    }
                    .take(3)
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Edad")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            colors = coloresCampoRegistro()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // GÉNERO

        SelectorGenero(
            generoSeleccionado = genero,
            menuAbierto = menuGeneroAbierto,
            opciones = opcionesGenero,

            onAbrirMenu = {
                menuGeneroAbierto = true
            },

            onCerrarMenu = {
                menuGeneroAbierto = false
            },

            onGeneroSeleccionado = {
                genero = it
                menuGeneroAbierto = false
            }
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )


        // CREAR CUENTA

        Button(
            onClick = onRegistrar,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = TurquesaTextil,
                contentColor = NegroTextil
            )
        ) {

            Text(
                text = "CREAR CUENTA",
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // VOLVER

        TextButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Volver",
                color = TurquesaTextil
            )
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )
    }
}


// CAMPO NORMAL

@Composable
fun CampoRegistro(
    valor: String,
    onValorCambio: (String) -> Unit,
    etiqueta: String
) {

    OutlinedTextField(
        value = valor,
        onValueChange = onValorCambio,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(etiqueta)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        },
        singleLine = true,
        colors = coloresCampoRegistro()
    )
}


// SELECTOR REGIÓN

@Composable
fun SelectorRegion(
    regionSeleccionada: String,
    busqueda: String,
    menuAbierto: Boolean,
    regiones: List<String>,
    onAbrirMenu: () -> Unit,
    onCerrarMenu: () -> Unit,
    onBusquedaCambio: (String) -> Unit,
    onRegionSeleccionada: (String) -> Unit
) {

    val regionesFiltradas = regiones.filter {

        it.contains(
            busqueda,
            ignoreCase = true
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Región",
            color = BlancoTextil,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedButton(
                onClick = onAbrirMenu,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        if (regionSeleccionada.isEmpty()) {
                            "Seleccionar región"
                        } else {
                            regionSeleccionada
                        },
                    color = BlancoTextil
                )
            }

            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = onCerrarMenu,
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .heightIn(max = 400.dp)
            ) {

                OutlinedTextField(
                    value = busqueda,
                    onValueChange = onBusquedaCambio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = {
                        Text("Buscar región")
                    },
                    singleLine = true
                )

                regionesFiltradas.forEach { region ->

                    DropdownMenuItem(
                        text = {
                            Text(region)
                        },
                        onClick = {
                            onRegionSeleccionada(region)
                        }
                    )
                }

                if (regionesFiltradas.isEmpty()) {

                    Text(
                        text = "No se encontraron regiones",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}


// SELECTOR COMUNA

@Composable
fun SelectorComuna(
    comunaSeleccionada: String,
    regionSeleccionada: String,
    busqueda: String,
    menuAbierto: Boolean,
    comunas: List<String>,
    onAbrirMenu: () -> Unit,
    onCerrarMenu: () -> Unit,
    onBusquedaCambio: (String) -> Unit,
    onComunaSeleccionada: (String) -> Unit
) {

    val comunasFiltradas = comunas.filter {

        it.contains(
            busqueda,
            ignoreCase = true
        )
    }

    val regionSeleccionadaValida =
        regionSeleccionada.isNotEmpty()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Comuna",
            color = BlancoTextil,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedButton(
                onClick = onAbrirMenu,
                enabled = regionSeleccionadaValida,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = when {

                        !regionSeleccionadaValida ->
                            "Primero selecciona una región"

                        comunaSeleccionada.isEmpty() ->
                            "Seleccionar comuna"

                        else ->
                            comunaSeleccionada
                    },
                    color =
                        if (regionSeleccionadaValida) {
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
                onDismissRequest = onCerrarMenu,
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .heightIn(max = 400.dp)
            ) {

                OutlinedTextField(
                    value = busqueda,
                    onValueChange = onBusquedaCambio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = {
                        Text("Buscar comuna")
                    },
                    singleLine = true
                )

                comunasFiltradas.forEach { comuna ->

                    DropdownMenuItem(
                        text = {
                            Text(comuna)
                        },
                        onClick = {
                            onComunaSeleccionada(comuna)
                        }
                    )
                }

                if (comunasFiltradas.isEmpty()) {

                    Text(
                        text = "No se encontraron comunas",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}


// SELECTOR GÉNERO

@Composable
fun SelectorGenero(
    generoSeleccionado: String,
    menuAbierto: Boolean,
    opciones: List<String>,
    onAbrirMenu: () -> Unit,
    onCerrarMenu: () -> Unit,
    onGeneroSeleccionado: (String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Género",
            color = BlancoTextil,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedButton(
                onClick = onAbrirMenu,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        if (generoSeleccionado.isEmpty()) {
                            "Seleccionar género"
                        } else {
                            generoSeleccionado
                        },
                    color = BlancoTextil
                )
            }

            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = onCerrarMenu
            ) {

                opciones.forEach { opcion ->

                    DropdownMenuItem(
                        text = {
                            Text(opcion)
                        },
                        onClick = {
                            onGeneroSeleccionado(opcion)
                        }
                    )
                }
            }
        }
    }
}


// COLORES DE LOS CAMPOS

@Composable
fun coloresCampoRegistro() =
    OutlinedTextFieldDefaults.colors(

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


// FORMATO VISUAL DEL RUT

class RutVisualTransformation : VisualTransformation {

    override fun filter(
        text: AnnotatedString
    ): TransformedText {

        val rut = text.text

        if (rut.length < 8) {

            return TransformedText(
                text = AnnotatedString(rut),
                offsetMapping = OffsetMapping.Identity
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
            object : OffsetMapping {

                override fun originalToTransformed(
                    offset: Int
                ): Int {

                    return if (
                        offset < posicionGuion
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
                        offset <= posicionGuion
                    ) {

                        offset

                    } else {

                        offset - 1
                    }
                }
            }

        return TransformedText(
            text = AnnotatedString(
                rutFormateado
            ),
            offsetMapping = offsetMapping
        )
    }
}