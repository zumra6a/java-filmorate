package ru.yandex.practicum.filmorate.model;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("film name should not be blank")
    public void testBlankName() {
        final Film film = new Film(1, "", "description", LocalDate.now(), 120);

        assertTrue(validator
                .validateProperty(film, "name")
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("Film name should not be blank")));
    }

    @Test
    @DisplayName("film description should not be blank")
    public void testBlankDescription() {
        final Film film = new Film(1, "name", "", LocalDate.now(), 120);

        assertTrue(validator
                .validateProperty(film, "description")
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("Film description should not be blank")));
    }

    @Test
    @DisplayName("film description length should not be longer than 200")
    public void testLongDescription() {
        final String description = "-".repeat(201);
        final Film film = new Film(1, "name", description, LocalDate.now(), 120);

        assertTrue(validator
                .validateProperty(film, "description")
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("Film description should not be longer than 200 characters")));
    }

    @Test
    @DisplayName("film release date should be after 1895-12-28")
    public void testReleaseDate() {
        final LocalDate releaseDate = LocalDate.parse("1895-12-28");
        final Film film = new Film(1, "name", "description", releaseDate, 120);

        assertTrue(validator
                .validateProperty(film, "releaseDate")
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("Film release date should be after 1895-12-28")));
    }

    @Test
    @DisplayName("film duration should be positive")
    public void testDuration() {
        final Film film = new Film(0, "name", "description", LocalDate.now(), -1);

        assertTrue(validator
                .validateProperty(film, "duration")
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("Film duration should be positive number")));
    }
}