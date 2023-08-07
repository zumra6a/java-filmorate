package ru.yandex.practicum.filmorate.exception.film;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateFilmException extends RuntimeException {
    public DuplicateFilmException(String s) {
        super(s);
    }
}
