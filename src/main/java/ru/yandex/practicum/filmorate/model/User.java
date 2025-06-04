package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;

@Data
public class User {

    private Long id;

    @NotBlank(message = "email обязателен для заполнения")
    @Email(message = "не соответствует параметрам email")
    private String email;

    @NotBlank(message = "Логин обязателен для заполнения")
    @Pattern(regexp = "\\S+", message = "Не допускаются пробелы в логине")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата не может быть в прошлом")
    private LocalDate birthday;

    private HashSet<Long> friends;
}
