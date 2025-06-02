package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmDbService;

import java.util.List;

@Qualifier
@RestController
@Slf4j
@RequiredArgsConstructor
public class FilmDbController {

    private final FilmDbService filmDbService;

    @GetMapping("/films")
    public List<FilmDto> findAll() {
        return filmDbService.findAll();
    }

    @GetMapping("/films/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto findFilmById(@PathVariable("id") Long id) {
        return filmDbService.getFilmById(id);
    }

    @PostMapping("/films")
    public FilmDto create(@Valid @RequestBody NewFilmRequest newFilmRequest) {
        return filmDbService.create(newFilmRequest);
    }

    @PutMapping("/films")
    public FilmDto update(@Valid @RequestBody UpdateFilmRequest updateFilmRequest) {
        return filmDbService.update(updateFilmRequest);
    }

    @DeleteMapping("/films/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        return filmDbService.delete(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@Valid @PathVariable Long id, @PathVariable Long userId) {
        filmDbService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public boolean removeLike(@Valid @PathVariable Long id, @PathVariable Long userId) {
        return filmDbService.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<FilmDto> topLikeFilms(@RequestParam(required = false, defaultValue = "10") Long count) {
        return filmDbService.topLikeFilms(count);
    }

    @GetMapping("/mpa/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa findMpaById(@PathVariable("id") Long id) {
        return filmDbService.getMpaById(id);
    }

    @GetMapping("/mpa")
    @ResponseStatus(HttpStatus.OK)
    public List<Mpa> findMpaAll() {
        return filmDbService.getMpaAll();
    }

    @GetMapping("/genres/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Genre findGenreById(@PathVariable("id") Long id) {
        return filmDbService.getGenreById(id);
    }

    @GetMapping("/genres")
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> findGenreAll() {
        return filmDbService.getGenreAll();
    }
}
