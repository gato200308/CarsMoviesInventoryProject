package com.carsmoviesinventory.app.Repositories;

import com.carsmoviesinventory.app.Entities.VideoGamesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoGamesRepository extends JpaRepository<VideoGamesEntity, UUID> {

    // Búsqueda de videojuegos cuyo nombre contenga una cadena específica (ignorando mayúsculas y minúsculas)
    Page<VideoGamesEntity> findAllByGameNameContainingIgnoreCase(String gameName, Pageable pageable);

    // Verificar si un videojuego con un nombre específico existe (ignorando mayúsculas y minúsculas)
    boolean existsByGameNameIgnoreCase(String gameName);
}
