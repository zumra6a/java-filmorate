package ru.yandex.practicum.filmorate.model;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("user email should not be null")
    public void testNullEmail() {
        final LocalDate birthdate = LocalDate.now().minusDays(1);
        final User user = new User(null, "", "", birthdate);

        assertTrue(validator
                .validateProperty(user, "email")
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("User email should not be null")));
    }

    @Test
    @DisplayName("user email should be well-formed")
    public void testWellFormedEmail() {
        final LocalDate birthdate = LocalDate.now().minusDays(1);
        final User user = new User("some-invalid-email", "", "", birthdate);

        assertTrue(validator
                .validateProperty(user, "email")
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("User should have well-formed email address")));
    }

    @Test
    @DisplayName("user login should not be null")
    public void testNullLogin() {
        final LocalDate birthdate = LocalDate.now().minusDays(1);
        final User user = new User("email@adress.com", null, "", birthdate);

        assertTrue(validator
                .validateProperty(user, "login")
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("User should have login")));
    }

    @Test
    @DisplayName("user login should not be blank")
    public void testBlankLogin() {
        final LocalDate birthdate = LocalDate.now().minusDays(1);
        final User user = new User("email@adress.com", "", "", birthdate);

        assertTrue(validator
                .validateProperty(user, "login")
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("User should have login")));
    }

    @Test
    @DisplayName("user login should not contain spaces")
    public void testSpacesInLogin() {
        final LocalDate birthdate = LocalDate.now().minusDays(1);
        final User user = new User("email@adress.com", "some login", "", birthdate);

        assertTrue(validator
                .validateProperty(user, "login")
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("User login should not have spaces")));
    }

    @Test
    @DisplayName("user birth date should be in past")
    public void testPastBirthdate() {
        final LocalDate today = LocalDate.now().plusDays(1);
        final User user = new User("email@adress.com", "some-login", null, today);

        assertTrue(validator
                .validateProperty(user, "birthday")
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("User birthday should not be in future")));
    }

    @Test
    @DisplayName("user name should be login if it was not defined")
    public void testUndefinedName() {
        final LocalDate birthdate = LocalDate.now().minusDays(1);
        final User user = new User("email@adress.com", "some-login", null, birthdate);

        assertEquals(user.getName(), user.getLogin());
    }
}