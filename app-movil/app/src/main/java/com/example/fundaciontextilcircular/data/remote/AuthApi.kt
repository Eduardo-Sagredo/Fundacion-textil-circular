package com.example.fundaciontextilcircular.data.remote

import com.example.fundaciontextilcircular.data.remote.dto.CambiarContrasenaRequestDTO
import com.example.fundaciontextilcircular.data.remote.dto.LoginRequestDTO
import com.example.fundaciontextilcircular.data.remote.dto.LoginResponseDTO
import com.example.fundaciontextilcircular.data.remote.dto.OlvideContrasenaRequestDTO
import com.example.fundaciontextilcircular.data.remote.dto.PerfilResponseDTO
import com.example.fundaciontextilcircular.data.remote.dto.RegistroRequestDTO
import com.example.fundaciontextilcircular.data.remote.dto.RegistroResponseDTO
import com.example.fundaciontextilcircular.data.remote.dto.RestablecerContrasenaRequestDTO

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {

    @POST("api/auth/registro")
    suspend fun registrar(
        @Body request: RegistroRequestDTO
    ): Response<RegistroResponseDTO>


    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequestDTO
    ): Response<LoginResponseDTO>


    @GET("api/auth/perfil/{correo}")
    suspend fun obtenerPerfil(
        @Path("correo")
        correo: String
    ): Response<PerfilResponseDTO>


    @PUT("api/auth/cambiar-contrasena")
    suspend fun cambiarContrasena(
        @Body request: CambiarContrasenaRequestDTO
    ): Response<Map<String, String>>


    // =========================================
    // OLVIDÉ MI CONTRASEÑA
    // =========================================

    @POST("api/auth/olvide-contrasena")
    suspend fun olvideContrasena(
        @Body request: OlvideContrasenaRequestDTO
    ): Response<Map<String, String>>


    // =========================================
    // RESTABLECER CONTRASEÑA
    // =========================================

    @POST("api/auth/restablecer-contrasena")
    suspend fun restablecerContrasena(
        @Body request: RestablecerContrasenaRequestDTO
    ): Response<Map<String, String>>
}