package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.After;

import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotBlank(message = "Film name should not be blank")
    private final String name;

    @NotBlank(message = "Film description should not be blank")
    @Size(max = 200, message = "Film description should not be longer than 200 characters")
    private final String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @After(date = "1895-12-28", message = "Film release date should be after 1895-12-28")
    private final LocalDate releaseDate;

    @Min(value = 0, message = "Film duration should be positive number")
    private final long duration;
}
