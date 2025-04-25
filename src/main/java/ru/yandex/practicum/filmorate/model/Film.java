package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;


import java.time.LocalDate;


/**
 * Film.
 */
@Data
@Builder
public class Film {

    private Long id;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @NonNull
    private LocalDate releaseDate;

    @Min(1)
    private Long duration;
}
