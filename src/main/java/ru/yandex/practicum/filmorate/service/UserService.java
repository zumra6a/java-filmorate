package ru.yandex.practicum.filmorate.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.NoSuchModelException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findOneById(int userId) {
        final Optional<User> optUser = userStorage.findOneById(userId);

        return optUser.orElseThrow(() -> new NoSuchModelException(String.format("User with id %s not found", userId)));
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> findAllFriends(int userId) {
        return findOneById(userId).getFriends().stream()
                .map(userStorage::findOneById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void addFriend(int userId, int friendId) {
        final User user = findOneById(userId);
        final User friend = findOneById(friendId);

        userStorage.addFriend(user.getId(), friend.getId());
    }

    public void removeFriend(int userId, int friendId) {
        final User user = findOneById(userId);
        final User friend = findOneById(friendId);

        userStorage.deleteFriend(user.getId(), friend.getId());
    }

    public List<User> findCommonFriends(int userId, int otherId) {
        return userStorage.commonFriend(userId, otherId);
    }
}
