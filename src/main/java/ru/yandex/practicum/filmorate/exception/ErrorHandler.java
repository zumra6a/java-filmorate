package ru.yandex.practicum.filmorate.exception;

import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.yandex.practicum.filmorate.exception.Genre.DuplicateGenreException;
import ru.yandex.practicum.filmorate.exception.Genre.NoSuchGenreException;
import ru.yandex.practicum.filmorate.exception.Mpa.DuplicateMpaException;
import ru.yandex.practicum.filmorate.exception.Mpa.NoSuchMpaException;
import ru.yandex.practicum.filmorate.exception.film.DuplicateFilmException;
import ru.yandex.practicum.filmorate.exception.film.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exception.user.DuplicateUserException;
import ru.yandex.practicum.filmorate.exception.user.NoSuchUserException;

@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationError(final ValidationException e) {
        return Map.of(
                "error", "Ошибка валидации.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationError(final ConstraintViolationException e) {
        return Map.of(
                "error", "Ошибка валидации.",
                "errorMessage", e.getMessage()
        );
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        return Map.of(
                "error", "Ошибка валидации.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchUserError(final NoSuchUserException e) {
        return Map.of(
                "error", "Пользователь не найден.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicateUserError(final DuplicateUserException e) {
        return Map.of(
                "error", "Пользователь дублируется.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchFilmError(final NoSuchFilmException e) {
        return Map.of(
                "error", "Фильм не найден.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicateFilmError(final DuplicateFilmException e) {
        return Map.of(
                "error", "Фильм дублируется.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchMpaError(final NoSuchMpaException e) {
        return Map.of(
                "error", "Рейтинг не найден.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicateGenreError(final DuplicateGenreException e) {
        return Map.of(
                "error", "Жанр дублируется.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchGenreError(final NoSuchGenreException e) {
        return Map.of(
                "error", "Жанр не найден.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicateMpaError(final DuplicateMpaException e) {
        return Map.of(
                "error", "Рейтинг дублируется.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(final Throwable e) {
        return Map.of(
                "error", "Ошибка выполнения.",
                "errorMessage", e.getMessage()
        );
    }
}
