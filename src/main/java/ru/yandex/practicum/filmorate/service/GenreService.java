package ru.yandex.practicum.filmorate.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.NoSuchModelException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre findOneById(int id) {
        final Optional<Genre> optGenre = genreStorage.findOneById(id);

        return optGenre.orElseThrow(() -> new NoSuchModelException(String.format("Genre with id %s not found", id)));
    }

    public Genre add(Genre genre) {
        return genreStorage.add(genre);
    }

    public Genre update(Genre genre) {
        return genreStorage.update(genre);
    }

    public void deleteOneById(int id) {
        genreStorage.deleteOneById(id);
    }
}