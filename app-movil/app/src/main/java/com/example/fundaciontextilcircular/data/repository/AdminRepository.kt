package com.example.fundaciontextilcircular.data.repository

import com.example.fundaciontextilcircular.data.remote.AdminApi
import com.example.fundaciontextilcircular.data.remote.dto.AdminSolicitudDTO
import com.example.fundaciontextilcircular.data.remote.dto.RechazoRequestDTO

class AdminRepository(
    private val adminApi: AdminApi
) {

    suspend fun obtenerSolicitudes():
            Result<List<AdminSolicitudDTO>> {

        return try {

            val response =
                adminApi.obtenerSolicitudes()

            if (response.isSuccessful) {

                Result.success(
                    response.body() ?: emptyList()
                )

            } else {

                Result.failure(
                    Exception(
                        "No se pudieron cargar las solicitudes"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    "No se pudo conectar con el servidor",
                    e
                )
            )
        }
    }


    suspend fun aprobarSolicitud(
        id: Long
    ): Result<AdminSolicitudDTO> {

        return try {

            val response =
                adminApi.aprobarSolicitud(id)

            if (
                response.isSuccessful &&
                response.body() != null
            ) {

                Result.success(
                    response.body()!!
                )

            } else {

                Result.failure(
                    Exception(
                        "No se pudo aprobar la solicitud"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    "No se pudo conectar con el servidor",
                    e
                )
            )
        }
    }


    suspend fun rechazarSolicitud(
        id: Long,
        motivo: String
    ): Result<AdminSolicitudDTO> {

        return try {

            val response =
                adminApi.rechazarSolicitud(
                    id = id,
                    request =
                        RechazoRequestDTO(
                            motivo = motivo
                        )
                )

            if (
                response.isSuccessful &&
                response.body() != null
            ) {

                Result.success(
                    response.body()!!
                )

            } else {

                Result.failure(
                    Exception(
                        "No se pudo rechazar la solicitud"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    "No se pudo conectar con el servidor",
                    e
                )
            )
        }
    }
}