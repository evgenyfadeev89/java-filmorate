package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.controller.FilmDbController;
import ru.yandex.practicum.filmorate.controller.UserDbController;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.rowmapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.rowmapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.rowmapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.dal.rowmapper.UserRowMapper;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.UserDbService;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import({FilmDbController.class,
        FilmDbService.class,
        FilmRepository.class,
        FilmRowMapper.class,
        MpaRepository.class,
        MpaRowMapper.class,
        GenreRepository.class,
        GenreRowMapper.class,
        UserDbController.class,
        UserDbService.class,
        UserRepository.class,
        UserRowMapper.class})
class FilmDbControllerTest {

    @Autowired
    private FilmDbController filmDbController;
    @Autowired
    private UserDbController userDbController;

    @Test
    public void testCreateFilm() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("G");

        NewFilmRequest film = new NewFilmRequest();
        film.setName("New Film");
        film.setDescription("new Description");
        film.setReleaseDate(LocalDate.of(1995, 5, 15));
        film.setDuration(15L);
        film.setMpa(mpa);

        FilmDto createdFilm = filmDbController.create(film);

        assertThat(createdFilm.getId()).isNotNull();
    }

    @Test
    public void testFindFilmById() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("G");

        NewFilmRequest film = new NewFilmRequest();
        film.setName("New Film");
        film.setDescription("new Description");
        film.setReleaseDate(LocalDate.of(1995, 5, 15));
        film.setDuration(15L);
        film.setMpa(mpa);

        Long id = filmDbController.create(film).getId();

        FilmDto filmDto = filmDbController.findFilmById(id);
        assertThat(filmDto).isNotNull();
        assertThat(filmDto.getId()).isEqualTo(id);
        assertThat(filmDto.getName()).isEqualTo("New Film");
        assertThat(filmDto.getDescription()).isEqualTo("new Description");
        assertThat(filmDto.getMpa()).isEqualTo(mpa);
    }

    @Test
    public void testFindAllFilms() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("G");

        NewFilmRequest film = new NewFilmRequest();
        film.setName("New Film");
        film.setDescription("new Description");
        film.setReleaseDate(LocalDate.of(1995, 5, 15));
        film.setDuration(15L);
        film.setMpa(mpa);

        filmDbController.create(film);

        NewFilmRequest film2 = new NewFilmRequest();
        film2.setName("New Film2");
        film2.setDescription("new Description2");
        film2.setReleaseDate(LocalDate.of(1995, 5, 15));
        film2.setDuration(10L);
        film2.setMpa(mpa);

        filmDbController.create(film2);

        assertThat(filmDbController.findAll()).isNotNull();
        assertThat(filmDbController.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testUpdateUser() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("G");

        NewFilmRequest film = new NewFilmRequest();
        film.setName("New Film");
        film.setDescription("new Description");
        film.setReleaseDate(LocalDate.of(1995, 5, 15));
        film.setDuration(15L);
        film.setMpa(mpa);

        FilmDto filmDto = filmDbController.create(film);

        UpdateFilmRequest film2 = new UpdateFilmRequest();
        film2.setId(filmDto.getId());
        film2.setName("New Film2");
        film2.setDescription("new Description2");
        film2.setReleaseDate(LocalDate.of(1995, 5, 15));
        film2.setDuration(10L);
        film2.setMpa(mpa);

        filmDbController.update(film2);
        assertThat(filmDbController.findFilmById(filmDto.getId())
                .getDescription()).isEqualTo("new Description2");
    }

    @Test
    public void testDeleteFilm() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("G");

        NewFilmRequest film = new NewFilmRequest();
        film.setName("New Film");
        film.setDescription("new Description");
        film.setReleaseDate(LocalDate.of(1995, 5, 15));
        film.setDuration(15L);
        film.setMpa(mpa);

        FilmDto filmDto = filmDbController.create(film);

        assertThat(filmDbController.findFilmById(filmDto.getId())).isEqualTo(filmDto);

        filmDbController.delete(filmDto.getId());
        assertThrows(NotFoundException.class, () -> filmDbController.findFilmById(filmDto.getId()));
    }


    @Test
    public void testAddLike() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("G");

        NewFilmRequest film = new NewFilmRequest();
        film.setName("New Film");
        film.setDescription("new Description");
        film.setReleaseDate(LocalDate.of(1995, 5, 15));
        film.setDuration(15L);
        film.setMpa(mpa);

        FilmDto filmDto = filmDbController.create(film);

        NewUserRequest usr = new NewUserRequest();
        usr.setEmail("new@example.com");
        usr.setLogin("new_login");
        usr.setName("New User");
        usr.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto createdUser = userDbController.create(usr);

        Set<Long> userIds = new HashSet<>();
        userIds.add(createdUser.getId());

        assertThat(filmDbController.addLike(createdUser.getId(), filmDto.getId())).isEqualTo(true);
        assertThat(filmDbController.findFilmById(filmDto.getId()).getLikes())
                .isEqualTo(userIds);
    }

    @Test
    public void testRemoveLike() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("G");

        NewFilmRequest film = new NewFilmRequest();
        film.setName("New Film");
        film.setDescription("new Description");
        film.setReleaseDate(LocalDate.of(1995, 5, 15));
        film.setDuration(15L);
        film.setMpa(mpa);

        FilmDto filmDto = filmDbController.create(film);

        NewUserRequest usr = new NewUserRequest();
        usr.setEmail("new@example.com");
        usr.setLogin("new_login");
        usr.setName("New User");
        usr.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto createdUser = userDbController.create(usr);

        filmDbController.addLike(createdUser.getId(), filmDto.getId());

        assertThat(filmDbController.removeLike(createdUser.getId(), filmDto.getId())).isEqualTo(true);
        assertThat(filmDbController.findFilmById(filmDto.getId()).getLikes()).isEqualTo(new HashSet<>());
    }

    @Test
    public void testTopLikeFilm() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("G");

        NewFilmRequest film = new NewFilmRequest();
        film.setName("New Film");
        film.setDescription("new Description");
        film.setReleaseDate(LocalDate.of(1995, 5, 15));
        film.setDuration(15L);
        film.setMpa(mpa);

        FilmDto filmDto = filmDbController.create(film);

        NewFilmRequest film2 = new NewFilmRequest();
        film2.setName("New Film2");
        film2.setDescription("new Description2");
        film2.setReleaseDate(LocalDate.of(1995, 5, 15));
        film2.setDuration(15L);
        film2.setMpa(mpa);

        FilmDto filmDto2 = filmDbController.create(film2);

        List<FilmDto> filmLst = new ArrayList<>();


        NewUserRequest usr = new NewUserRequest();
        usr.setEmail("new@example.com");
        usr.setLogin("new_login");
        usr.setName("New User");
        usr.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto createdUser = userDbController.create(usr);

        NewUserRequest usr2 = new NewUserRequest();
        usr2.setEmail("new2@example.com");
        usr2.setLogin("new_login2");
        usr2.setName("New User2");
        usr2.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto createdUser2 = userDbController.create(usr2);

        filmDbController.addLike(filmDto.getId(), createdUser.getId());
        filmDbController.addLike(filmDto2.getId(), createdUser.getId());
        filmDbController.addLike(filmDto2.getId(), createdUser2.getId());

        HashSet<Long> likes = new HashSet<>();
        likes.add(createdUser.getId());
        filmDto.setLikes(likes);

        HashSet<Long> likes2 = new HashSet<>();
        likes2.add(createdUser.getId());
        likes2.add(createdUser2.getId());
        filmDto2.setLikes(likes2);

        filmLst.add(filmDto2);
        filmLst.add(filmDto);

        assertThat(filmDbController.topLikeFilms(10L)).isEqualTo(filmLst);
        assertThat(filmDbController.topLikeFilms(10L).get(0)).isEqualTo(filmDto2);
    }
}
