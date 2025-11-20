package com.example.proyecto_app.data.local.remote.dto

import com.google.gson.annotations.SerializedName

// --- LOGIN ---
data class LoginRequestDto(
    @SerializedName("nombreUsuario") val nombreUsuario: String,
    @SerializedName("contrasena") val contrasena: String
)

data class LoginResponseDto(
    @SerializedName("usuario_id") val usuarioId: Long,
    @SerializedName("nombre_usuario") val nombreUsuario: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("rol_id") val rolId: Long,
    @SerializedName("token") val token: String
)

// --- REGISTRO Y ACTUALIZACIÓN ---
data class UsuarioBodyDto(
    @SerializedName("nombre_usuario") val nombreUsuario: String,
    @SerializedName("contrasena") val contrasena: String,
    @SerializedName("numero_telefono") val numeroTelefono: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("rol") val rol: RolBodyDto
)

data class RolBodyDto(
    @SerializedName("rol_id") val rolId: Long
)

// --- LISTADO DE USUARIOS (Aquí estaba el problema) ---
// ✅ AHORA INCLUYE TODOS LOS CAMPOS QUE USA AdminViewModel
data class UsuarioDto(
    @SerializedName("usuario_id") val usuarioId: Long,
    @SerializedName("nombre_usuario") val nombreUsuario: String,
    @SerializedName("contrasena") val contrasena: String?, // Puede venir nulo
    @SerializedName("numero_telefono") val numeroTelefono: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("rol_id") val rolId: Long
)