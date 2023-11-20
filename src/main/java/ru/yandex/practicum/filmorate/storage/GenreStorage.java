package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreStorage {
    List<Genre> findAll();

    Optional<Genre> findOneById(int id);

    Genre add(Genre genre);

    Genre update(Genre genre);

    void deleteOneById(int id);

    List<Genre> findAllByFilmId(int filId);

    void deleteAllByFilmId(int filId);

    void fetchFilmGenres(Film film);

    void fetchFilmGenres(List<Film> films);
}