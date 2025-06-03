package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.UserDbService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GenreDbController {
    private final UserDbService userDbService;

    private final FilmDbService filmDbService;

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
