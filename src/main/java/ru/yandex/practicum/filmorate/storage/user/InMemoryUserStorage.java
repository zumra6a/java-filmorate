package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.user.DuplicateUserException;
import ru.yandex.practicum.filmorate.exception.user.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static int id = 1;
    private final Map<Integer, User> users;

    public InMemoryUserStorage(Map<Integer, User> users) {
        this.users = users;
    }

    public int nextId() {
        return id;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findOneById(int userId) {
        return Optional.ofNullable(users.get(Integer.valueOf(userId)));
    }

    @Override
    public User add(User user) {
        int userId = user.getId();

        if (findOneById(userId).isPresent()) {
            throw new DuplicateUserException(String.format("User %s already exists", user));
        }

        userId = Math.max(userId, nextId());
        user.setId(userId);
        users.put(userId, user);

        id = userId + 1;

        return user;
    }

    @Override
    public User update(User user) {
        final int userId = user.getId();

        if (findOneById(userId).isPresent()) {
            users.put(user.getId(), user);

            return user;
        }

        throw new NoSuchUserException(String.format("User %s not found", user));
    }
}
