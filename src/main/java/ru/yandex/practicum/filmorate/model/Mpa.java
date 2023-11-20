package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, access = AccessLevel.PUBLIC)
public class Mpa {
    private Integer id;

    @NotBlank(message = "Mpa name should not be blank")
    @Size(max = 255, message = "Mpa name should not be longer than 255 characters")
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
