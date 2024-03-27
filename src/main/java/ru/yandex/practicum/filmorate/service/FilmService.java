package ru.yandex.practicum.filmorate.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.NoSuchModelException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
    }

    public List<Film> findAll() {
        List<Film> films = filmStorage.findAll();

        genreStorage.fetchFilmGenres(films);

        return films;
    }

    public Film findOneById(int filmId) {
        final Optional<Film> optFilm = filmStorage.findOneById(filmId);

        if (optFilm.isPresent()) {
            Film film = optFilm.get();

            genreStorage.fetchFilmGenres(film);

            return film;
        }

        throw new NoSuchModelException(String.format("Film with id %s not found", filmId));
    }

    public Film add(Film film) {
        final Film addedFilm = filmStorage.add(film);

        genreStorage.fetchFilmGenres(addedFilm);

        return addedFilm;
    }

    public Film update(Film film) {
        final Film updatedFilm = filmStorage.update(film);

        genreStorage.fetchFilmGenres(updatedFilm);

        return updatedFilm;
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public void addLike(int filmId, int userId) {
        final Optional<Film> optFilm = filmStorage.findOneById(filmId);
        final Optional<User> optUser = userStorage.findOneById(userId);

        if (optFilm.isPresent() && optUser.isPresent()) {
            filmStorage.addLike(filmId, userId);
            return;
        }

        if (optUser.isEmpty()) {
            throw new NoSuchModelException(String.format("User with id %s not found", userId));
        }

        if (optFilm.isEmpty()) {
            throw new NoSuchModelException(String.format("Film with id %s not found", filmId));
        }
    }

    public void removeLike(int filmId, int userId) {
        final Optional<Film> optFilm = filmStorage.findOneById(filmId);
        final Optional<User> optUser = userStorage.findOneById(userId);

        if (optFilm.isPresent() && optUser.isPresent()) {
            filmStorage.removeLike(filmId, userId);
            return;
        }

        if (optUser.isEmpty()) {
            throw new NoSuchModelException(String.format("User with id %s not found", userId));
        }

        if (optFilm.isEmpty()) {
            throw new NoSuchModelException(String.format("Film with id %s not found", filmId));
        }
    }
}
