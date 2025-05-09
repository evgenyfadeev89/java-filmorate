package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film addLike(Long id, Long userId) {
        log.info("Добавление нового лайка");
        Film film = filmStorage.findAll()
                .stream()
                .filter(flm -> flm.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм с таким id: " + id + " не найден"));
        User user = userService.findAll()
                .stream()
                .filter(usr -> usr.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с таким userId: " + userId + " не найден"));

        film.getLikes().add(userId);
        filmStorage.update(film);

        return film;
    }

    public Film removeLike(Long id, Long userId) {
        log.info("Удаление лайка");
        Film film = filmStorage.findAll()
                .stream()
                .filter(flm -> flm.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм с таким id: " + id + " не найден"));
        User user = userService.findAll()
                .stream()
                .filter(usr -> usr.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с таким userId: " + userId + " не найден"));

        if (film.getLikes().contains(userId)) {
            film.getLikes().remove(userId);
            filmStorage.update(film);

            return film;
        } else {
            log.error("Ошибка: У фильма c id: " + id + " нет лайка от пользователя с id: " + userId);
            throw new NotFoundException("У фильма c id: " + id + " нет лайка от пользователя с id: " + userId);
        }
    }

    public List<Film> topLikeFilms(Long count) {
        if (count <= 0) {
            throw new ValidationException("Некорректное значение количества фильмов", count.toString());
        }
        return filmStorage.findAll().stream()
                .filter(film -> film.getLikes() != null)
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }
}
