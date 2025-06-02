package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.rowmapper.FilmResultSetExtractor;
import ru.yandex.practicum.filmorate.dal.rowmapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {

    private final List<Mpa> mpaList;
    private final List<Genre> genreList;

    public FilmRepository(JdbcTemplate jdbc,
                          FilmRowMapper mapper,
                          MpaRepository mpaRepository,
                          GenreRepository genreRepository
    ) {
        super(jdbc, mapper, Film.class);
        this.mpaList = mpaRepository.findMpa();
        this.genreList = genreRepository.findGenre();
    }

    public List<Mpa> getMpaList() {
        return mpaList;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

    private static final String FIND_ALL_QUERY = "SELECT f.film_id, f.name, f.description, f.release_date, " +
            "f.duration, f.mpa_id, g.genre_id, g.genre, mpa.mpa, fl.user_id FROM films AS f " +
            "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
            "LEFT JOIN mpa_rating AS mpa ON mpa.mpa_id = f.mpa_id " +
            "LEFT JOIN film_likes fl ON fl.film_id = f.film_id";
    private static final String FIND_BY_ID_QUERY = "SELECT f.film_id, f.name, f.description, f.release_date, " +
            "f.duration, f.mpa_id, g.genre_id, g.genre, mpa.mpa, fl.user_id FROM films AS f " +
            "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
            "LEFT JOIN mpa_rating AS mpa ON mpa.mpa_id = f.mpa_id " +
            "LEFT JOIN film_likes fl ON fl.film_id = f.film_id " +
            "WHERE f.film_id = ?";
    private static final String FIND_TOP_LIKE_FILM_QUERY = "SELECT f.film_id, f.name, f.description, f.release_date, " +
            "f.duration, f.mpa_id, g.genre_id, g.genre, mpa.mpa, fl.user_id FROM films AS f " +
            "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
            "LEFT JOIN mpa_rating AS mpa ON mpa.mpa_id = f.mpa_id " +
            "LEFT JOIN film_likes fl ON fl.film_id = f.film_id " +
            "INNER JOIN ( " +
            "SELECT film_id, COUNT(user_id) AS like_count " +
            "FROM film_likes " +
            "GROUP BY film_id " +
            "ORDER BY like_count DESC " +
            "LIMIT ? " +
            ") AS top_films ON f.film_id = top_films.film_id " +
            "ORDER BY top_films.like_count DESC";
    private static final String FIND_BY_NAME_QUERY = "SELECT f.film_id, f.name, f.description, f.release_date," +
            "f.duration, f.mpa_id, g.genre_id, g.genre, mpa.mpa, fl.user_id FROM films AS f " +
            "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
            "LEFT JOIN mpa_rating AS mpa ON mpa.mpa_id = f.mpa_id " +
            "LEFT JOIN film_likes fl ON fl.film_id = f.film_id " +
            "WHERE f.name = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String DELETE_LIKE_FILM_QUERY = "DELETE FROM film_likes WHERE film_id = ? and user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, mpa_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, " +
            "description = ?, " +
            "release_date = ?, " +
            "duration = ?, " +
            "mpa_id = ? " +
            "WHERE film_id = ?";
    private static final String INSERT_LIKE_FILM_QUERY = "INSERT INTO film_likes(film_id, user_id)" +
            "VALUES (?, ?)";

    public Optional<Film> findById(long filmId) {
        return super.findBy(FIND_BY_ID_QUERY, new FilmResultSetExtractor(), filmId);
    }

    public Optional<Film> findByName(String filmName) {
        return super.findBy(FIND_BY_NAME_QUERY, filmName);
    }

    public List<Film> findAll() {
        return super.findAll(FIND_ALL_QUERY, new FilmResultSetExtractor());
    }

    public List<Film> topLikeFilms(Long cnt) {
        return super.findAll(FIND_TOP_LIKE_FILM_QUERY, new FilmResultSetExtractor(), cnt);
    }

    public boolean delete(long userId) {
        return super.delete(DELETE_QUERY, userId);
    }

    public boolean deleteLike(long filmId, long userId) {
        return super.delete(DELETE_LIKE_FILM_QUERY, filmId, userId);
    }

    public Film update(Film film) {
        super.update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        return film;
    }

    public Film save(Film film) {
        Long id = super.insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        super.update(INSERT_LIKE_FILM_QUERY, filmId, userId);
    }
}
