package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;

@Data
public class Film {

    private Long id;

    @NotBlank(message = "Имя обязательно для заполнения")
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Min(1)
    private Long duration;

    private LinkedHashSet<Genre> genres;

    @NotNull
    private Mpa mpa;

    private HashSet<Long> likes;
}
