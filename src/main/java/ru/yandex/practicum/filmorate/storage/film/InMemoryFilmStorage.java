package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        log.info("Вызов показа всех фильмов");
        return films.values();
    }

    @Override
    public Film create(Film film) {
        log.info("Создание фильма");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка: дата раньше допустимой");
            throw new ValidationException("Дата раньше допустимой", film.getReleaseDate().toString());
        }

        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }

        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
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
                throw new ValidationException("Длина описания больше допустимой",
                        String.valueOf(newFilm.getDescription().length()));
            }
            if (newFilm.getDuration() != null && newFilm.getDuration() > 0) {
                oldFilm.setDuration(newFilm.getDuration());
            } else if (newFilm.getDuration() < 0) {
                log.error("Ошибка: Продолжительность фильма меньше допустимой");
                throw new ValidationException("Продолжительность фильма меньше допустимой", newFilm.getDuration().toString());
            }
            if (newFilm.getReleaseDate() != null &&
                    !newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            } else if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error("Ошибка: Дата раньше допустимой");
                throw new ValidationException("Дата раньше допустимой", newFilm.getReleaseDate().toString());
            }

            oldFilm.setLikes(newFilm.getLikes());

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
