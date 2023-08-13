package ru.yandex.practicum.filmorate.storage.impl;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ru.yandex.practicum.filmorate.model.Genre;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreDbStorageTest(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    @Test
    void findAll() {
        List<Genre> result = genreDbStorage.findAll();

        assertThat(result.size()).isEqualTo(6);
    }

    @Test
    void findOneById() {
        final Optional<Genre> genreOpt = genreDbStorage.findOneById(1);

        assertThat(genreOpt).isNotEmpty();
        assertThat(genreOpt.get())
                .isEqualTo(Genre.of(1, "Комедия"));
    }
}