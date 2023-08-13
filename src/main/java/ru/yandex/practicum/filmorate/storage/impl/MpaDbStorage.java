package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.Mpa.DuplicateMpaException;
import ru.yandex.practicum.filmorate.exception.Mpa.NoSuchMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class MpaDbStorage implements MpaStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAll() {
        final String query = "SELECT * FROM mpa ORDER BY id";

        return jdbcTemplate.query(query, this::mapRowToMpa);
    }

    @Override
    public Optional<Mpa> findOneById(int id) {
        final String query = "SELECT * FROM mpa WHERE id = ?";

        try {
            Mpa mpa = jdbcTemplate.queryForObject(query, this::mapRowToMpa, id);

            return Optional.ofNullable(mpa);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Mpa add(Mpa mpa) {
        final int mpaId = mpa.getId();

        if (findOneById(mpaId).isPresent()) {
            throw new DuplicateMpaException(String.format("MPA %s already exists", mpa));
        }

        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String query = "INSERT INTO mpa (name) VALUES (?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, mpa.getName());

            return ps;
        }, keyHolder);

        int newId = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return findOneById(newId).get();
    }

    @Override
    public Mpa update(Mpa mpa) {
        final int mpaId = mpa.getId();

        if (findOneById(mpaId).isPresent()) {
            final String query = "UPDATE mpa SET name =? WHERE id = ?";

            jdbcTemplate.update(
                    query,
                    mpa.getName(),
                    mpaId
            );

            return findOneById(mpaId).get();
        }

        throw new NoSuchMpaException(String.format("MPA %s not found", mpa));
    }

    @Override
    public void deleteOneById(int id) {
        final String query = "DELETE FROM mpa WHERE id = ?";

        jdbcTemplate.update(query, id);
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa
                .builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}