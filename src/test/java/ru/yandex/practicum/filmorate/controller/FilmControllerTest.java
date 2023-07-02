package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.Film;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@WebMvcTest(controllers = FilmController.class)
public class FilmControllerTest {
    private final String uri = "/films";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws Exception {
        final LocalDate releaseDate = LocalDate.of(2023, 07, 01);
        final Film film = new Film("film name", "film description", releaseDate, 120);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(uri)
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .isOk()
                );
    }

    @AfterEach
    public void cleanup() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(uri)
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .isOk()
                );
    }

    @Test
    @DisplayName("should return all films")
    public void testGetAllFilms() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("film name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("film description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseDate").value("2023-07-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].duration").value(120));
    }

    @Test
    @DisplayName("should update film")
    public void testUpdateFilm() throws Exception {
        final LocalDate releaseDate = LocalDate.of(2023, 07, 01);
        final Film film = new Film("name", "description", releaseDate, 120);

        film.setId(1);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put(uri)
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("2023-07-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(120));
    }

    @Test
    @DisplayName("should fail to update non existent film")
    public void testUpdateNonExistentFilm() throws Exception {
        final LocalDate releaseDate = LocalDate.of(2023, 07, 01);
        final Film film = new Film("name", "description", releaseDate, 120);

        film.setId(1000);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put(uri)
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("should add to film")
    public void testAddFilm() throws Exception {
        final LocalDate releaseDate = LocalDate.of(2023, 07, 01);
        final Film film = new Film("New film name", "New film description", releaseDate, 120);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(uri)
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New film name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("New film description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("2023-07-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(120));
    }
}