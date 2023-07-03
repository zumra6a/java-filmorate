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
import ru.yandex.practicum.filmorate.model.User;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    private final String uri = "/users";
    private Map<Integer, User> users = new HashMap<>();

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserController userController;

    @BeforeEach
    public void setup() {
        Field field = ReflectionUtils.findField(UserController.class, "users");

        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, userController, users);
    }

    @AfterEach
    public void cleanup() throws Exception {
        users.clear();
    }

    @Test
    @DisplayName("should return all users")
    public void testGetAllUsers() throws Exception {
        final User user = User.builder()
                .id(1)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 07, 01))
                .build();

        users.put(user.getId(), user);

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
        final User user = User.builder()
                .id(1)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 07, 01))
                .build();

        users.put(user.getId(), user);

        final User updatedUser = user.toBuilder()
                .id(1)
                .email("new-email@adress.com")
                .login("new-login")
                .name("New Name")
                .build();

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put(uri)
                                .content(objectMapper.writeValueAsString(updatedUser))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("new-email@adress.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("new-login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthday").value("2000-07-01"));

        assertEquals(1, users.size());
        assertEquals(updatedUser, users.get(1));
    }

    @Test
    @DisplayName("should fail to update non existent user")
    public void testUpdateNonExistentUser() throws Exception {
        final User user = User.builder()
                .id(1000)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 07, 01))
                .build();

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put(uri)
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        assertEquals(0, users.size());
    }

    @Test
    @DisplayName("should add to user")
    public void testAddUser() throws Exception {
        final User user = User.builder()
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 07, 01))
                .build();

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

        assertEquals(1, users.size());
    }
}