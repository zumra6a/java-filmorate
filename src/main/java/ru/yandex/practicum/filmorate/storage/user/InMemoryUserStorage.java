package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.user.DuplicateUserException;
import ru.yandex.practicum.filmorate.exception.user.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static int id;
    private final Map<Integer, User> users;

    public InMemoryUserStorage(Map<Integer, User> users) {
        this.users = users;
    }

    public int nextId() {
        return ++id;
    }

    @Override
    public List<User> list() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User add(User user) {
        if (users.containsKey(user.getId())) {
            throw new DuplicateUserException(String.format("User %s already exists", user));
        }

        int userId = Math.max(user.getId(), nextId());

        user.setId(userId);
        users.put(userId, user);

        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);

            return user;
        }

        throw new NoSuchUserException(String.format("User %s not found", user));
    }

    @Override
    public User delete(User user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());

            return user;
        }

        throw new NoSuchUserException(String.format("User %s not found", user));
    }
}
