package ru.yandex.practicum.filmorate.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.yandex.practicum.filmorate.exception.NoSuchModelException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class GenreServiceTest {

    @Mock
    private GenreStorage genreStorage;

    @MockBean
    private GenreService genreService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        genreService = new GenreService(genreStorage);
    }

    @Test
    @DisplayName("should find all genres")
    void testFindAll() {
        final Genre genre1 = Genre.of(1, "Genre 1");
        final Genre genre2 = Genre.of(1, "Genre 1");

        Mockito.doReturn(List.of(genre1, genre2)).when(genreStorage).findAll();

        List<Genre> result = genreService.findAll();

        assertEquals(2, result.size());
        assertEquals(genre1, result.get(0));
        assertEquals(genre1, result.get(1));

        verify(genreStorage, times(1)).findAll();
    }

    @Test
    @DisplayName("should find genre by id")
    void testFindOneByIdGenre() {
        final Genre genre = Genre.of(1, "Genre");

        Mockito.doReturn(Optional.of(genre)).when(genreStorage).findOneById(1);

        Genre result = genreService.findOneById(1);

        assertEquals(genre, result);

        verify(genreStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should throw error if genre not found by id")
    public void testFindOneByIdGenreNotFound() {
        Mockito.doReturn(Optional.empty()).when(genreStorage).findOneById(1);

        assertThrows(NoSuchModelException.class, () -> genreService.findOneById(1));

        verify(genreStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should add genre")
    void testAddGenre() {
        final Genre genre = Genre.of(1, "Genre");

        Mockito.doReturn(genre).when(genreStorage).add(any(Genre.class));

        Genre result = genreService.add(genre);

        assertEquals(genre, result);

        verify(genreStorage, times(1)).add(genre);
    }

    @Test
    @DisplayName("should update genre")
    public void testUpdateGenre() {
        final Genre genre = Genre.of(1, "Genre");

        Mockito.doReturn(genre).when(genreStorage).update(any(Genre.class));

        Genre result = genreService.update(genre);

        assertEquals(genre, result);

        verify(genreStorage, times(1)).update(genre);
    }
}