package com.example.fundaciontextilcircular.data.network

import retrofit2.Response
import retrofit2.http.*
interface TextilCircularApiService {
    // --- Endpoints de Mensajería ---
    @POST("api/mensajes")
    suspend fun enviarMensaje(@Body request: MensajeRequestDto): Response<List<MensajeResponseDto>>

    @GET("api/chats/{chatId}/mensajes")
    suspend fun obtenerMensajesPorChat(@Path("chatId") chatId: String): Response<List<MensajeResponseDto>>

    @GET("api/chats")
    suspend fun listarChats(): Response<List<ChatResponseDto>>

    @PUT("api/chats/{chatId}/cerrar")
    suspend fun cerrarChat(@Path("chatId") chatId: String): Response<ChatResponseDto>

    // --- Endpoints de Productos ---
    @GET("api/productos")
    suspend fun listarProductos(
        @Query("categoria") categoria: String? = null,
        @Query("soloDisponibles") soloDisponibles: Boolean = false
    ): Response<List<ProductoResponseDto>>

    @GET("api/productos/{id}")
    suspend fun obtenerProducto(@Path("id") id: Long): Response<ProductoResponseDto>

    @GET("api/productos/usuario/{usuarioId}")
    suspend fun listarProductosPorUsuario(@Path("usuarioId") usuarioId: Long): Response<List<ProductoResponseDto>>

    @POST("api/productos")
    suspend fun crearProducto(@Body request: ProductoRequestDto): Response<ProductoResponseDto>

    @PUT("api/productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: Long,
        @Body request: ProductoRequestDto
    ): Response<ProductoResponseDto>

    @PATCH("api/productos/{id}/disponibilidad")
    suspend fun cambiarDisponibilidad(
        @Path("id") id: Long,
        @Query("disponible") disponible: Boolean
    ): Response<ProductoResponseDto>

    @DELETE("api/productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Long): Response<Unit>
}