package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.controller.GenreDbController;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.rowmapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.rowmapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.rowmapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.dal.rowmapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.UserDbService;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Используем in-memory H2
@Transactional
@Import({GenreDbController.class,
        GenreRepository.class,
        GenreRowMapper.class,
        FilmDbService.class,
        FilmRepository.class,
        FilmRowMapper.class,
        UserDbService.class,
        UserRepository.class,
        UserRowMapper.class,
        MpaRepository.class,
        MpaRowMapper.class})
class GenreDbControllerTest {

    @Autowired
    private GenreDbController genreDbController;

    @Test
    public void testGenreById() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Комедия");

        Genre test = genreDbController.findGenreById(1L);

        assertThat(test.getId()).isEqualTo(genre.getId());
        assertThat(test.getName()).isEqualTo("Комедия");
    }

    @Test
    public void testFindAllGenres() {
        ArrayList<Genre> genreLst = new ArrayList<>();
        Genre genre1 = new Genre();
        genre1.setId(1L);
        genre1.setName("Комедия");

        genreLst.add(genre1);

        Genre genre2 = new Genre();
        genre2.setId(2L);
        genre2.setName("Драма");

        genreLst.add(genre2);

        Genre genre3 = new Genre();
        genre3.setId(3L);
        genre3.setName("Мультфильм");

        genreLst.add(genre3);

        Genre genre4 = new Genre();
        genre4.setId(4L);
        genre4.setName("Триллер");

        genreLst.add(genre4);

        Genre genre5 = new Genre();
        genre5.setId(5L);
        genre5.setName("Документальный");

        genreLst.add(genre5);

        Genre genre6 = new Genre();
        genre6.setId(6L);
        genre6.setName("Боевик");

        genreLst.add(genre6);

        assertThat(genreDbController.findGenreAll()).isEqualTo(genreLst);
    }
}
