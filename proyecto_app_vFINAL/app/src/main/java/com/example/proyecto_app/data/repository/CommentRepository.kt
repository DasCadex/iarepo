package com.example.proyecto_app.data.repository

import com.example.proyecto_app.data.local.remote.PixelHubApi
import com.example.proyecto_app.data.local.remote.dto.ComentarioDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CommentRepository(private val api: PixelHubApi) {

    fun getCommentsForPublication(id: Long): Flow<List<ComentarioDto>> = flow {
        try {
            emit(api.getComentarios(id))
        } catch (e: Exception) { emit(emptyList()) }
    }

    suspend fun addComment(comment: ComentarioDto) {
        try { api.crearComentario(comment) } catch (e: Exception) { e.printStackTrace() }
    }
}