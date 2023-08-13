package ru.yandex.practicum.filmorate.exception.Mpa;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchMpaException extends NoSuchElementException {
    public NoSuchMpaException(String s) {
        super(s);
    }
}
