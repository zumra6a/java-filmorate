package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.Mpa.NoSuchMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    public Mpa findOneById(int id) {
        final Optional<Mpa> optMpa = mpaStorage.findOneById(id);

        return optMpa.orElseThrow(() -> new NoSuchMpaException(String.format("Mpa with id %s not found", id)));
    }

    public Mpa add(@NotNull Mpa mpa) {
        return mpaStorage.add(mpa);
    }

    public Mpa update(@NotNull Mpa mpa) {
        return mpaStorage.update(mpa);
    }

    public void deleteOneById(int id) {
        mpaStorage.deleteOneById(id);
    }
}