package ru.yandex.practicum.filmorate.exception.Genre;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchGenreException extends NoSuchElementException {
    public NoSuchGenreException(String s) {
        super(s);
    }
}
