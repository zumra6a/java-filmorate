package ru.yandex.practicum.filmorate.storage.impl;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
//        jdbcTemplate.update("DELETE FROM FAVORITE_FILMS");
//        jdbcTemplate.update("DELETE FROM FILM");
//        jdbcTemplate.update("DELETE FROM USERS");
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM FAVORITE_FILMS");
        jdbcTemplate.update("DELETE FROM FILM");
        jdbcTemplate.update("ALTER TABLE FILM ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    @DisplayName("should add film to database")
    void testAddFilm() {
        final Film film = Film.builder()
                .id(1)
                .name("name 1")
                .description("description 1")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .mpa(Mpa.of(1))
                .build();
        filmDbStorage.add(film);

        Optional<Film> filmOpt = filmDbStorage.findOneById(film.getId());

        assertThat(filmOpt).isNotEmpty();
        assertThat(filmOpt.get())
                .usingRecursiveComparison()
                .ignoringFields("id", "mpa.name")
                .isEqualTo(film);
    }

    @Test
    @DisplayName("should fetch all films from database")
    void testFindAllFilms() {
        final Film film1 = Film.builder()
                .id(1)
                .name("name 1")
                .description("description 1")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .mpa(Mpa.of(1))
                .build();
        final Film film2 = Film.builder()
                .id(2)
                .name("name 2")
                .description("description 2")
                .releaseDate(LocalDate.of(2023, 8, 2))
                .duration(110)
                .mpa(Mpa.of(2))
                .build();
        filmDbStorage.add(film1);
        filmDbStorage.add(film2);

        Collection<Film> result = filmDbStorage.findAll();

        assertThat(result).isNotNull();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("should fetch all films from database")
    void testUpdateFilm() {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .mpa(Mpa.of(1))
                .build();
        filmDbStorage.add(film);

        final Film updatedFilm = film.toBuilder()
                .name("updated name")
                .name("updated description")
                .duration(180)
                .mpa(Mpa.of(2))
                .build();

        filmDbStorage.update(updatedFilm);

        Optional<Film> filmOpt = filmDbStorage.findOneById(film.getId());

        assertThat(filmOpt).isNotEmpty();
        assertThat(filmOpt.get())
                .usingRecursiveComparison()
                .ignoringFields("id", "mpa.name")
                .isEqualTo(updatedFilm);
    }

    @Test
    @DisplayName("should add like to film")
    void testFilmAddLike() {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .mpa(Mpa.of(1))
                .build();
        final User user = User.builder()
                .id(1)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();
        filmDbStorage.add(film);
        userDbStorage.add(user);

        filmDbStorage.addLike(film.getId(), user.getId());

        List<Integer> likes = filmDbStorage.fetchLikes(film.getId());

        assertThat(likes).isNotNull();
        assertThat(likes.contains(user.getId())).isTrue();
    }

    @Test
    @DisplayName("should remove like to film")
    void removeLike() {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .mpa(Mpa.of(1))
                .build();
        final User user = User.builder()
                .id(1)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();
        filmDbStorage.add(film);
        userDbStorage.add(user);

        filmDbStorage.addLike(film.getId(), user.getId());

        List<Integer> likes = filmDbStorage.fetchLikes(film.getId());

        assertThat(likes).isNotNull();
        assertThat(likes.contains(user.getId())).isTrue();

        filmDbStorage.removeLike(film.getId(), user.getId());

        List<Integer> likesAfter = filmDbStorage.fetchLikes(film.getId());

        assertThat(likesAfter).isNotNull();
        assertThat(likesAfter.contains(user.getId())).isFalse();
    }

    @Test
    @DisplayName("should fetch popular films")
    void getPopularFilms() {
        filmDbStorage.add(Film.builder()
                .id(1)
                .name("film 1")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .mpa(Mpa.of(1))
                .build());
        filmDbStorage.add(Film.builder()
                .id(2)
                .name("film 2")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 2))
                .duration(120)
                .mpa(Mpa.of(2))
                .build());
        filmDbStorage.add(Film.builder()
                .id(3)
                .name("film 3")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 3))
                .duration(120)
                .mpa(Mpa.of(3))
                .build());
        userDbStorage.add(User.builder()
                .id(1)
                .email("email1@adress.com")
                .login("login1")
                .name("Name 1")
                .birthday(LocalDate.of(2000, 7, 1))
                .build());
        userDbStorage.add(User.builder()
                .id(2)
                .email("email2@adress.com")
                .login("login2")
                .name("Name 2")
                .birthday(LocalDate.of(2000, 7, 2))
                .build());
        filmDbStorage.addLike(3, 1);
        filmDbStorage.addLike(3, 2);
        filmDbStorage.addLike(1, 1);

        List<Film> popularFilms = filmDbStorage.getPopularFilms(10);

        assertThat(popularFilms.size()).isEqualTo(3);
        assertThat(popularFilms.get(0).getName()).isEqualTo("film 3");
        assertThat(popularFilms.get(1).getName()).isEqualTo("film 1");
        assertThat(popularFilms.get(2).getName()).isEqualTo("film 2");
    }
}