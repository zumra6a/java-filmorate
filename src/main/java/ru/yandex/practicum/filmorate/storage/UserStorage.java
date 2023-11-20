package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    Optional<User> findOneById(int id);

    List<User> findAll();

    User add(User user);

    User update(User user);

    List<Integer> getUserFriends(int userId);

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> commonFriend(int userId, int otherId);
}
