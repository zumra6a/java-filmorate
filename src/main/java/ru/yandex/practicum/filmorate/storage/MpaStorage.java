package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.Mpa.DuplicateMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    List<Mpa> findAll();

    Optional<Mpa> findOneById(int id);

    Mpa add(Mpa mpa) throws DuplicateMpaException;

    Mpa update(Mpa mpa);

    void deleteOneById(int id);
}