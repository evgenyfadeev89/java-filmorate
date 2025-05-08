package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@Valid @PathVariable Long id, @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike(@Valid @PathVariable Long id, @PathVariable Long userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> topLikeFilms(@RequestParam(required = false, defaultValue = "10") Long count) {
        return filmService.topLikeFilms(count);
    }
}
