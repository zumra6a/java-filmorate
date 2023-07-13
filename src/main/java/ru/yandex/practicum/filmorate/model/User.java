package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true, access = AccessLevel.PUBLIC)
public class User {
    private int id;

    @NotNull(message = "User email should not be null")
    @Email(message = "User should have well-formed email address")
    private final String email;

    @NotBlank(message = "User should have login")
    @Pattern(regexp = "\\S+", message = "User login should not have spaces")
    private final String login;

    private final String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "User birthday should not be in future")
    private final LocalDate birthday;

    public String getName() {
        return name == null || name.isBlank() ? login : name;
    }
}
