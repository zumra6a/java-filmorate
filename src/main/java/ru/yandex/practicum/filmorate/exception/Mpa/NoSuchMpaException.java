package ru.yandex.practicum.filmorate.exception.Mpa;

import java.util.NoSuchElementException;

public class NoSuchMpaException extends NoSuchElementException {
    public NoSuchMpaException(String s) {
        super(s);
    }
}
