package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.rowmapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
public class GenreRepository extends BaseRepository<Genre> {

    public GenreRepository(JdbcTemplate jdbc, GenreRowMapper mapper) {
        super(jdbc, mapper, Genre.class);
    }

    private static final String FIND_GENRE_QUERY = "SELECT * FROM genres";
    private static final String FIND_INSERT_FILM_GENRE_QUERY = "INSERT INTO film_genres (film_id, genre_id) " +
            "VALUES (?, ?)";

    public List<Genre> findGenre() {
        return super.findAll(FIND_GENRE_QUERY);
    }

    public void insertFilmGenre(Long filmId, Long genreId) {
        super.update(FIND_INSERT_FILM_GENRE_QUERY,
                filmId,
                genreId);
    }
}