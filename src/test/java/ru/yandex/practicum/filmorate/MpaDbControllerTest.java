package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.controller.MpaDbController;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.rowmapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.rowmapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.rowmapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.dal.rowmapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.UserDbService;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Используем in-memory H2
@Transactional
@Import({MpaDbController.class,
        MpaRepository.class,
        MpaRowMapper.class,
        GenreRepository.class,
        GenreRowMapper.class,
        FilmDbService.class,
        FilmRepository.class,
        FilmRowMapper.class,
        UserDbService.class,
        UserRepository.class,
        UserRowMapper.class})
class MpaDbControllerTest {

    @Autowired
    private MpaDbController mpaDbController;

    @Test
    public void testMpaById() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("G");

        Mpa test = mpaDbController.findMpaById(1L);

        assertThat(test.getId()).isEqualTo(mpa.getId());
        assertThat(test.getName()).isEqualTo("G");
    }

    @Test
    public void testFindAllMpa() {
        ArrayList<Mpa> mpaLst = new ArrayList<>();
        Mpa mpa1 = new Mpa();
        mpa1.setId(1L);
        mpa1.setName("G");

        mpaLst.add(mpa1);

        Mpa mpa2 = new Mpa();
        mpa2.setId(2L);
        mpa2.setName("PG");

        mpaLst.add(mpa2);

        Mpa mpa3 = new Mpa();
        mpa3.setId(3L);
        mpa3.setName("PG-13");

        mpaLst.add(mpa3);

        Mpa mpa4 = new Mpa();
        mpa4.setId(4L);
        mpa4.setName("R");

        mpaLst.add(mpa4);

        Mpa mpa5 = new Mpa();
        mpa5.setId(5L);
        mpa5.setName("NC-17");

        mpaLst.add(mpa5);

        assertThat(mpaDbController.findMpaAll()).isEqualTo(mpaLst);
    }
}
