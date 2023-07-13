package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryFriendsStorage implements FriendsStorage {
    public final Map<Integer, Set<Integer>> storage;

    public InMemoryFriendsStorage(Map<Integer, Set<Integer>> storage) {
        this.storage = storage;
    }

    public Set<Integer> findAllById(int userId) {
        final Set<Integer> friends = storage.getOrDefault(userId, new HashSet<>());

        return friends;
    }

    public void add(int userId, int friendId) {
        addConnection(userId, friendId);
        addConnection(friendId, userId);
    }

    public void remove(int userId, int friendId) {
        removeConnection(userId, friendId);
        removeConnection(friendId, userId);
    }

    private void addConnection(int first, int second) {
        final Set<Integer> friends = storage.getOrDefault(first, new HashSet<>());

        friends.add(second);

        storage.put(first, friends);
    }

    private void removeConnection(int first, int second) {
        final Set<Integer> friends = storage.getOrDefault(first, new HashSet<>());

        friends.remove(second);

        storage.put(first, friends);
    }
}
