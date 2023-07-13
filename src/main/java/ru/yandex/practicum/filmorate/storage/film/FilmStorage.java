package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> findOneById(int id);

    List<Film> findAll();

    Film add(Film film);

    Film update(Film film);
}
