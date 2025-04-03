package com.carsmoviesinventory.app.Controllers;

import com.carsmoviesinventory.app.Entities.VideoGamesEntity;
import com.carsmoviesinventory.app.Services.VideoGamesService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/videogames")
@Validated
public class VideoGamesController {

    private final VideoGamesService videoGamesService;

    public VideoGamesController(VideoGamesService videoGamesService) {
        this.videoGamesService = videoGamesService;
    }

    @GetMapping
    public ResponseEntity<?> getAllVideoGames(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "gameName,asc") String sort) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
            return videoGamesService.getAllGames(pageable);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid sorting direction. Use 'asc' or 'desc'.");
        }
    }

    private Sort.Order parseSort(String sort) {
        String[] sortParams = sort.split(",");
        if (sortParams.length != 2) {
            throw new IllegalArgumentException("Sort parameter must have both field and direction (e.g., 'gameYear,desc').");
        }

        String property = sortParams[0];
        String direction = sortParams[1].toLowerCase();

        List<String> validDirections = Arrays.asList("asc", "desc");
        if (!validDirections.contains(direction)) {
            throw new IllegalArgumentException("Invalid sort direction. Use 'asc' or 'desc'.");
        }

        return new Sort.Order(Sort.Direction.fromString(direction), property);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVideoGameById(@PathVariable UUID id) {
        return videoGamesService.getGameById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getGamesByName(
            @RequestParam String gameName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "gameName,asc") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
        return videoGamesService.getGamesByName(gameName, pageable);
    }

    @PostMapping
    public ResponseEntity<?> insertVideoGame(@Valid @RequestBody VideoGamesEntity videoGamesEntity) {
        return videoGamesService.addGame(videoGamesEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVideoGame(@PathVariable UUID id, @Valid @RequestBody VideoGamesEntity videoGamesEntity) {
        return videoGamesService.updateGame(id, videoGamesEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVideoGame(@PathVariable UUID id) {
        return videoGamesService.deleteGame(id);
    }
}
