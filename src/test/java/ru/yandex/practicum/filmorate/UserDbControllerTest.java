package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.controller.UserDbController;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.rowmapper.UserRowMapper;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.UserDbService;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import({UserDbController.class,
        UserDbService.class,
        UserRepository.class,
        UserRowMapper.class})
class UserDbControllerTest {

    @Autowired
    private UserDbController userDbController;

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
    public void testUpdateUser() {
        NewUserRequest usr = new NewUserRequest();
        usr.setEmail("new@example.com");
        usr.setLogin("new_login");
        usr.setName("New User");
        usr.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto usrDto = userDbController.create(usr);

        UpdateUserRequest usrUpd = new UpdateUserRequest();
        usrUpd.setId(usrDto.getId());
        usrUpd.setEmail("newUpd@example.com");
        usrUpd.setLogin("upd_login");
        usrUpd.setName("New UserUpd");
        usrUpd.setBirthday(LocalDate.of(1990, 5, 15));

        userDbController.update(usrUpd);
        assertThat(userDbController.findUserById(usrDto.getId()).getLogin()).isEqualTo("upd_login");
    }

    @Test
    public void testDeleteUser() {
        NewUserRequest usr = new NewUserRequest();
        usr.setEmail("new@example.com");
        usr.setLogin("new_login");
        usr.setName("New User");
        usr.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto createdUser = userDbController.create(usr);

        assertThat(userDbController.findUserById(createdUser.getId())).isEqualTo(createdUser);

        userDbController.delete(createdUser.getId());
        assertThrows(NotFoundException.class, () -> userDbController.findUserById(createdUser.getId()));
    }

    @Test
    public void testFindAllUsers() {
        NewUserRequest usr = new NewUserRequest();
        usr.setEmail("new@example.com");
        usr.setLogin("new_login");
        usr.setName("New User");
        usr.setBirthday(LocalDate.of(1995, 5, 15));

        NewUserRequest usr2 = new NewUserRequest();
        usr2.setEmail("mail@mail.ru");
        usr2.setLogin("new_login2");
        usr2.setName("New User2");
        usr2.setBirthday(LocalDate.of(1995, 5, 15));

        userDbController.create(usr);
        userDbController.create(usr2);
        assertThat(userDbController.findAll()).isNotNull();
        assertThat(userDbController.findAll().size()).isEqualTo(2);
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

    @Test
    public void testAddFriend() {
        NewUserRequest usr = new NewUserRequest();
        usr.setEmail("new@example.com");
        usr.setLogin("new_login");
        usr.setName("New User");
        usr.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto usrDto = userDbController.create(usr);

        NewUserRequest usrUpd = new NewUserRequest();
        usrUpd.setEmail("new2@example.com");
        usrUpd.setLogin("upd_login");
        usrUpd.setName("Upd User");
        usrUpd.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto usrUpdDto = userDbController.create(usrUpd);

        userDbController.addFriend(usrDto.getId(), usrUpdDto.getId());

        assertThat(userDbController.viewFriends(usrDto.getId()).get(0).getId()).isEqualTo(usrUpdDto.getId());
    }

    @Test
    public void testRemoveFriend() {
        NewUserRequest usr = new NewUserRequest();
        usr.setEmail("new@example.com");
        usr.setLogin("new_login");
        usr.setName("New User");
        usr.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto usrDto = userDbController.create(usr);

        NewUserRequest usrUpd = new NewUserRequest();
        usrUpd.setEmail("new2@example.com");
        usrUpd.setLogin("upd_login");
        usrUpd.setName("Upd User");
        usrUpd.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto usrUpdDto = userDbController.create(usrUpd);

        userDbController.addFriend(usrDto.getId(), usrUpdDto.getId());

        assertThat(userDbController.removeFriend(usrDto.getId(), usrUpdDto.getId())).isTrue();
        assertThat(userDbController.viewFriends(usrDto.getId())).isEqualTo(new ArrayList<>());
    }

    @Test
    public void testCommonFriends() {
        NewUserRequest usr = new NewUserRequest();
        usr.setEmail("new@example.com");
        usr.setLogin("new_login");
        usr.setName("New User");
        usr.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto usrDto = userDbController.create(usr);

        NewUserRequest usrUpd = new NewUserRequest();
        usrUpd.setEmail("new2@example.com");
        usrUpd.setLogin("upd_login");
        usrUpd.setName("Upd User");
        usrUpd.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto usrUpdDto = userDbController.create(usrUpd);

        NewUserRequest usrCommonUpd = new NewUserRequest();
        usrCommonUpd.setEmail("usrCommon@example.com");
        usrCommonUpd.setLogin("usrCommon");
        usrCommonUpd.setName("usrCommon Name");
        usrCommonUpd.setBirthday(LocalDate.of(1995, 5, 15));

        UserDto usrCommonDto = userDbController.create(usrCommonUpd);

        userDbController.addFriend(usrDto.getId(), usrCommonDto.getId());
        userDbController.addFriend(usrUpdDto.getId(), usrCommonDto.getId());

        assertThat(userDbController.commonFriends(usrDto.getId()
                , usrUpdDto.getId()).get(0).getId()).isEqualTo(usrCommonDto.getId());
    }
}
