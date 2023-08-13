package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = MpaController.class)
@AutoConfigureMockMvc
public class MpaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MpaController mpaController;

    @Test
    void shouldReturnAll() throws Exception {
        Mpa mpa = Mpa.builder()
                .id(1)
                .name("G")
                .build();

        Mockito.when(mpaController.findAll()).thenReturn(Collections.singletonList(mpa));

        mockMvc.perform(get("/mpa"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(mpa))));
    }

    @Test
    void shouldReturnById() throws Exception {
        Mpa mpa = Mpa.builder()
                .id(1)
                .name("G")
                .build();

        Mockito.when(mpaController.findOneById(1)).thenReturn(mpa);

        mockMvc.perform(get("/mpa/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mpa)));
    }
}