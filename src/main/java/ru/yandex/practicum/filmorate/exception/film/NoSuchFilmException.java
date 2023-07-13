package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchFilmException extends NoSuchElementException {
    public NoSuchFilmException(String s) {
        super(s);
    }
}
