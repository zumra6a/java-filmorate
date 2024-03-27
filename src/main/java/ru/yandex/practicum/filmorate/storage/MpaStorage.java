package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.filmorate.model.Mpa;

public interface MpaStorage {
    List<Mpa> findAll();

    Optional<Mpa> findOneById(int id);

    Mpa add(Mpa mpa);

    Mpa update(Mpa mpa);

    void deleteOneById(int id);
}