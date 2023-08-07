package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;

@WebMvcTest(controllers = FilmController.class)
public class FilmControllerTest {
    private final String uri = "/films";
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FilmService filmService;

    @Test
    @DisplayName("should return all films")
    public void testFindAll() throws Exception {
        final Film film1 = Film.builder()
                .id(1)
                .name("name 1")
                .description("description 1")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .build();
        final Film film2 = Film.builder()
                .id(2)
                .name("name 2")
                .description("description 2")
                .releaseDate(LocalDate.of(2023, 8, 2))
                .duration(110)
                .build();

        Mockito.doReturn(List.of(film1, film2)).when(filmService).findAll();

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("name 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseDate").value("2023-07-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].duration").value(120))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("name 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("description 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].releaseDate").value("2023-08-02"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].duration").value(110));
    }

    @Test
    @DisplayName("should return film by id")
    public void testFindOneById() throws Exception {
        final Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .build();

        Mockito.doReturn(film).when(filmService).findOneById(1);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(uri +  "/1").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("2023-07-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(120));
    }

    @Test
    @DisplayName("should add to film")
    public void testAddFilm() throws Exception {
        final Film film = Film.builder()
                .name("New film name")
                .description("New film description")
                .releaseDate(LocalDate.of(2023, 8, 5))
                .duration(65)
                .build();

        Mockito.doReturn(film).when(filmService).add(film);

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
        final Film updatedFilm = film.toBuilder()
                .description("new description")
                .releaseDate(LocalDate.of(2023, 07, 02))
                .build();

        Mockito.doReturn(updatedFilm).when(filmService).update(film);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("new description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("2023-07-02"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(120));
    }

    @Test
    @DisplayName("should add like to film")
    public void testAddLike() throws Exception {
        Mockito.doNothing().when(filmService).addLike(anyInt(), anyInt());

        mockMvc.perform(MockMvcRequestBuilders.put(uri + "/1/like/2"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(filmService).addLike(1, 2);
    }

    @Test
    @DisplayName("should remove like form film")
    public void testRemoveLike() throws Exception {
        Mockito.doNothing().when(filmService).removeLike(anyInt(), anyInt());

        mockMvc.perform(MockMvcRequestBuilders.delete(uri + "/1/like/2"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(filmService).removeLike(1, 2);
    }

    @Test
    @DisplayName("should return list of popular films")
    public void testGetPopularFilms() throws Exception {
        final Film film1 = Film.builder()
                .id(1)
                .name("name 1")
                .description("description 1")
                .releaseDate(LocalDate.of(2023, 7, 1))
                .duration(120)
                .build();
        final Film film2 = Film.builder()
                .id(2)
                .name("name 2")
                .description("description 2")
                .releaseDate(LocalDate.of(2023, 8, 2))
                .duration(110)
                .build();

        Mockito.doReturn(List.of(film1, film2)).when(filmService).getPopularFilms(anyInt());

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(uri + "/popular?count=2").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("name 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseDate").value("2023-07-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].duration").value(120))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("name 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("description 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].releaseDate").value("2023-08-02"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].duration").value(110));

        Mockito.verify(filmService).getPopularFilms(2);
    }
}