package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder(toBuilder = true, access = AccessLevel.PUBLIC)
public class User {
    private int id;

    @NotNull(message = "User email should not be null")
    @Email(message = "User should have well-formed email address")
    @NotEmpty(message = "User should not be empty")
    private final String email;

    @NotBlank(message = "User should have login")
    @Pattern(regexp = "\\S+", message = "User login should not have spaces")
    private final String login;

    private final String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "User birthday should not be in future")
    @NotNull
    private final LocalDate birthday;

    @Builder.Default
    @JsonIgnore
    private final Set<Integer> friends = new HashSet<>();

    public String getName() {
        return name == null || name.isBlank() ? login : name;
    }
}
