package ru.yandex.practicum.filmorate.storage.film;

public interface LikeStorage {
    int count(int filmId);

    void add(int filmId, int userId);

    void remove(int filmId, int userId);
}
