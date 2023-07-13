package ru.yandex.practicum.filmorate.storage.user;


import java.util.Set;

public interface FriendsStorage {
    public Set<Integer> findAllById(int userId);

    public void add(int userId, int friendId);

    public void remove(int userId, int friendId);
}
