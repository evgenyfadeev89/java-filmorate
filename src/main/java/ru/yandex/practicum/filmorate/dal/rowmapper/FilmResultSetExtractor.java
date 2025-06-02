package ru.yandex.practicum.filmorate.dal.rowmapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FilmResultSetExtractor implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Film> filmMap = new LinkedHashMap<>();

        while (rs.next()) {
            Long filmId = rs.getLong("film_id");

            Film film = filmMap.get(filmId);
            if (film == null) {
                film = new Film();
                film.setId(filmId);
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setDuration(rs.getLong("duration"));

                Mpa mpa = new Mpa();
                mpa.setId(rs.getLong("mpa_id"));
                mpa.setName(rs.getString("mpa"));
                film.setMpa(mpa);

                film.setGenres(new LinkedHashSet<>());
                film.setLikes(new HashSet<>());

                filmMap.put(filmId, film);
            }

            Long genreId = rs.getLong("genre_id");
            if (genreId != 0) { // если genre_id не null
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(rs.getString("genre"));

                if (film.getGenres().stream().noneMatch(g -> g.getId().equals(genreId))) {
                    film.getGenres().add(genre);
                }
            }

            Long userId = rs.getLong("user_id");
            if (!rs.wasNull() && userId != 0) {
                film.getLikes().add(userId);
            }
        }

        return new ArrayList<>(filmMap.values());
    }
}