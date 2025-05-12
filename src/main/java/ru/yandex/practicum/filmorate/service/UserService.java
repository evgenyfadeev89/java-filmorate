package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User addFriend(Long id, Long friendId) {
        log.info("Добавление пользователя в друзья");
        User user = userStorage.findAll()
                .stream()
                .filter(usr -> usr.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id: " + id + " не найден"));
        User friend = userStorage.findAll()
                .stream()
                .filter(usr -> usr.getId().equals(friendId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с таким friendId: " + friendId + " не найден"));

        user.getFriends().add(friendId);
        userStorage.update(user);

        friend.getFriends().add(id);
        userStorage.update(friend);

        return user;
    }

    public User removeFriend(Long id, Long friendId) {
        log.info("Удаление пользователя из друзей");
        User user = userStorage.findAll()
                .stream()
                .filter(usr -> usr.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id: " + id + " не найден"));
        User friend = userStorage.findAll()
                .stream()
                .filter(usr -> usr.getId().equals(friendId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с таким friendId: " + friendId + " не найден"));

        if (user.getFriends().contains(friend.getId())) {
            user.getFriends().remove(friend.getId());
            userStorage.update(user);
            friend.getFriends().remove(user.getId());
            userStorage.update(friend);
        } else {
            log.info("Ошибка: Пользователя с таким friendId: " + friendId + " нет в друзьях");
        }
        return user;
    }

    public Collection<User> viewFriends(Long id) {
        log.info("Просмотр друзей пользователя");
        User user = userStorage.findAll()
                .stream()
                .filter(usr -> usr.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id: " + id + " не найден"));

        return userStorage.findAll()
                .stream()
                .filter(usr -> user.getFriends().contains(usr.getId()))
                .collect(Collectors.toList());
    }

    public Collection<User> commonFriends(Long id, Long otherId) {
        log.info("Пересечение друзей у пользователей");
        User user = userStorage.findAll()
                .stream()
                .filter(usr -> usr.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id: " + id + " не найден"));
        User otherUser = userStorage.findAll()
                .stream()
                .filter(usr -> usr.getId().equals(otherId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с таким otherId: " + otherId + " не найден"));

        List<Long> lstFriends = user.getFriends().stream().filter(otherUser.getFriends()::contains).toList();

        if (lstFriends.isEmpty()) {
            log.error("Ошибка: Пересечения друзей не найдено");
            throw new NotFoundException("Пересечения друзей не найдено");
        }

        return userStorage.findAll()
                .stream()
                .filter(usr -> lstFriends.contains(usr.getId()))
                .collect(Collectors.toList());
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }
}
