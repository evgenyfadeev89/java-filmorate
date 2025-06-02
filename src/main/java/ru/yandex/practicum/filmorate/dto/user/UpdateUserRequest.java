package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;


@Data
public class UpdateUserRequest {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private HashSet<Long> friends;

    public boolean hasValidBirthday() {
        return (birthday.isBefore(LocalDate.now()));
    }

    public boolean hasValidLogin() {
        return !(login == null || login.isBlank() || login.contains(" "));
    }

    public boolean hasValidEmail() {
        return !(email == null || email.isBlank() || !email.contains("@"));
    }
    public boolean hasValidFriends() {
        return (friends == null);
    }
}
