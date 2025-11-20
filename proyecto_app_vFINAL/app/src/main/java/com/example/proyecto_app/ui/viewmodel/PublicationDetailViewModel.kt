package com.example.proyecto_app.ui.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_app.data.local.remote.dto.ComentarioDto
import com.example.proyecto_app.data.local.remote.dto.LoginResponseDto
import com.example.proyecto_app.data.local.remote.dto.NotificacionDto
import com.example.proyecto_app.data.local.remote.dto.PublicacionDto

import com.example.proyecto_app.data.repository.CommentRepository
import com.example.proyecto_app.data.repository.NotificationRepository
import com.example.proyecto_app.data.repository.PublicationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ✅ ESTADO USANDO DTOs
data class PublicationDetailUiState(
    val publication: PublicacionDto? = null, // Ya no usamos PublicationWithAuthor
    val comments: List<ComentarioDto> = emptyList(),
    val newCommentText: String = "",
    val isLoading: Boolean = true,
    val deleted: Boolean = false,
    val showDeleteConfirmDialog: Boolean = false,
    val deleteReason: String = ""
)

// Estado local auxiliar
private data class LocalState(
    val newCommentText: String = "",
    val showDeleteConfirmDialog: Boolean = false,
    val deleteReason: String = "",
    val deleted: Boolean = false
)

class PublicationDetailViewModel(
    private val publicationRepository: PublicationRepository,
    private val commentRepository: CommentRepository,
    private val notificationRepository: NotificationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val publicationId: Long

    init {
        publicationId = requireNotNull(savedStateHandle.get<Long>("publicationId")) {
            "Error: Publication ID no encontrado."
        }
    }

    private val _localState = MutableStateFlow(LocalState())

    // ✅ COMBINAMOS FLUJOS
    val detailState: StateFlow<PublicationDetailUiState> = combine(
        publicationRepository.getPublicationById(publicationId), // Devuelve PublicacionDto?
        commentRepository.getCommentsForPublication(publicationId), // Devuelve List<ComentarioDto>
        _localState
    ) { publication, comments, local ->
        PublicationDetailUiState(
            publication = publication,
            comments = comments,
            isLoading = publication == null,
            newCommentText = local.newCommentText,
            showDeleteConfirmDialog = local.showDeleteConfirmDialog,
            deleteReason = local.deleteReason,
            deleted = local.deleted
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = PublicationDetailUiState(isLoading = true)
    )

    fun onNewCommentChange(text: String) {
        _localState.update { it.copy(newCommentText = text) }
    }

    // ✅ AÑADIR COMENTARIO (Usando DTOs)
    fun addComment(currentUser: LoginResponseDto) {
        val text = _localState.value.newCommentText
        if (text.isBlank()) return

        // Creamos el DTO. ID y Fecha suelen ser nulos al enviar si el backend los genera.
        val comment = ComentarioDto(
            id = null,
            publicationId = publicationId,
            userId = currentUser.usuarioId,
            authorName = currentUser.nombreUsuario,
            text = text.trim(),
            createdAt = null
        )

        viewModelScope.launch {
            commentRepository.addComment(comment)
            _localState.update { it.copy(newCommentText = "") }
        }
    }

    fun onShowDeleteDialog() {
        _localState.update { it.copy(showDeleteConfirmDialog = true, deleteReason = "") }
    }

    fun onDismissDeleteDialog() {
        _localState.update { it.copy(showDeleteConfirmDialog = false) }
    }

    fun onReasonChange(reason: String) {
        _localState.update { it.copy(deleteReason = reason) }
    }

    // ✅ BORRAR PUBLICACIÓN (Con notificación)
    fun deletePublicationWithReason(adminUser: LoginResponseDto) {
        val reason = _localState.value.deleteReason
        val publication = detailState.value.publication // Obtenemos el post actual

        if (reason.isBlank() || publication == null) return

        viewModelScope.launch {
            try {
                // 1. Crear notificación
                val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                val notification = NotificacionDto(
                    id = 0,
                    userId = publication.userId,
                    adminName = adminUser.nombreUsuario,
                    message = reason,
                    publicationTitle = publication.title,
                    createdAt = currentDate,
                    isRead = false
                )
                notificationRepository.createNotification(notification)

                // 2. Borrar post
                publicationRepository.deletePublication(publicationId)

                _localState.update { it.copy(deleted = true, showDeleteConfirmDialog = false) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetDeletedFlag() {
        _localState.update { it.copy(deleted = false) }
    }
}