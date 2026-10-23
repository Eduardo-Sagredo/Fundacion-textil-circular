package com.example.fundaciontextilcircular.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundaciontextilcircular.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PantallaSplash(
    onFinalizar: () -> Unit
) {

    val density =
        LocalDensity.current.density


    // =============================================
    // ANIMACIONES DEL LOGO
    // =============================================

    val alphaLogo =
        remember {
            Animatable(0f)
        }

    val escalaLogo =
        remember {
            Animatable(0.25f)
        }

    val rotacionY =
        remember {
            Animatable(-180f)
        }

    val rotacionX =
        remember {
            Animatable(25f)
        }

    val movimientoY =
        remember {
            Animatable(180f)
        }


    // =============================================
    // TEXTOS
    // =============================================

    var mostrarTextil by remember {
        mutableStateOf(false)
    }

    var mostrarCircular by remember {
        mutableStateOf(false)
    }


    // =============================================
    // ANIMACIÓN COMPLETA
    // =============================================

    LaunchedEffect(Unit) {

        // -----------------------------------------
        // APARECER
        // -----------------------------------------

        launch {

            alphaLogo.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 500
                )
            )
        }


        // -----------------------------------------
        // SUBIR DESDE ABAJO
        // -----------------------------------------

        launch {

            movimientoY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 1000
                )
            )
        }


        // -----------------------------------------
        // GIRO 3D HORIZONTAL
        // -----------------------------------------

        launch {

            rotacionY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 1400
                )
            )
        }


        // -----------------------------------------
        // INCLINACIÓN 3D
        // -----------------------------------------

        launch {

            rotacionX.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 1100
                )
            )
        }


        // -----------------------------------------
        // AUMENTAR TAMAÑO
        // -----------------------------------------

        escalaLogo.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000
            )
        )


        // =========================================
        // PEQUEÑO REBOTE
        // =========================================

        escalaLogo.animateTo(
            targetValue = 1.12f,
            animationSpec = tween(
                durationMillis = 180
            )
        )

        escalaLogo.animateTo(
            targetValue = 0.96f,
            animationSpec = tween(
                durationMillis = 120
            )
        )

        escalaLogo.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 150
            )
        )


        // Espera antes del texto

        delay(250)


        // =========================================
        // APARECE TEXTIL
        // =========================================

        mostrarTextil =
            true


        delay(650)


        // =========================================
        // APARECE CIRCULAR
        // =========================================

        mostrarCircular =
            true


        // Mostrar logo completo por un momento

        delay(1600)


        // =========================================
        // IR A LA BIENVENIDA
        // =========================================

        onFinalizar()
    }


    // =============================================
    // PANTALLA
    // =============================================

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF4EFE6)
                ),

        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {


            // =====================================
            // LOGO
            // =====================================

            Image(
                painter =
                    painterResource(
                        id =
                            R.drawable.logo_textil_circular
                    ),

                contentDescription =
                    "Logo Textil Circular",

                modifier =
                    Modifier
                        .size(270.dp)
                        .graphicsLayer {

                            // Aparecer
                            alpha =
                                alphaLogo.value


                            // Tamaño
                            scaleX =
                                escalaLogo.value

                            scaleY =
                                escalaLogo.value


                            // Movimiento vertical
                            translationY =
                                movimientoY.value


                            // Giro 3D
                            rotationY =
                                rotacionY.value

                            rotationX =
                                rotacionX.value


                            // Profundidad 3D
                            cameraDistance =
                                14f * density
                        },

                contentScale =
                    ContentScale.Fit
            )


            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )


            // =====================================
            // TEXTIL
            // =====================================

            AnimatedVisibility(

                visible =
                    mostrarTextil,

                enter =
                    fadeIn(
                        animationSpec =
                            tween(
                                durationMillis = 600
                            )
                    ) +
                            slideInVertically(
                                animationSpec =
                                    tween(
                                        durationMillis = 600
                                    ),

                                initialOffsetY = {
                                    it
                                }
                            )
            ) {

                Text(
                    text =
                        "TEXTIL",

                    color =
                        Color(0xFF202824),

                    fontSize =
                        52.sp,

                    fontWeight =
                        FontWeight.Black,

                    textAlign =
                        TextAlign.Center
                )
            }


            // =====================================
            // CIRCULAR
            // =====================================

            AnimatedVisibility(

                visible =
                    mostrarCircular,

                enter =
                    fadeIn(
                        animationSpec =
                            tween(
                                durationMillis = 600
                            )
                    ) +
                            slideInVertically(
                                animationSpec =
                                    tween(
                                        durationMillis = 600
                                    ),

                                initialOffsetY = {
                                    it
                                }
                            )
            ) {

                Text(
                    text =
                        "CIRCULAR",

                    color =
                        Color(0xFFFFB900),

                    fontSize =
                        52.sp,

                    fontWeight =
                        FontWeight.Black,

                    textAlign =
                        TextAlign.Center
                )
            }
        }
    }
}