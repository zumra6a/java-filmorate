package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.DuplicateFilmException;
import ru.yandex.practicum.filmorate.exception.film.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static int id = 1;

    @Override
    public void removeLike(int filmId, int userId) {

    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return null;
    }

    private final Map<Integer, Film> films;

    public InMemoryFilmStorage(Map<Integer, Film> films) {
        this.films = films;
    }

    public int nextId() {
        return id;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    public Optional<Film> findOneById(int filmId) {
        final Film film = films.get(Integer.valueOf(filmId));

        return Optional.ofNullable(film);
    }

    @Override
    public Film add(Film film) {
        int filmId = film.getId();

        if (findOneById(filmId).isPresent()) {
            throw new DuplicateFilmException(String.format("Film %s already exists", film));
        }

        filmId = Math.max(filmId, nextId());
        film.setId(filmId);
        films.put(filmId, film);

        id = filmId + 1;

        return film;
    }

    @Override
    public Film update(Film film) {
        final int filmId = film.getId();

        if (findOneById(filmId).isPresent()) {
            films.put(filmId, film);

            return film;
        }

        throw new NoSuchFilmException(String.format("Film %s not found", film));
    }

    @Override
    public void addLike(int filmId, int userId) {

    }
}
