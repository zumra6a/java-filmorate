package ru.yandex.practicum.filmorate.exception;

import java.util.NoSuchElementException;

public class NoSuchModelException extends NoSuchElementException {
    public NoSuchModelException(String s) {
        super(s);
    }
}
