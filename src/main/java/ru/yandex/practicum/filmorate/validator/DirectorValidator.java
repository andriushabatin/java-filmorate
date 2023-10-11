package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

public class DirectorValidator {

    public static Boolean isValid(Director director) throws ValidationException {

        if (director.getName().isBlank()) {
            throw new ValidationException("Введено пустое имя.");
        } else {
            return true;
        }
    }
}

