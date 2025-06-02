package ru.yandex.practicum.filmorate.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;

@Data
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String email;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String login;
    private String name;
    private LocalDate birthday;
    private HashSet<Long> friends;
}
