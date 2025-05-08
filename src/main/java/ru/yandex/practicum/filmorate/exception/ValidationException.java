package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException {
    String parametr;
    String message;

    public ValidationException(String message, String parameter) {
        this.parametr = parameter;
        this.message = message;
    }

    public String getParametr() {
        return parametr;
    }
}
