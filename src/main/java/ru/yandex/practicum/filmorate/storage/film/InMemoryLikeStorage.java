package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryLikeStorage implements LikeStorage {
    public final Map<Integer, Set<Integer>> storage;

    public InMemoryLikeStorage(Map<Integer, Set<Integer>> storage) {
        this.storage = storage;
    }

    @Override
    public int count(int filmId) {
        if (storage.containsKey(filmId)) {
            final Set<Integer> likes = storage.get(filmId);

            return likes.size();
        }

        return 0;
    }

    @Override
    public void add(int filmId, int userId) {
        final Set<Integer> likes = storage.getOrDefault(filmId, new HashSet<>());

        likes.add(userId);

        storage.put(filmId, likes);
    }

    @Override
    public void remove(int filmId, int userId) {
        final Set<Integer> likes = storage.get(filmId);

        likes.remove(userId);

        storage.put(filmId, likes);
    }
}
