package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AfterValidator.class)
public @interface After {
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    String date();
    String message() default "{ru.yandex.practicum.filmorate.validator.AfterDate.message}";
}