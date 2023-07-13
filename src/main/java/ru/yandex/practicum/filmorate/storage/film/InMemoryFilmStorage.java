package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.DuplicateFilmException;
import ru.yandex.practicum.filmorate.exception.film.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static int id;
    private final Map<Integer, Film> films;

    public InMemoryFilmStorage(Map<Integer, Film> films) {
        this.films = films;
    }

    public int nextId() {
        return ++id;
    }

    @Override
    public List<Film> list() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film add(Film film) {
        if (films.containsKey(film.getId())) {
            throw new DuplicateFilmException(String.format("Film %s already exists", film));
        }

        int filmId = Math.max(film.getId(), nextId());

        film.setId(filmId);
        films.put(filmId, film);

        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);

            return film;
        }

        throw new NoSuchFilmException(String.format("Film %s not found", film));
    }

    @Override
    public Film delete(Film film) {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());

            return film;
        }

        throw new NoSuchFilmException(String.format("Film %s not found", film));
    }
}
