package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder(toBuilder = true, access = AccessLevel.PUBLIC)
public class Genre {
    private Integer id;

    @NotBlank(message = "Genre name should not be blank")
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