package com.example.fundaciontextilcircular.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkMonitor(context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isOnline: Flow<Boolean> = callbackFlow {

        // Función interna para revisar el estado inicial al abrir la app
        val checkInitialState = {
            val redActual = connectivityManager.activeNetwork
            val capacidades = connectivityManager.getNetworkCapabilities(redActual)
            capacidades?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        }

        // Emitimos el estado inicial
        trySend(checkInitialState())

        // Creamos el "espía" que le avisará a la app si la red cambia
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true) // ¡Volvió el internet!
            }

            override fun onLost(network: Network) {
                trySend(false) // ¡Se cortó el internet!
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Cuando la app se cierra, desconectamos el espía para ahorrar batería
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}