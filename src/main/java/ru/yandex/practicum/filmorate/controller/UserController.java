package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findall() {
        log.info("Вызов показа всех пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создание пользователя");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Обновление пользователя");
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            if (newUser.getEmail() != null &&
                    !newUser.getEmail().isBlank() &&
                    newUser.getEmail().matches(".+@.+\\..+")) {
                oldUser.setEmail(newUser.getEmail());
            } else if (newUser.getEmail() != null) {
                log.error("Ошибка: Некорректное значение email");
                throw new ValidationException("Некорректное значение email");
            }

            if (newUser.getLogin() != null && !newUser.getLogin().contains(" ")) {
                oldUser.setLogin(newUser.getLogin());
            } else if (newUser.getLogin() != null) {
                log.error("Ошибка: Некорректное значение Login");
                throw new ValidationException("Некорректное значение Login");
            }

            if (newUser.getName() == null) {
                if (newUser.getLogin() != null && !newUser.getLogin().contains(" ")) {
                    oldUser.setName(newUser.getLogin());
                } else if (newUser.getLogin() != null) {
                    log.error("Ошибка: Некорректное значение Login");
                    throw new ValidationException("Некорректное значение Login");
                }
            } else {
                oldUser.setName(newUser.getName());
            }

            if (newUser.getBirthday().isBefore(LocalDate.now())) {
                oldUser.setBirthday(newUser.getBirthday());
            } else {
                log.error("Ошибка: Некорректное значение для Дня Рождения");
                throw new ValidationException("Некорректное значение для Дня Рождения");
            }

            return oldUser;
        }

        log.error("Ошибка: Не найден такой пользователь");
        throw new NotFoundException("Не найден такой пользователь");
    }


    private Long generateId() {
        Long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }
}