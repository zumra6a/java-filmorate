package ru.yandex.practicum.filmorate.exception.film;

import java.util.NoSuchElementException;

public class NoSuchFilmException extends NoSuchElementException {
    public NoSuchFilmException(String s) {
        super(s);
    }
}
