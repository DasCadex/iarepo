package com.example.proyecto_app.data.local.remote.dto

import com.google.gson.annotations.SerializedName

data class ComentarioDto(
    @SerializedName("id") val id: Long,
    @SerializedName("content") val content: String,

    // 🔴 ANTES: "publication_id"
    // 🟢 AHORA: "publicationId" (Coincide con Java)
    @SerializedName("publicationId") val publicationId: Long,

    // 🔴 ANTES: "user_id"
    // 🟢 AHORA: "userId"
    @SerializedName("userId") val userId: Long,

    // 🔴 ANTES: "author_name"
    // 🟢 AHORA: "authorName"
    @SerializedName("authorName") val authorName: String,

    // Este verifica si en Java se llama "createDt" o "create_dt".
    // Por lo general en Java usas camelCase, así que debería ser createDt.
    @SerializedName("createDt") val createDt: String?
)