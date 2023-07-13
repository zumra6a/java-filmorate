package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public UserService(UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findOneById(int userId) {
        final Optional<User> optUser = userStorage.findOneById(userId);

        if (optUser.isPresent()) {
            return optUser.get();
        }

        throw new NoSuchUserException(String.format("User with id %s not found", userId));
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> findAllFriends(int userId) {
        return friendsStorage.findAllById(userId)
                .stream()
                .map(userStorage::findOneById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void addFriend(int userId, int friendId) {
        findOneById(userId);
        findOneById(friendId);

        friendsStorage.add(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        findOneById(userId);
        findOneById(friendId);

        friendsStorage.remove(userId, friendId);
    }

    public List<User> findCommonFriends(int userId, int otherId) {
        final Set<Integer> userFriends = friendsStorage.findAllById(userId);
        final Set<Integer> otherFriends = friendsStorage.findAllById(otherId);

        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(userStorage::findOneById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
