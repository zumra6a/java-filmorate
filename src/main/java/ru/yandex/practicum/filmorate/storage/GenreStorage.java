package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> findAll();

    Optional<Genre> findOneById(int id);

    Genre add(Genre genre);

    Genre update(Genre genre);

    void deleteOneById(int id);

    List<Genre> findAllByFilmId(int filId);

    void deleteAllByFilmId(int filId);
}