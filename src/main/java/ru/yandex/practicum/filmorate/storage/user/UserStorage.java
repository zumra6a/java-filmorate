package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> findOneById(int id);

    List<User> findAll();

    User add(User user);

    User update(User user);
}
