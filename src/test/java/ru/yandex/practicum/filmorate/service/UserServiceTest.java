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
import ru.yandex.practicum.filmorate.storage.user.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UserServiceTest {
    @Mock
    private UserStorage userStorage;

    @Mock
    private FriendsStorage friendsStorage;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userStorage, friendsStorage);
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

        Mockito.doReturn(Optional.of(user1)).when(userStorage).findOneById(10);
        Mockito.doReturn(Optional.of(user2)).when(userStorage).findOneById(11);
        Mockito.doReturn(
                Set.of(user1.getId(), user2.getId())
        ).when(friendsStorage).findAllById(1);

        List<User> result = userService.findAllFriends(1);

        assertTrue(result.containsAll(List.of(user1, user2)));
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

        Mockito.doReturn(Optional.of(user1)).when(userStorage).findOneById(10);
        Mockito.doReturn(Optional.of(user2)).when(userStorage).findOneById(11);
        Mockito.doNothing().when(friendsStorage).add(anyInt(), anyInt());

        userService.addFriend(10, 11);

        verify(friendsStorage, times(1)).add(10, 11);
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

        Mockito.doReturn(Optional.of(user1)).when(userStorage).findOneById(10);
        Mockito.doReturn(Optional.of(user2)).when(userStorage).findOneById(11);
        Mockito.doNothing().when(friendsStorage).remove(anyInt(), anyInt());

        userService.removeFriend(10, 11);

        verify(friendsStorage, times(1)).remove(10, 11);
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

        Mockito.doReturn(Optional.of(user1)).when(userStorage).findOneById(10);
        Mockito.doReturn(Optional.of(user2)).when(userStorage).findOneById(11);
        Mockito.doReturn(Optional.of(user3)).when(userStorage).findOneById(13);
        Mockito.doReturn(
                Set.of(user1.getId(), user2.getId())
        ).when(friendsStorage).findAllById(1);
        Mockito.doReturn(
                Set.of(user2.getId(), user3.getId())
        ).when(friendsStorage).findAllById(2);

        List<User> result = userService.findCommonFriends(1, 2);

        assertEquals(1, result.size());
        assertEquals(user2, result.get(0));
    }
}