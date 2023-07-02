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
import ru.yandex.practicum.filmorate.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    private final String uri = "/users";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws Exception {
        final LocalDate birthday = LocalDate.of(2000, 07, 01);
        final User user = new User("email@adress.com", "login", "User Name", birthday);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(uri)
                                .content(objectMapper.writeValueAsString(user))
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
    @DisplayName("should return all users")
    public void testGetAllUsers() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("email@adress.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value("login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("User Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("2000-07-01"));
    }

    @Test
    @DisplayName("should update user")
    public void testUpdateUser() throws Exception {
        final LocalDate birthday = LocalDate.of(2000, 07, 01);
        final User user = new User("new-email@adress.com", "new-login", "New Name", birthday);

        user.setId(1);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put(uri)
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("new-email@adress.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("new-login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthday").value("2000-07-01"));
    }

    @Test
    @DisplayName("should fail to update non existent user")
    public void testUpdateNonExistentUser() throws Exception {
        final LocalDate birthday = LocalDate.of(2000, 07, 01);
        final User user = new User("email@adress.com", "login", "User Name", birthday);

        user.setId(1000);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put(uri)
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("should add to user")
    public void testAddUser() throws Exception {
        final LocalDate birthday = LocalDate.of(2000, 07, 01);
        final User user = new User("email@adress.com", "login", "User Name", birthday);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(uri)
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email@adress.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("User Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthday").value("2000-07-01"));
    }
}