package com.example.proyecto_app.data.local.remote.dto

import com.google.gson.annotations.SerializedName

// Espejo del microservicio de Publicaciones
data class PublicacionDto(
    @SerializedName("id") val id: Long,

    // 🔴 CAMBIO CRÍTICO: Ponlo todo en minúsculas para coincidir con Java
    @SerializedName("userid") val userId: Long,

    @SerializedName("category") val category: String,
    @SerializedName("imageUri") val imageUri: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,

    // 🔴 CAMBIO CRÍTICO: Ponlo todo en minúsculas para coincidir con Java
    @SerializedName("authorname") val authorName: String,

    @SerializedName("createDt") val createDt: String?,
    @SerializedName("status") val status: String,
    @SerializedName("likes") val likes: Int
)