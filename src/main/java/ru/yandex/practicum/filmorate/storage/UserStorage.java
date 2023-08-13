package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> findOneById(int id);

    List<User> findAll();

    User add(User user);

    User update(User user);

    List<Integer> getUserFriends(int userId);

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);
}
