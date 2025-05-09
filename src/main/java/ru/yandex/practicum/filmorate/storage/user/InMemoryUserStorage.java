package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        log.info("Вызов показа всех пользователей");
        return users.values();
    }

    @Override
    public User create(@Valid User user) {
        log.info("Создание пользователя");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }

        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        log.info("Обновление пользователя");
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            if (newUser.getEmail() != null &&
                    !newUser.getEmail().isBlank() &&
                    newUser.getEmail().matches(".+@.+\\..+")) {
                oldUser.setEmail(newUser.getEmail());
            } else if (newUser.getEmail() != null) {
                log.error("Ошибка: Некорректное значение email");
                throw new ValidationException("Некорректное значение email", newUser.getEmail());
            }

            if (newUser.getLogin() != null && !newUser.getLogin().contains(" ")) {
                oldUser.setLogin(newUser.getLogin());
            } else if (newUser.getLogin() != null) {
                log.error("Ошибка: Некорректное значение Login");
                throw new ValidationException("Некорректное значение Login", newUser.getLogin());
            }

            if (newUser.getName() == null) {
                if (newUser.getLogin() != null && !newUser.getLogin().contains(" ")) {
                    oldUser.setName(newUser.getLogin());
                } else if (newUser.getLogin() != null) {
                    log.error("Ошибка: Некорректное значение Login");
                    throw new ValidationException("Некорректное значение Login", newUser.getLogin());
                }
            } else {
                oldUser.setName(newUser.getName());
            }

            if (newUser.getBirthday().isBefore(LocalDate.now())) {
                oldUser.setBirthday(newUser.getBirthday());
            } else {
                log.error("Ошибка: Некорректное значение для Дня Рождения");
                throw new ValidationException("Некорректное значение для Дня Рождения", newUser.getBirthday().toString());
            }

            oldUser.setFriends(newUser.getFriends());

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
