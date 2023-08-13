package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder(toBuilder = true, access = AccessLevel.PUBLIC)
public class Mpa {
    private Integer id;

    @NotBlank(message = "Mpa name should not be blank")
    private String name;

    public static Mpa of(int id) {
        return Mpa.builder()
                .id(id)
                .build();
    }

    public static Mpa of(int id, String name) {
        return Mpa.builder()
                .id(id)
                .name(name)
                .build();
    }
}
