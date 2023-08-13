package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.filmorate.exception.film.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exception.user.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FilmServiceTest {
    @Mock
    private FilmStorage filmStorage;

    @Mock
    private UserStorage userStorage;

    @MockBean
    private FilmService filmService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        filmService = new FilmService(filmStorage, userStorage);
    }

    @Test
    @DisplayName("should find all films")
    public void testFindAll() {
        final Film film1 = Film.builder()
                .id(1)
                .name("name 1")
                .description("description 1")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .build();
        final Film film2 = Film.builder()
                .id(2)
                .name("name 2")
                .description("description 2")
                .releaseDate(LocalDate.of(2023, 8, 2))
                .duration(110)
                .build();

        Mockito.doReturn(List.of(film1, film2)).when(filmStorage).findAll();

        List<Film> result = filmService.findAll();

        assertEquals(2, result.size());
        assertEquals(film1, result.get(0));
        assertEquals(film2, result.get(1));

        verify(filmStorage, times(1)).findAll();
    }

    @Test
    @DisplayName("should find film by id")
    public void testFindOneByIdFilm() {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .build();

        Mockito.doReturn(Optional.of(film)).when(filmStorage).findOneById(1);

        Film result = filmService.findOneById(1);

        assertEquals(film, result);

        verify(filmStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should throw error if film not found by id")
    public void testFindOneByIdFilmNotFound() {
        Mockito.doReturn(Optional.empty()).when(filmStorage).findOneById(1);

        assertThrows(NoSuchFilmException.class, () -> filmService.findOneById(1));

        verify(filmStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should add film")
    public void testAddFilm() {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .build();

        Mockito.doReturn(film).when(filmStorage).add(any(Film.class));

        Film result = filmService.add(film);

        assertEquals(film, result);

        verify(filmStorage, times(1)).add(film);
    }

    @Test
    @DisplayName("should update film")
    public void testUpdateFilm() {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .build();

        Mockito.doReturn(film).when(filmStorage).update(any(Film.class));

        Film result = filmService.update(film);

        assertEquals(film, result);

        verify(filmStorage, times(1)).update(film);
    }

    @Test
    @DisplayName("should add like")
    public void testAddLike() {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .build();

        final User user = User.builder()
                .id(1)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        Mockito.doReturn(Optional.of(film)).when(filmStorage).findOneById(1);
        Mockito.doReturn(Optional.of(user)).when(userStorage).findOneById(1);

        assertDoesNotThrow(() -> filmService.addLike(1, 1));

        verify(filmStorage, times(1)).findOneById(1);
        verify(userStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should throw if user not found")
    public void testAddLikeUnknownUser() {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .build();

        Mockito.doReturn(Optional.of(film)).when(filmStorage).findOneById(1);
        Mockito.doReturn(Optional.empty()).when(userStorage).findOneById(1);

        assertThrows(NoSuchUserException.class, () -> filmService.addLike(1, 1));

        verify(filmStorage, times(1)).findOneById(1);
        verify(userStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should throw if user not found")
    public void testAddLikeUnknownFilm() {
        final User user = User.builder()
                .id(1)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        Mockito.doReturn(Optional.empty()).when(filmStorage).findOneById(1);
        Mockito.doReturn(Optional.of(user)).when(userStorage).findOneById(1);

        assertThrows(NoSuchFilmException.class, () -> filmService.addLike(1, 1));

        verify(filmStorage, times(1)).findOneById(1);
        verify(userStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should remove like")
    public void testRemoveLike() {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .build();
        final User user = User.builder()
                .id(1)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        film.getLikes().add(user.getId());
        Mockito.doReturn(Optional.of(film)).when(filmStorage).findOneById(1);
        Mockito.doReturn(Optional.of(user)).when(userStorage).findOneById(1);

        assertDoesNotThrow(() -> filmService.removeLike(1, 1));

        verify(filmStorage, times(1)).findOneById(1);
        verify(userStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should throw if user not found")
    public void testRemoveLikeUnknownUser() {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .build();

        Mockito.doReturn(Optional.of(film)).when(filmStorage).findOneById(1);
        Mockito.doReturn(Optional.empty()).when(userStorage).findOneById(1);

        assertThrows(NoSuchUserException.class, () -> filmService.removeLike(1, 1));

        verify(filmStorage, times(1)).findOneById(1);
        verify(userStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should throw if user not found")
    public void testRemoveLikeUnknownFilm() {
        final User user = User.builder()
                .id(1)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        Mockito.doReturn(Optional.empty()).when(filmStorage).findOneById(1);
        Mockito.doReturn(Optional.of(user)).when(userStorage).findOneById(1);

        assertThrows(NoSuchFilmException.class, () -> filmService.removeLike(1, 1));

        verify(filmStorage, times(1)).findOneById(1);
        verify(userStorage, times(1)).findOneById(1);
    }
}
