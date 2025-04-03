package com.carsmoviesinventory.app.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.carsmoviesinventory.app.Repositories.VideoGamesRepository;
import com.carsmoviesinventory.app.Entities.VideoGamesEntity;

import java.util.*;

@Service
public class VideoGamesService {

    private final VideoGamesRepository videoGamesRepository;

    public VideoGamesService(VideoGamesRepository videoGamesRepository) {
        this.videoGamesRepository = videoGamesRepository;
    }

    public ResponseEntity<?> getAllGames(Pageable pageable) {
        Page<VideoGamesEntity> games = videoGamesRepository.findAll(pageable);
        return buildResponseEntity(games);
    }

    public ResponseEntity<?> getGameById(UUID id) {
        return videoGamesRepository.findById(id)
                .map(game -> ResponseEntity.ok(Collections.singletonMap("Game", game)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("Game", null)));
    }

    public ResponseEntity<?> getGamesByName(String gameName, Pageable pageable) {
        Page<VideoGamesEntity> games = videoGamesRepository.findAllByGameNameContainingIgnoreCase(gameName, pageable);
        return buildResponseEntity(games);
    }

    private ResponseEntity<?> buildResponseEntity(Page<VideoGamesEntity> games) {
        Map<String, Object> response = new HashMap<>();
        response.put("TotalElements", games.getTotalElements());
        response.put("TotalPages", games.getTotalPages());
        response.put("CurrentPage", games.getNumber());
        response.put("NumberOfElements", games.getNumberOfElements());
        response.put("Games", games.getContent());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> addGame(VideoGamesEntity gameToAdd) {
        // 🔹 Se corrigió el error en existsByGameNameIgnoreCase
        boolean exists = videoGamesRepository.existsByGameNameIgnoreCase(gameToAdd.getGameName());
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("Status", "Game already exists."));
        }
        VideoGamesEntity savedGame = videoGamesRepository.save(gameToAdd);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("Status", String.format("Added Game with ID %s", savedGame.getId())));
    }

    public ResponseEntity<?> updateGame(UUID id, VideoGamesEntity gameToUpdate) {
        return videoGamesRepository.findById(id)
                .map(existingGame -> {
                    existingGame.setGameName(gameToUpdate.getGameName());
                    existingGame.setReleaseYear(gameToUpdate.getReleaseYear());
                    existingGame.setPlayTime(gameToUpdate.getPlayTime());

                    videoGamesRepository.save(existingGame);
                    return ResponseEntity.ok(Collections.singletonMap("Status", String.format("Updated Game with ID %s", existingGame.getId())));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("Status", String.format("Game with ID %s not found.", id))));
    }

    public ResponseEntity<?> deleteGame(UUID id) {
        // 🔹 Se corrigió la estructura del paréntesis en Collections.singletonMap
        if (!videoGamesRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Status", String.format("Game with ID %s doesn't exist.", id)));
        }
        videoGamesRepository.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("Status", String.format("Deleted Game with ID %s", id)));
    }
}
