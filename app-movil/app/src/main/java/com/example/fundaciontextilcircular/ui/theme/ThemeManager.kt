package com.example.fundaciontextilcircular.ui.theme

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


object ThemeManager {

    private const val PREFS_NAME =
        "textil_theme_preferences"

    private const val KEY_DARK_MODE =
        "dark_mode"


    var darkMode by
    mutableStateOf<Boolean?>(
        null
    )

        private set


    fun inicializar(
        context: Context,
        valorInicial: Boolean
    ) {

        if (
            darkMode != null
        ) {

            return
        }


        val preferencias =
            context.getSharedPreferences(

                PREFS_NAME,

                Context.MODE_PRIVATE
            )


        darkMode =

            if (
                preferencias.contains(
                    KEY_DARK_MODE
                )
            ) {

                preferencias.getBoolean(

                    KEY_DARK_MODE,

                    valorInicial
                )

            } else {

                valorInicial
            }
    }


    fun cambiarModo(
        context: Context,
        oscuro: Boolean
    ) {

        darkMode =
            oscuro


        context
            .getSharedPreferences(

                PREFS_NAME,

                Context.MODE_PRIVATE
            )
            .edit()
            .putBoolean(

                KEY_DARK_MODE,

                oscuro
            )
            .apply()
    }
}