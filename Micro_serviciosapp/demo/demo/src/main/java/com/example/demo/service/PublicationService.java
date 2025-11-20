package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.PublicationModel;
import com.example.demo.repository.PublicationRepository;
import com.example.demo.webClient.UsuarioClient;

@Service
public class PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    public PublicationModel crearPublicacion(PublicationModel crearPubli) {
        Map<String, Object> usuarioData = usuarioClient.getUsuarioById(crearPubli.getUserid());
        if (usuarioData == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        return publicationRepository.save(crearPubli);

    }

    public List<PublicationModel> obtenerTodasPubli() {
        return publicationRepository.findAll();
    }

    public List<PublicationModel> obtenerPubliPorUsuario(Long userid) {
        return publicationRepository.findByUserid(userid);
    }

    public void eliminarPubli( Long publicationId) {
        // 1. Buscar publicación
        PublicationModel publi = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        // 2. Obtener el userId de la publicación
        Long userId = publi.getUserid();

        // 3. Consultar al microservicio de usuarios
        Map<String, Object> usuarioData = usuarioClient.getUsuarioById(userId);

        if (usuarioData == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // 4. Obtener el rol del usuario
        Map<String, Object> rolMap = (Map<String, Object>) usuarioData.get("rol");

        if (rolMap == null) {
            throw new RuntimeException("El usuario no tiene rol asociado");
        }

        String nombreRol = rolMap.get("nombre_rol").toString();

        if (!nombreRol.equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("No autorizado para eliminar la publicación");
        }

        // 5. Eliminar publicación
        publicationRepository.deleteById(publicationId);
    }

    public PublicationModel buscarPublicacionPorId(Long publicationId) {
        return publicationRepository.findById(publicationId).orElseThrow(() -> new RuntimeException("No encontrado"));

    }

}
