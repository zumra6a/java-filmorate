package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AfterValidator implements ConstraintValidator<After, LocalDate> {
    private After annotation;

    @Override
    public void initialize(After constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        try {
            LocalDate date = LocalDate.parse(annotation.date());

            return value.isAfter(date);
        } catch (DateTimeParseException exception) {
            return false;
        }
    }
}