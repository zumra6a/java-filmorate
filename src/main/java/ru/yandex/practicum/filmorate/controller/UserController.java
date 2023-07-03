package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int id = 1;

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        final int userId = user.getId();

        if (!users.containsKey(userId)) {
            final String message = String.format("User %s not found", user);

            log.error(message);
            throw new NoSuchUserException(message);
        }

        log.debug("Update user {} by id {}", user, userId);

        users.put(userId, user);

        return user;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        final int userId = Math.max(id, user.getId());

        id = userId + 1;
        user.setId(userId);

        users.put(userId, user);

        log.debug("Add user {} by id {}", user, userId);

        return user;
    }
}
