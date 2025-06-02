package ru.yandex.practicum.filmorate.dto.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;

@Data
public class NewFilmRequest {

    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    private LinkedHashSet<Genre> genres;
    private Mpa mpa;
    private HashSet<Long> likes;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasReleaseDate() {
        return !(releaseDate == null || releaseDate.isBefore(LocalDate.of(1895, 1, 28)) ||
                releaseDate.isAfter(LocalDate.now()));
    }

    public boolean hasDuration() {
        return !(duration < 1);
    }

    public boolean hasMpa() {
        return !(mpa == null);
    }

    public boolean hasDescription() {
        return !(description.length() > 200);
    }

    public boolean hasGenres() {
        return !(genres == null);
    }
}
