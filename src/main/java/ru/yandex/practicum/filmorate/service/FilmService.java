package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exception.user.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findOneById(int filmId) {
        final Optional<Film> optFilm = filmStorage.findOneById(filmId);

        if (optFilm.isPresent()) {
            return optFilm.get();
        }

        throw new NoSuchFilmException(String.format("Film with id %s not found", filmId));
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getPopularFilms(int count) {
        return findAll().stream()
                .sorted((Film a, Film b) -> b.getLikes().size() - a.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addLike(int filmId, int userId) {
        final Optional<Film> optFilm = filmStorage.findOneById(filmId);
        final Optional<User> optUser = userStorage.findOneById(userId);

        if (optFilm.isPresent() && optUser.isPresent()) {
            final Film film = optFilm.get();
            film.getLikes().add(userId);
            return;
        }

        if (optUser.isEmpty()) {
            throw new NoSuchUserException(String.format("User with id %s not found", userId));
        }

        if (optFilm.isEmpty()) {
            throw new NoSuchFilmException(String.format("Film with id %s not found", filmId));
        }
    }

    public void removeLike(int filmId, int userId) {
        final Optional<Film> optFilm = filmStorage.findOneById(filmId);
        final Optional<User> optUser = userStorage.findOneById(userId);

        if (optFilm.isPresent() && optUser.isPresent()) {
            final Film film = optFilm.get();
            film.getLikes().remove(userId);

            return;
        }

        if (optUser.isEmpty()) {
            throw new NoSuchUserException(String.format("User with id %s not found", userId));
        }

        if (optFilm.isEmpty()) {
            throw new NoSuchFilmException(String.format("Film with id %s not found", filmId));
        }
    }
}
