package ru.yandex.practicum.filmorate.validator;

import org.apache.commons.lang3.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Date;

public class FilmValidator {
    private static final int MAX_DESC_LENGTH = 200;
    private static final Date EARLIEST_RELEASE_DATE = java.sql.Date.valueOf(LocalDate.of(1895, 12, 28));

    public static Boolean isValid(Film film) throws ValidationException {

        if(StringUtils.isEmpty(film.getName())) {
            throw new ValidationException("Введено пустое имя.");
        } else if(film.getDescription().length() > MAX_DESC_LENGTH) {
            throw new ValidationException("Длина описания превышает 200 символов.");
        } else if(film.getReleaseDate().before(EARLIEST_RELEASE_DATE)) {
            throw new ValidationException("Введенная дата раньше, чем дата первого в истории кинопоказа.");
        } else if(film.getDuration().isNegative()) {
            throw new ValidationException("Введена отрицательная продолжительность.");
        } else {
            return true;
        }
    }
}
