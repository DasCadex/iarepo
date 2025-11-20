package com.example.notificaciones.service;

import com.example.notificaciones.model.NotificationModel;
import com.example.notificaciones.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Crear una notificación (llamado cuando se borra un post)
    public NotificationModel crearNotificacion(NotificationModel notificacion) {
        return notificationRepository.save(notificacion);
    }

    // Obtener notificaciones para un usuario
    public List<NotificationModel> obtenerNotificacionesPorUsuario(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // Marcar como leída (opcional, para cuando el usuario abre la notificación)
    public void marcarComoLeida(Long notificationId) {
        NotificationModel noti = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        noti.setIsRead(true);
        notificationRepository.save(noti);
    }
}