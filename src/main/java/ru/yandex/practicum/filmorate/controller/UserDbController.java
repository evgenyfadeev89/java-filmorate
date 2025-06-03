package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDbService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserDbController {
    private final UserDbService userDbService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findAll() {
        return userDbService.findAll();
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto findUserById(@PathVariable("id") Long id) {
        return userDbService.getUserById(id);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody NewUserRequest userRequest) {
        return userDbService.create(userRequest);
    }

    @PutMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return userDbService.update(updateUserRequest);
    }

    @DeleteMapping("/users/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        return userDbService.delete(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userDbService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public boolean removeFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        return userDbService.removeFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> viewFriends(@PathVariable("id") Long id) {
        return userDbService.viewFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> commonFriends(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        return userDbService.commonFriends(id, otherId);
    }
}
