package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int id = 1;

    private Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        final Integer filmId = film.getId();

        if (!films.containsKey(filmId)) {
            final String message = String.format("Film %s not found", film);

            log.error(message);
            throw new NoSuchFilmException(message);
        }

        films.put(filmId, film);

        log.debug("Update film {} by id {}", film, filmId);

        return film;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        final int filmId = Math.max(id, film.getId());

        id = filmId + 1;
        film.setId(filmId);

        films.put(filmId, film);

        log.debug("Add film {} by id {}", film, filmId);

        return film;
    }
}
