package com.carsmoviesinventory.app.Entities;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VideoGamesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("gameName")
    @NotBlank(message = "Game name is required")
    @Size(min = 3, max = 100, message = "Game name must be between 3 and 100 characters")
    private String gameName;

    @JsonProperty("releaseYear")
    @NotNull(message = "Year is required")
    @Pattern(regexp = "^(19|20)\\d{2}$", message = "Year must be a valid 4-digit number (1900-2099)")
    private String releaseYear;

    @JsonProperty("playTime")
    @NotNull(message = "Playtime is required")
    @Min(value = 1, message = "Playtime must be at least 1 minute")
    private Integer playTime;
}
