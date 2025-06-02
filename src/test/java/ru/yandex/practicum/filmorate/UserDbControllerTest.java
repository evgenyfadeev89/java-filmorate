package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.controller.UserDbController;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Используем in-memory H2
@Transactional
class UserDbControllerTest {

    @Autowired
    private UserDbController userDbController;

    @Test
    public void testFindUserById() {
        NewUserRequest usr = new NewUserRequest();
        usr.setEmail("mail@mail.ru");
        usr.setLogin("new_login");
        usr.setName("New User");
        usr.setBirthday(LocalDate.of(1995, 5, 15));
        Long id = userDbController.create(usr).getId();

        UserDto user = userDbController.findUserById(id);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getEmail()).isEqualTo("mail@mail.ru");
        assertThat(user.getLogin()).isEqualTo("new_login");
    }

    @Test
    public void testCreateUser() {
        NewUserRequest usr = new NewUserRequest();
        usr.setEmail("new@example.com");
        usr.setLogin("new_login");
        usr.setName("New User");
        usr.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto createdUser = userDbController.create(usr);
        assertThat(createdUser.getId()).isNotNull();
    }

    @Test
    public void testFindNonExistentUser() {
        assertThrows(NotFoundException.class, () -> userDbController.findUserById(999L));
    }

    @Test
    public void testCreateUserWithInvalidEmail() {
        NewUserRequest usr = new NewUserRequest();
        usr.setEmail("invalid_email");
        usr.setLogin("new_login");
        usr.setName("New User");
        usr.setBirthday(LocalDate.of(1995, 5, 15));

        assertThrows(RuntimeException.class, () -> userDbController.create(usr));
    }
}
