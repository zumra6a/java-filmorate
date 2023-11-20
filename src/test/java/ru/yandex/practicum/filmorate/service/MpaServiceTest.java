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
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MpaServiceTest {

    @Mock
    private MpaStorage mpaStorage;

    @MockBean
    private MpaService mpaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mpaService = new MpaService(mpaStorage);
    }

    @Test
    @DisplayName("should find all mpa")
    void testFindAll() {
        final Mpa mpa1 = Mpa.of(1, "Mpa 1");
        final Mpa mpa2 = Mpa.of(1, "Mpa 1");

        Mockito.doReturn(List.of(mpa1, mpa2)).when(mpaStorage).findAll();

        List<Mpa> result = mpaService.findAll();

        assertEquals(2, result.size());
        assertEquals(mpa1, result.get(0));
        assertEquals(mpa2, result.get(1));

        verify(mpaStorage, times(1)).findAll();
    }

    @Test
    @DisplayName("should find mpa by id")
    void testFindOneByIdMpa() {
        final Mpa mpa = Mpa.of(1, "Mpa");

        Mockito.doReturn(Optional.of(mpa)).when(mpaStorage).findOneById(1);

        Mpa result = mpaService.findOneById(1);

        assertEquals(mpa, result);

        verify(mpaStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should throw error if mpa not found by id")
    public void testFindOneByIdMpaNotFound() {
        Mockito.doReturn(Optional.empty()).when(mpaStorage).findOneById(1);

        assertThrows(NoSuchModelException.class, () -> mpaService.findOneById(1));

        verify(mpaStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should add mpa")
    void testAddMpa() {
        final Mpa mpa = Mpa.of(1, "Mpa");

        Mockito.doReturn(mpa).when(mpaStorage).add(any(Mpa.class));

        Mpa result = mpaService.add(mpa);

        assertEquals(mpa, result);

        verify(mpaStorage, times(1)).add(mpa);
    }

    @Test
    @DisplayName("should update mpa")
    public void testUpdateMpa() {
        final Mpa mpa = Mpa.of(1, "Mpa");

        Mockito.doReturn(mpa).when(mpaStorage).update(any(Mpa.class));

        Mpa result = mpaService.update(mpa);

        assertEquals(mpa, result);

        verify(mpaStorage, times(1)).update(mpa);
    }
}