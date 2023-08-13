package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.user.DuplicateUserException;
import ru.yandex.practicum.filmorate.exception.user.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findOneById(int id) {
        String query = "SELECT * FROM users WHERE id = ?";

        try {
            User user = jdbcTemplate.queryForObject(query, this::mapRowToUser, id);

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM users";

        return jdbcTemplate.query(query, this::mapRowToUser);
    }

    @Override
    public User add(User user) {
        int userId = user.getId();

        if (findOneById(userId).isPresent()) {
            throw new DuplicateUserException(String.format("User %s already exists", user));
        }

        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String query = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));

            return ps;
        }, keyHolder);

        int newId = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return findOneById(newId).get();
    }

    @Override
    public User update(User user) {
        int userId = user.getId();

        if (findOneById(userId).isPresent()) {
            final String query = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

            jdbcTemplate.update(
                    query,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    userId
            );

            return findOneById(userId).get();
        }

        throw new NoSuchUserException(String.format("User %s not found", user));
    }

    @Override
    public List<Integer> getUserFriends(int userId) {
        String sqlGetFriends = "SELECT friend_id from friends where user_id = ?";

        return jdbcTemplate.queryForList(sqlGetFriends, Integer.class, userId);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlGetReversFriend = "SELECT * FROM friends WHERE user_id = ? and friend_id = ?";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGetReversFriend, friendId, userId);

        final boolean friendAccepted = sqlRowSet.next();

        String sqlSetFriend = "INSERT INTO friends (user_id, friend_id, approved) VALUES (?, ?, ?)";

        jdbcTemplate.update(sqlSetFriend, userId, friendId, friendAccepted);

        if (friendAccepted) {
            String sqlSetStatus = "UPDATE friends SET approved = true WHERE user_id = ? and friend_id = ?";

            jdbcTemplate.update(sqlSetStatus, friendId, userId);
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlDeleteFriend = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

        jdbcTemplate.update(sqlDeleteFriend, userId, friendId);

        String sqlSetStatus = "UPDATE friends SET approved = false WHERE user_id = ? AND friend_id = ?";

        jdbcTemplate.update(sqlSetStatus, friendId, userId);
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(new HashSet<>(getUserFriends(rs.getInt("id"))))
                .build();
    }
}
