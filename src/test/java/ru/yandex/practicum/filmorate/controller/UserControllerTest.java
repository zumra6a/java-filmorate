package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    private final String uri = "/users";

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("should return all users")
    public void testFindAll() throws Exception {
        final User user1 = User.builder()
                .id(1)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        final User user2 = User.builder()
                .id(1)
                .email("email@yandex.com")
                .login("other-login")
                .name("Other Username")
                .birthday(LocalDate.of(2001, 8, 10))
                .build();

        Mockito.doReturn(List.of(user1, user2)).when(userService).findAll();

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("email@adress.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value("login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("User Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("2000-07-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("email@yandex.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].login").value("other-login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Other Username"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].birthday").value("2001-08-10"));
    }

    @Test
    @DisplayName("should return user by id")
    public void findOneById() throws Exception {
        final User user = User.builder()
                .id(1)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        Mockito.doReturn(user).when(userService).findOneById(1);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(uri + "/1").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email@adress.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("User Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthday").value("2000-07-01"));
    }

    @Test
    @DisplayName("should update user")
    public void testUpdateUser() throws Exception {
        final User user = User.builder()
                .id(1)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        final User updatedUser = user.toBuilder()
                .id(1)
                .email("new-email@adress.com")
                .login("new-login")
                .name("New Name")
                .build();

        Mockito.doReturn(updatedUser).when(userService).update(any());

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
    @DisplayName("should add to user")
    public void testAddUser() throws Exception {
        final User user = User.builder()
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        Mockito.doReturn(user).when(userService).add(any());

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

    @Test
    @DisplayName("should add friend")
    public void testAddToFriend() throws Exception {
        Mockito.doNothing().when(userService).addFriend(anyInt(), anyInt());

        mockMvc.perform(MockMvcRequestBuilders.put(uri + "/1/friends/2"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(userService).addFriend(1, 2);
    }

    @Test
    @DisplayName("should remove friend")
    public void testRemoveToFriend() throws Exception {
        Mockito.doNothing().when(userService).removeFriend(anyInt(), anyInt());

        mockMvc.perform(MockMvcRequestBuilders.delete(uri + "/1/friends/2"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(userService).removeFriend(1, 2);
    }

    @Test
    @DisplayName("should find all friends")
    public void testFindAllFriends() throws Exception {
        final User user1 = User.builder()
                .id(10)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        final User user2 = User.builder()
                .id(11)
                .email("email@yandex.com")
                .login("other-login")
                .name("Other Username")
                .birthday(LocalDate.of(2001, 8, 10))
                .build();

        Mockito.doReturn(List.of(user1, user2)).when(userService).findAllFriends(1);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(uri + "/1/friends").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("email@adress.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value("login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("User Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("2000-07-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("email@yandex.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].login").value("other-login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Other Username"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].birthday").value("2001-08-10"));
    }

    @Test
    @DisplayName("should find common friends")
    public void testFindCommonFriends() throws Exception {
        final User user1 = User.builder()
                .id(10)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        final User user2 = User.builder()
                .id(11)
                .email("email@yandex.com")
                .login("other-login")
                .name("Other Username")
                .birthday(LocalDate.of(2001, 8, 10))
                .build();

        Mockito.doReturn(List.of(user1, user2)).when(userService).findCommonFriends(1, 2);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(uri + "/1/friends/common/2").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("email@adress.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value("login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("User Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("2000-07-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("email@yandex.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].login").value("other-login"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Other Username"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].birthday").value("2001-08-10"));
    }
}