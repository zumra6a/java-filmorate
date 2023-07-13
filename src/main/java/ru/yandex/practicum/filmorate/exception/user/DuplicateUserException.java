package ru.yandex.practicum.filmorate.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String s) {
        super(s);
    }
}
