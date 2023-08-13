package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public Collection<Mpa> findAll() {
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    public Mpa findOneById(@PathVariable("id") int id) {
        return mpaService.findOneById(id);
    }

    @PostMapping
    public void post(@Valid @RequestBody Mpa mpa) {
        mpaService.add(mpa);
    }

    @PutMapping
    public void put(@Valid @RequestBody Mpa mpa) {
        mpaService.update(mpa);
    }

    @DeleteMapping("/{id}")
    public void deleteOneById(@PathVariable("id") int id) {
        mpaService.deleteOneById(id);
    }
}