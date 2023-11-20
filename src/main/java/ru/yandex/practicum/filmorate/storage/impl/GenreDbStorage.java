package ru.yandex.practicum.filmorate.storage.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.exception.NoSuchModelException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import static java.util.function.UnaryOperator.identity;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        final String query = "SELECT * FROM genre ORDER BY id";

        return jdbcTemplate.query(query, this::mapRowToGenre);
    }

    @Override
    public Optional<Genre> findOneById(int id) {
        final String query = "SELECT * FROM genre WHERE id = ?";

        try {
            Genre genre = jdbcTemplate.queryForObject(query, this::mapRowToGenre, id);

            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Genre add(Genre genre) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String query = "INSERT INTO mpa (name) VALUES (?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, genre.getName());

            return ps;
        }, keyHolder);

        int newId = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return findOneById(newId).get();
    }

    @Override
    public Genre update(Genre genre) {
        final int genreId = genre.getId();

        if (findOneById(genreId).isPresent()) {
            final String query = "UPDATE genre SET name =? WHERE id = ?";

            jdbcTemplate.update(
                    query,
                    genre.getName(),
                    genreId
            );

            return findOneById(genreId).get();
        }

        throw new NoSuchModelException(String.format("Genre %s not found", genre));
    }

    @Override
    public void deleteOneById(int id) {
        final String query = "DELETE FROM genre WHERE id = ?";

        jdbcTemplate.update(query, id);
    }

    @Override
    public List<Genre> findAllByFilmId(int filId) {
        final String query = "SELECT G.* from film_genre FG LEFT JOIN GENRE G on G.id = FG.genre_id WHERE FG.film_id = ? order by G.id";

        return jdbcTemplate.query(query, this::mapRowToGenre, filId);
    }

    @Override
    public void deleteAllByFilmId(int filId) {
        final String query = "DELETE film_genre WHERE film_id = ?";

        jdbcTemplate.update(query, filId);
    }

    @Override
    public void fetchFilmGenres(Film film) {
        fetchFilmGenres(List.of(film));
    }

    @Override
    public void fetchFilmGenres(List<Film> films) {
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        final Set<Integer> ids = filmById.keySet();
        final String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        final String query = String.format("SELECT G.*, FG.film_id from film_genre FG " +
                "RIGHT JOIN GENRE G on G.id = FG.genre_id WHERE FG.film_id IN (%s)", inSql);

        jdbcTemplate.query(query, (rs, rowNum) -> {
            final int filmId = rs.getInt("film_id");
            final Film film = filmById.get(filmId);
            film.addGenre(mapRowToGenre(rs, rowNum));

            return null;
        }, ids.toArray());
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }

}