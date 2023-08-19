package ru.yandex.practicum.filmorate.validator;

import org.apache.commons.lang3.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class UserValidator {

    public static boolean isValid(User user) throws ValidationException {
        if (StringUtils.isEmpty(user.getEmail())) {
            throw new ValidationException("Введена пустая почта.");
        } else if (!user.getEmail().contains("@"))  {
            throw new ValidationException("В введенной почте пропущен элемент '@'.");
        } else if (user.getLogin().isEmpty()) {
            throw new ValidationException("Введён пустой логин.");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("Введенный логин содержит пробелы.");
        } else if (user.getBirthday().after(Date.from(Instant.from(LocalDate.now())))) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        } else if (user.getName().isBlank() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            return true;
        } else {
            return true;
        }
    }
}
