package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {

    public static boolean isValid(User user) throws ValidationException {
        if (user.getEmail().isEmpty()) {
            throw new ValidationException("Введена пустая почта.");
        } else if (!user.getEmail().contains("@"))  {
            throw new ValidationException("В введенной почте пропущен элемент '@'.");
        } else if (user.getLogin().isEmpty()) {
            throw new ValidationException("Введён пустой логин.");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("Введенный логин содержит пробелы.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        } else {
            return true;
        }
    }
}
