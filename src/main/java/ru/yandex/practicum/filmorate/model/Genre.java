package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, access = AccessLevel.PUBLIC)
public class Genre {
    private Integer id;

    @NotBlank(message = "Genre name should not be blank")
    @Size(max = 255, message = "Genre name should not be longer than 255 characters")
    private String name;

    public static Genre of(int id) {
        return Genre.builder()
                .id(id)
                .build();
    }

    public static Genre of(int id, String name) {
        return Genre.builder()
                .id(id)
                .name(name)
                .build();
    }
}