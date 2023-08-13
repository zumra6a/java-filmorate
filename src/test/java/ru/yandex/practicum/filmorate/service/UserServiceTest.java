package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.filmorate.exception.user.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UserServiceTest {
    @Mock
    private UserStorage userStorage;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userStorage);
    }

    @Test
    @DisplayName("should return all users")
    public void testFindAll() {
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

        Mockito.doReturn(List.of(user1, user2)).when(userStorage).findAll();

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
        assertEquals(user1, result.get(0));
        assertEquals(user2, result.get(1));

        verify(userStorage, times(1)).findAll();
    }

    @Test
    @DisplayName("should find user by id")
    public void testFindOneByIdUser() {
        final User user = User.builder()
                .id(10)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        Mockito.doReturn(Optional.of(user)).when(userStorage).findOneById(1);

        User result = userService.findOneById(1);

        assertEquals(user, result);

        verify(userStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should throw error if user not found by id")
    public void testFindOneByIdUserNotFound() {
        Mockito.doReturn(Optional.empty()).when(userStorage).findOneById(1);

        assertThrows(NoSuchUserException.class, () -> userService.findOneById(1));

        verify(userStorage, times(1)).findOneById(1);
    }

    @Test
    @DisplayName("should add user")
    public void testAdduser() {
        final User user = User.builder()
                .id(10)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        Mockito.doReturn(user).when(userStorage).add(any(User.class));

        User result = userService.add(user);

        assertEquals(user, result);

        verify(userStorage, times(1)).add(user);
    }

    @Test
    @DisplayName("should update user")
    public void testUpdateUser() {
        final User user = User.builder()
                .id(10)
                .email("email@adress.com")
                .login("login")
                .name("User Name")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        Mockito.doReturn(user).when(userStorage).update(any(User.class));

        User result = userService.update(user);

        assertEquals(user, result);

        verify(userStorage, times(1)).update(user);
    }

    @Test
    @DisplayName("should find all friends")
    public void testFindAllFriends() {
        final User friend1 = User.builder()
                .id(10)
                .email("friend-1@yandex.com")
                .login("friend-1")
                .name("Friend Name One")
                .birthday(LocalDate.of(2000, 7, 1))
                .build();

        final User friend2 = User.builder()
                .id(11)
                .email("friend-2@yandex.com")
                .login("friend-2")
                .name("Friend Name two")
                .birthday(LocalDate.of(2001, 8, 10))
                .build();

        final User user = User.builder()
                .id(1)
                .email("email@yandex.by")
                .login("user")
                .name("User Name")
                .birthday(LocalDate.of(2001, 8, 10))
                .build();
        user.getFriends().addAll(Set.of(friend1.getId(), friend2.getId()));
        Mockito.doReturn(Optional.of(friend1)).when(userStorage).findOneById(friend1.getId());
        Mockito.doReturn(Optional.of(friend2)).when(userStorage).findOneById(friend2.getId());
        Mockito.doReturn(Optional.of(user)).when(userStorage).findOneById(user.getId());

        List<User> result = userService.findAllFriends(1);

        assertTrue(result.containsAll(List.of(friend1, friend2)));
    }

    @Test
    @DisplayName("should add friend")
    public void testAddFriend() {
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

        Mockito.doReturn(Optional.of(user1)).when(userStorage).findOneById(user1.getId());
        Mockito.doReturn(Optional.of(user2)).when(userStorage).findOneById(user2.getId());

        userService.addFriend(10, 11);

        verify(userStorage).addFriend(10, 11);
    }

    @Test
    @DisplayName("should remove friend")
    public void testRemoveFriend() {
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

        Mockito.doReturn(Optional.of(user1)).when(userStorage).findOneById(user1.getId());
        Mockito.doReturn(Optional.of(user2)).when(userStorage).findOneById(user2.getId());

        userService.removeFriend(10, 11);

        verify(userStorage).deleteFriend(10, 11);
    }

    @Test
    @DisplayName("should find common friends")
    public void testFindCommonFriends() {
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

        final User user3 = User.builder()
                .id(12)
                .email("email@yandex.ru")
                .login("other")
                .name("Name")
                .birthday(LocalDate.of(2000, 3, 1))
                .build();

        user1.getFriends().addAll(Set.of(user2.getId(), user3.getId()));
        user3.getFriends().addAll(Set.of(user1.getId(), user2.getId()));

        Mockito.doReturn(Optional.of(user1)).when(userStorage).findOneById(user1.getId());
        Mockito.doReturn(Optional.of(user2)).when(userStorage).findOneById(user2.getId());
        Mockito.doReturn(Optional.of(user3)).when(userStorage).findOneById(user3.getId());

        List<User> result = userService.findCommonFriends(10, 12);

        assertEquals(1, result.size());
        assertEquals(user2, result.get(0));
    }
}