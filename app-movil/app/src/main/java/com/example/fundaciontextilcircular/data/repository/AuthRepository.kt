package com.example.fundaciontextilcircular.data.repository

import com.example.fundaciontextilcircular.data.remote.AuthApi
import com.example.fundaciontextilcircular.data.remote.dto.CambiarContrasenaRequestDTO
import com.example.fundaciontextilcircular.data.remote.dto.LoginRequestDTO
import com.example.fundaciontextilcircular.data.remote.dto.LoginResponseDTO
import com.example.fundaciontextilcircular.data.remote.dto.PerfilResponseDTO
import com.example.fundaciontextilcircular.data.remote.dto.RegistroRequestDTO
import com.example.fundaciontextilcircular.data.remote.dto.RegistroResponseDTO
import org.json.JSONObject

class AuthRepository(
    private val authApi: AuthApi
) {

    suspend fun login(
        correo: String,
        contrasena: String
    ): Result<LoginResponseDTO> {

        return try {

            val response =
                authApi.login(

                    LoginRequestDTO(
                        correo = correo,
                        contrasena = contrasena
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

                val mensaje =
                    obtenerMensajeError(
                        response
                            .errorBody()
                            ?.string()
                    )


                Result.failure(
                    Exception(

                        mensaje.ifBlank {
                            "Correo o contraseña incorrectos"
                        }
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


    suspend fun registrar(
        request: RegistroRequestDTO
    ): Result<RegistroResponseDTO> {

        return try {

            val response =
                authApi.registrar(
                    request
                )


            if (
                response.isSuccessful &&
                response.body() != null
            ) {

                Result.success(
                    response.body()!!
                )

            } else {

                val mensaje =
                    obtenerMensajeError(
                        response
                            .errorBody()
                            ?.string()
                    )


                Result.failure(
                    Exception(

                        mensaje.ifBlank {
                            "No se pudo crear la cuenta"
                        }
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


    // =========================================
    // PERFIL
    // =========================================

    suspend fun obtenerPerfil(
        correo: String
    ): Result<PerfilResponseDTO> {

        return try {

            val response =
                authApi.obtenerPerfil(
                    correo
                        .trim()
                        .lowercase()
                )


            if (
                response.isSuccessful &&
                response.body() != null
            ) {

                Result.success(
                    response.body()!!
                )

            } else {

                val mensaje =
                    obtenerMensajeError(
                        response
                            .errorBody()
                            ?.string()
                    )


                Result.failure(
                    Exception(

                        mensaje.ifBlank {
                            "No se pudo cargar el perfil"
                        }
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


    // =========================================
    // CAMBIAR CONTRASEÑA
    // =========================================

    suspend fun cambiarContrasena(
        correo: String,
        contrasenaActual: String,
        nuevaContrasena: String
    ): Result<String> {

        return try {

            val response =
                authApi.cambiarContrasena(

                    CambiarContrasenaRequestDTO(

                        correo =
                            correo
                                .trim()
                                .lowercase(),

                        contrasenaActual =
                            contrasenaActual,

                        nuevaContrasena =
                            nuevaContrasena
                    )
                )


            if (
                response.isSuccessful
            ) {

                val mensaje =
                    response
                        .body()
                        ?.get(
                            "mensaje"
                        )
                        ?: "Contraseña actualizada correctamente"


                Result.success(
                    mensaje
                )

            } else {

                val mensaje =
                    obtenerMensajeError(
                        response
                            .errorBody()
                            ?.string()
                    )


                Result.failure(
                    Exception(

                        mensaje.ifBlank {
                            "No se pudo cambiar la contraseña"
                        }
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


    private fun obtenerMensajeError(
        contenido: String?
    ): String {

        if (
            contenido.isNullOrBlank()
        ) {

            return ""
        }


        return try {

            JSONObject(
                contenido
            ).optString(
                "mensaje",
                ""
            )

        } catch (e: Exception) {

            ""
        }
    }
}