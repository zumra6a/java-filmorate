package ru.yandex.practicum.filmorate.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchUserException extends NoSuchElementException {
    public NoSuchUserException(String s) {
        super(s);
    }
}
