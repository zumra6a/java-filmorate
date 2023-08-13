package ru.yandex.practicum.filmorate.exception.Genre;

public class DuplicateGenreException extends RuntimeException {
    public DuplicateGenreException(String s) {
        super(s);
    }
}
