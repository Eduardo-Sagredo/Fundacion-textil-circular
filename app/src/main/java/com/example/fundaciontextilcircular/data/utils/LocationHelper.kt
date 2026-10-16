package com.example.fundaciontextilcircular.data.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import java.util.Locale

object LocationHelper {

    @SuppressLint("MissingPermission")
    fun obtenerUbicacionSugerida(
        context: Context,
        onResultado: (comuna: String, region: String) -> Unit,
        onError: (String) -> Unit
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    try {
                        val geocoder = Geocoder(context, Locale.getDefault())

                        // Geocodificación inversa: Coordenadas -> Dirección física
                        @Suppress("DEPRECATION")
                        val direcciones = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        if (!direcciones.isNullOrEmpty()) {
                            val direccion = direcciones[0]
                            // SubAdminArea o Locality suelen corresponder a la Comuna en Chile
                            val comuna = direccion.locality ?: direccion.subAdminArea ?: "Comuna no identificada"
                            val region = direccion.adminArea ?: "Región no identificada"

                            onResultado(comuna, region)
                        } else {
                            onError("No se pudo obtener la dirección desde el GPS.")
                        }
                    } catch (e: Exception) {
                        onError("Error al traducir las coordenadas: ${e.message}")
                    }
                } else {
                    onError("Asegúrate de tener el GPS activo en tu celular.")
                }
            }
            .addOnFailureListener { e ->
                onError("Error al acceder al sensor GPS: ${e.message}")
            }
    }
}