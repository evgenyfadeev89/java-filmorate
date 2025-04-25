package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findall() {
        log.info("Вызов показа всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создание фильма");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка: дата раньше допустимой");
            throw new ValidationException("Дата раньше допустимой");
        }
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Обновление фильма");
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            if (newFilm.getName() != null) {
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getDescription() != null && newFilm.getDescription().length() <= 200) {
                oldFilm.setDescription(newFilm.getDescription());
            } else if (newFilm.getDescription().length() > 200) {
                log.error("Ошибка: Длина описания больше допустимой");
                throw new RuntimeException("Длина описания больше допустимой");
            }
            if (newFilm.getDuration() != null && newFilm.getDuration() > 0) {
                oldFilm.setDuration(newFilm.getDuration());
            } else if (newFilm.getDuration() < 0) {
                log.error("Ошибка: Продолжительность фильма меньше допустимой");
                throw new RuntimeException("Продолжительность фильма меньше допустимой");
            }
            if (newFilm.getReleaseDate() != null &&
                    !newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            } else if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error("Ошибка: Дата раньше допустимой");
                throw new ValidationException("Дата раньше допустимой");
            }

            return oldFilm;
        }

        log.error("Ошибка: Не найден такой фильм");
        throw new NotFoundException("Не найден такой фильм");
    }

    private Long generateId() {
        Long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }
}
