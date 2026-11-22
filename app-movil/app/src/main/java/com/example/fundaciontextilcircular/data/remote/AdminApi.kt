package com.example.fundaciontextilcircular.data.remote

import com.example.fundaciontextilcircular.data.remote.dto.AdminSolicitudDTO
import com.example.fundaciontextilcircular.data.remote.dto.RechazoRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdminApi {

    @GET("api/admin/solicitudes")
    suspend fun obtenerSolicitudes():
            Response<List<AdminSolicitudDTO>>

    @PUT("api/admin/aprobar/{id}")
    suspend fun aprobarSolicitud(
        @Path("id") id: Long
    ): Response<AdminSolicitudDTO>

    @PUT("api/admin/rechazar/{id}")
    suspend fun rechazarSolicitud(
        @Path("id") id: Long,
        @Body request: RechazoRequestDTO
    ): Response<AdminSolicitudDTO>
}