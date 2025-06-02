package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;
import java.util.stream.Collectors;

@Qualifier
@Service
@RequiredArgsConstructor
@Slf4j
public class FilmDbService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;

    public void addLike(Long id, Long userId) {
        log.info("Добавление нового лайка");
        filmRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));

        userRepository.findById(userId).
                orElseThrow(() -> new NotFoundException("Пользователь с таким userId: " + userId + " не найден"));

        filmRepository.addLike(id, userId);
    }

    public boolean removeLike(Long filmId, Long userId) {
        log.info("Удаление лайка");
        filmRepository.findById(filmId).
                orElseThrow(() -> new NotFoundException("Фильм с id " + filmId + " не найден"));

        userRepository.findById(userId).
                orElseThrow(() -> new NotFoundException("Пользователь с таким userId: " + userId + " не найден"));

        return filmRepository.deleteLike(filmId, userId);
    }

    public List<FilmDto> topLikeFilms(Long count) {
        if (count <= 0) {
            throw new ValidationException("Некорректное значение количества фильмов", count.toString());
        }
        return filmRepository.topLikeFilms(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto getFilmById(Long filmId) {
        return filmRepository.findById(filmId).
                map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильм не найден с ID: " + filmId));
    }

    public List<FilmDto> findAll() {
        return filmRepository.findAll()
                .stream()
                .map(FilmMapper::mapToFilmDto).toList();
    }

    public FilmDto create(NewFilmRequest request) {
        if (!request.hasName()) {
            throw new ConditionsNotMetException("Имя фильма должно быть указано");
        }
        if (!request.hasReleaseDate()) {
            throw new ConditionsNotMetException("Дата фильма должна быть указана");
        }
        if (!request.hasDuration()) {
            throw new ConditionsNotMetException("Продолжительность фильма должна быть больше 1");
        }
        if (!request.hasMpa()) {
            throw new ConditionsNotMetException("Рейтинг фильма должен быть указан");
        }
        if (!request.hasDescription()) {
            throw new ConditionsNotMetException("Описание фильма превышает 200 символов: "
                    + request.getDescription().length());
        }

        List<Mpa> mpalst = filmRepository.getMpaList();
        List<Genre> genrelst = filmRepository.getGenreList();
        if (!(request.getGenres() == null || request.getGenres().isEmpty())) {
            List<Long> requestIdGenres = request.getGenres().stream().map(Genre::getId).toList();
            List<Long> genresId = genrelst.stream().map(Genre::getId).toList();

            if (!genresId.containsAll(requestIdGenres)) {
                throw new NotFoundException("Такого id жанра нет");
            }
        } else {
            request.setGenres(new LinkedHashSet<>());
        }

        mpalst.stream()
                .filter(item -> item.getId().equals(request.getMpa().getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Такого id рейтинга нет"));

        Film film = FilmMapper.mapToFilm(request);
        film.setId(filmRepository.save(film).getId());

        film.getGenres().forEach(genre ->
                genreRepository.insertFilmGenre(film.getId(), genre.getId())
        );

        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto update(UpdateFilmRequest request) {
        Film updatedFilm = filmRepository.findById(request.getId())
                .map(film -> FilmMapper.updateFilmFields(film, request))
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        if (request.hasName()) {
            updatedFilm.setName(request.getName());
        }
        if (request.hasReleaseDate()) {
            updatedFilm.setReleaseDate(request.getReleaseDate());
        }
        if (request.hasDuration()) {
            updatedFilm.setDuration(request.getDuration());
        }
        if (request.hasMpa()) {
            updatedFilm.setMpa(request.getMpa());
        }
        if (request.hasDescription()) {
            updatedFilm.setDescription(request.getDescription());
        }
        if (request.hasGenres()) {
            updatedFilm.setGenres(request.getGenres());
        }

        updatedFilm = filmRepository.update(updatedFilm);

        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public boolean delete(Long filmId) {
        return filmRepository.delete(filmId);
    }

    public List<Mpa> getMpaAll() {
        return new ArrayList<>(filmRepository.getMpaList());
    }

    public Mpa getMpaById(Long id) {
        return filmRepository.getMpaList()
                .stream()
                .filter(mpa -> mpa.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Рейтинг с таким id не найден"));
    }

    public List<Genre> getGenreAll() {
        return new ArrayList<>(filmRepository.getGenreList());
    }

    public Genre getGenreById(Long id) {
        return filmRepository.getGenreList()
                .stream()
                .filter(mpa -> mpa.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Рейтинг с таким id не найден"));
    }
}
