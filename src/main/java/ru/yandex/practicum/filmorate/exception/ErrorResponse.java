package ru.yandex.practicum.filmorate.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
class ErrorResponse {
    private final String error;
}
