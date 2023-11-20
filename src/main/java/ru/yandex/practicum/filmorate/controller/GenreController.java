package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Collection<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public Genre findOneById(@PathVariable("id") int id) {
        return genreService.findOneById(id);
    }

    @PostMapping
    public void post(@Valid @RequestBody Genre genre) {
        genreService.add(genre);
    }

    @PutMapping
    public void put(@Valid @RequestBody Genre genre) {
        genreService.update(genre);
    }

    @DeleteMapping("/{id}")
    public void deleteOneById(@PathVariable int id) {
        genreService.deleteOneById(id);
    }
}