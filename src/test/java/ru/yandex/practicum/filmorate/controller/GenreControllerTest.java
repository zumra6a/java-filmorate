package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = GenreController.class)
@AutoConfigureMockMvc
public class GenreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenreController genreController;

    @Test
    void shouldReturnAll() throws Exception {
        Genre genre = Genre.builder()
                .id(1)
                .name("Трагедия")
                .build();

        Mockito.when(genreController.findAll()).thenReturn(Collections.singletonList(genre));

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(genre))));
    }

    @Test
    void shouldReturnById() throws Exception {
        Genre genre = Genre.builder()
                .id(1)
                .name("Трагедия")
                .build();

        Mockito.when(genreController.findOneById(1)).thenReturn(genre);

        mockMvc.perform(get("/genres/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(genre)));
    }
}