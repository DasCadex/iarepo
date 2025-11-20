package com.example.proyecto_app.data.local.remote

import com.example.proyecto_app.data.local.remote.dto.ComentarioDto
import com.example.proyecto_app.data.local.remote.dto.LoginRequestDto
import com.example.proyecto_app.data.local.remote.dto.LoginResponseDto
import com.example.proyecto_app.data.local.remote.dto.NotificacionDto
import com.example.proyecto_app.data.local.remote.dto.PublicacionDto
import com.example.proyecto_app.data.local.remote.dto.UsuarioBodyDto
import com.example.proyecto_app.data.local.remote.dto.UsuarioDto

import retrofit2.Response
import retrofit2.http.*

interface PixelHubApi {

    // --- USUARIOS ---
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @POST("api/v1/users")
    suspend fun registro(@Body usuario: UsuarioBodyDto): Response<Any>

    // ✅ CORRECCIÓN AQUÍ:
    // Añadimos @Path("usuario_id") para que coincida con la URL
    @PUT("api/v1/users/{usuario_id}")
    suspend fun actualizarUsuario(
        @Path("usuario_id") id: Long,
        @Body usuario: UsuarioBodyDto
    ): Response<Any>

    @GET("api/v1/user")
    suspend fun listarUsuarios(): List<UsuarioDto>

    // --- PUBLICACIONES ---
    @GET("api/publicaciones")
    suspend fun getPublicaciones(): List<PublicacionDto>

    @POST("api/publicaciones/publicar")
    suspend fun crearPublicacion(@Body publicacion: PublicacionDto): Response<Unit>

    @DELETE("api/publicaciones/{id}")
    suspend fun eliminarPublicacion(@Path("id") id: Long): Response<Void>

    @POST("api/publicaciones/{id}/like")
    suspend fun darLike(@Path("id") id: Long): Response<Void>

    // --- COMENTARIOS ---
    @GET("api/comentarios/publicacion/{id}")
    suspend fun getComentarios(@Path("id") publicationId: Long): List<ComentarioDto>

    @POST("api/comentarios/comentar")
    suspend fun crearComentario(@Body comentario: ComentarioDto): ComentarioDto

    // --- NOTIFICACIONES ---
    @GET("api/notificaciones/usuario/{userId}")
    suspend fun getNotificaciones(@Path("userId") userId: Long): List<NotificacionDto>

    @POST("api/notificaciones")
    suspend fun crearNotificacion(@Body notificacion: NotificacionDto): NotificacionDto
}