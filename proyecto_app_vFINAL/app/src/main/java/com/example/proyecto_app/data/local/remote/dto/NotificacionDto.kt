package com.example.proyecto_app.data.local.remote.dto

import com.google.gson.annotations.SerializedName

data class NotificacionDto(
    @SerializedName("id") val id: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("adminName") val adminName: String,
    @SerializedName("message") val message: String,
    @SerializedName("publicationTitle") val publicationTitle: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("isRead") val isRead: Boolean
)