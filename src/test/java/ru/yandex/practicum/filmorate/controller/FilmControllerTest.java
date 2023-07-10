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
import org.springframework.util.ReflectionUtils;
import ru.yandex.practicum.filmorate.model.Film;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(controllers = FilmController.class)
public class FilmControllerTest {
    private final String uri = "/films";
    private Map<Integer, Film> films = new HashMap<>();

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FilmController filmController;

    @BeforeEach
    public void setup() {
        Field field = ReflectionUtils.findField(FilmController.class, "films");

        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, filmController, films);
    }

    @AfterEach
    public void cleanup() throws Exception {
        films.clear();
    }

    @Test
    @DisplayName("should return all films")
    public void testGetAllFilms() throws Exception {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 07, 01))
                .duration(120)
                .build();

        films.put(film.getId(), film);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseDate").value("2023-07-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].duration").value(120));
    }

    @Test
    @DisplayName("should update film")
    public void testUpdateFilm() throws Exception {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 07, 01))
                .duration(120)
                .build();

        films.put(film.getId(), film);

        final Film updatedFilm = film.toBuilder()
                .description("new description")
                .releaseDate(LocalDate.of(2023, 07, 02))
                .build();

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put(uri)
                                .content(objectMapper.writeValueAsString(updatedFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("new description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("2023-07-02"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(120));

        assertEquals(1, films.size());
        assertEquals(updatedFilm, films.get(1));
    }

    @Test
    @DisplayName("should fail to update non existent film")
    public void testUpdateNonExistentFilm() throws Exception {
        final Film film = Film.builder()
                .id(1000)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 07, 01))
                .duration(120)
                .build();

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put(uri)
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        assertEquals(0, films.size());
    }

    @Test
    @DisplayName("should add to film")
    public void testAddFilm() throws Exception {
        final Film film = Film.builder()
                .name("New film name")
                .description("New film description")
                .releaseDate(LocalDate.of(2023, 8, 05))
                .duration(65)
                .build();

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("2023-08-05"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(65));

        assertEquals(1, films.size());
    }
}