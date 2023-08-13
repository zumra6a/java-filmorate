package ru.yandex.practicum.filmorate.storage.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.exception.film.DuplicateFilmException;
import ru.yandex.practicum.filmorate.exception.film.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Repository
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public Optional<Film> findOneById(int id) {
        String query = "SELECT F.*, M.id mpa_id, M.NAME mpa_name FROM film F LEFT JOIN mpa M ON F.mpa_id = M.id WHERE F.id = ?";

        try {
            Film film = jdbcTemplate.queryForObject(query, this::mapRowToFilm, id);

            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException ignored) {
            System.out.println(ignored);
            return Optional.empty();
        }
    }

    @Override
    public List<Film> findAll() {
        String query = "SELECT F.*, M.id mpa_id, M.name mpa_name FROM film F LEFT JOIN mpa M ON F.mpa_id = M.id";

        return jdbcTemplate.query(query, this::mapRowToFilm);
    }

    @Override
    public Film add(Film film) {
        int filmId = film.getId();

        if (findOneById(filmId).isPresent()) {
            throw new DuplicateFilmException(String.format("Film %s already exists", film));
        }

        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String query = "INSERT INTO film (name, description, release_date, duration, mpa_id)"
                + " VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, Math.toIntExact(film.getMpa().getId()));

            return ps;
        }, keyHolder);

        int newId = Objects.requireNonNull(keyHolder.getKey()).intValue();

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                addGenreToFilm(newId, genre.getId());
            }
        }

        return findOneById(newId).get();
    }

    @Override
    public Film update(Film film) {
        final int filmId = film.getId();

        if (findOneById(filmId).isPresent()) {
            final String query = "UPDATE film SET name =?, description = ?, release_date = ?, duration = ?, mpa_id = ?"
                    + " WHERE id = ?";

            jdbcTemplate.update(
                    query,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    filmId
            );

            genreStorage.deleteAllByFilmId(filmId);

            if (film.getGenres() != null) {
                for (Genre genre: film.getGenres()) {
                    addGenreToFilm(filmId, genre.getId());
                }
            }

            return findOneById(filmId).get();
        }

        throw new NoSuchFilmException(String.format("Film %s not found", film));
    }

    @Override
    public void addLike(int filmId, int userId) {
        String query = "INSERT INTO favorite_films (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(query, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String query = "DELETE FROM favorite_films WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(query, filmId, userId);
    }

    public List<Integer> fetchLikes(int filmId) {
        String query = "SELECT user_id FROM favorite_films WHERE film_id = ?";

        return jdbcTemplate.query(query, this::mapRowToUserId, filmId);
    }

    public List<Film> getPopularFilms(int limit) {
        String query = "SELECT COUNT(FF.user_id) AS like_count, F.*, M.id mpa_id, M.name mpa_name"
                + " FROM Film F"
                + " LEFT JOIN favorite_films FF ON FF.film_id = F.id"
                + " INNER JOIN mpa M ON M.id = F.mpa_id"
                + " GROUP BY F.id"
                + " ORDER BY like_count DESC"
                + " LIMIT ?";

        return jdbcTemplate.query(query, this::mapRowToFilm, limit);
    }

    public void addGenreToFilm(int filmId, int genreId) {
        String query = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

        jdbcTemplate.update(query, filmId, genreId);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();

        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .mpa(mpa)
                .likes(new HashSet<>(this.fetchLikes(rs.getInt("id"))))
                .genres(new HashSet<>(genreStorage.findAllByFilmId(rs.getInt("id"))))
                .build();
    }

    private int mapRowToUserId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("user_id");
    }
}
