package ru.yandex.practicum.filmorate.dal.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;


@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        Mpa mpa = new Mpa();
        Genre genre = new Genre();
        HashSet<Long> likes;

        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(LocalDate.parse(resultSet.getString("release_date")));
        film.setDuration(resultSet.getLong("duration"));
        mpa.setId(resultSet.getLong("mpa_id"));
        mpa.setName(resultSet.getString("mpa"));

        if (film.getGenres() == null) {
            film.setGenres(new LinkedHashSet<>());
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }

        film.getLikes().add(resultSet.getLong("user_id"));
        genre.setId(resultSet.getLong("genre_id"));
        genre.setName(resultSet.getString("genre"));
        if (film.getGenres().stream().noneMatch(g -> g.getId().equals(genre.getId()))) {
            film.getGenres().add(genre);
        }

        film.setMpa(mpa);

        return film;
    }
}