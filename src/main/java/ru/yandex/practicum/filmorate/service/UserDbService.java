package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationParameterException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDbService {
    private final UserRepository userRepository;

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
    }

    public UserDto create(NewUserRequest request) {
        if (!request.hasValidEmail()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (!request.hasValidLogin()) {
            throw new ConditionsNotMetException("Логин должен быть указан");
        }
        if (!request.hasValidBirthday()) {
            throw new ConditionsNotMetException("День рождения позже текущей даты");
        }

        Optional<User> alreadyExistEmailUser = userRepository.findByEmail(request.getEmail());
        if (alreadyExistEmailUser.isPresent()) {
            throw new DuplicatedDataException("Данный имейл уже используется");
        }
        Optional<User> alreadyExistLoginUser = userRepository.findByLogin(request.getLogin());
        if (alreadyExistLoginUser.isPresent()) {
            throw new DuplicatedDataException("Данный логин уже используется");
        }

        User user = UserMapper.mapToUser(request);
        user = userRepository.save(user);

        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UpdateUserRequest request) {
        User updatedUser = userRepository.findById(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (!request.hasValidBirthday() ||
                !request.hasValidLogin() ||
                !request.hasValidEmail()) {
            throw new ValidationParameterException("Некорректные данные нового пользователя");
        }

        updatedUser = userRepository.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public boolean delete(Long userId) {
        return userRepository.delete(userId);
    }

    public void addFriend(Long id, Long friendId) {
        log.info("Добавление пользователя в друзья");

        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким userId: " + id + " не найден"));

        userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким userId: " + friendId + " не найден"));

        userRepository.addFriend(id, friendId);
    }

    public boolean removeFriend(Long userId, Long friendId) {
        log.info("Удаление из друзей");
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с userId " + userId + " не найден"));

        userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким friendId: " + friendId + " не найден"));

        return userRepository.deleteFriend(userId, friendId);
    }

    public List<User> viewFriends(Long id) {
        log.info("Просмотр друзей пользователя");
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));

        return userRepository.viewFriends(id);
    }

    public List<User> commonFriends(Long userId, Long otherId) {
        log.info("Пересечение друзей у пользователей");
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с userId " + userId + " не найден"));

        userRepository.findById(otherId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким friendId: " + otherId + " не найден"));

        return userRepository.viewCommonFriends(userId, otherId);
    }
}
