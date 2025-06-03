package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmDbService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MpaDbController {
    private final FilmDbService filmDbService;

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

}
