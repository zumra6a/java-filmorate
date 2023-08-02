package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        return findOneById(userId).getFriends().stream()
                .map(userStorage::findOneById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void addFriend(int userId, int friendId) {
        final User user = findOneById(userId);
        final User friend = findOneById(friendId);

        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    public void removeFriend(int userId, int friendId) {
        final User user = findOneById(userId);
        final User friend = findOneById(friendId);

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    public List<User> findCommonFriends(int userId, int otherId) {
        final User user = findOneById(userId);
        final User otherUser = findOneById(otherId);
        final Set<Integer> otherUserFriends = otherUser.getFriends();

        return user.getFriends().stream()
                .filter(otherUserFriends::contains)
                .map(userStorage::findOneById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
