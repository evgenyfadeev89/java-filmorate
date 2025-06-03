package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ValidationParameterException extends RuntimeException {
    public ValidationParameterException(String message) {
        super(message);
    }
}