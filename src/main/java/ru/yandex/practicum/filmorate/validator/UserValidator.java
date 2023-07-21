package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.user.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exception.user.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.user.InvalidLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;
public class UserValidator {
    public static boolean isValid(User user) throws InvalidEmailException, InvalidLoginException, InvalidBirthdayException {
        if (user.getEmail().isEmpty()) {
            throw new InvalidEmailException("Введена пустая почта.");
        } else if (!user.getEmail().contains("@"))  {
            throw new InvalidEmailException("В введенной почте пропущен элемент '@'.");
        } else if (user.getLogin().isEmpty()) {
            throw new InvalidLoginException("Введён пустой логин.");
        } else if (user.getLogin().contains(" ")) {
            throw new InvalidLoginException("Введенный логин содержит пробелы.");
        } else if (user.getBirthday().isAfter(LocalDateTime.now())) {
            throw new InvalidBirthdayException("Дата рождения не может быть в будущем.");
        } else {
            return true;
        }
    }
}
