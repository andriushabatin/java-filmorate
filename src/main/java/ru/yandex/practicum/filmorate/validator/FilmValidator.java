package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.film.InvalidDescriptionException;
import ru.yandex.practicum.filmorate.exception.film.InvalidDurationException;
import ru.yandex.practicum.filmorate.exception.film.InvalidNameException;
import ru.yandex.practicum.filmorate.exception.film.InvalidReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {
    private static final int MAX_DESC_LENGTH = 200;
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public static Boolean isValid(Film film) throws InvalidNameException, InvalidDescriptionException, InvalidReleaseDateException, InvalidDurationException {
        if (film.getName().isEmpty()) {
            throw new InvalidNameException("Введено пустое имя.");
        } else if (film.getDescription().length() > MAX_DESC_LENGTH) {
            throw new InvalidDescriptionException("Длина описания превышает 200 символов.");
        } else if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            throw new InvalidReleaseDateException("Введенная дата раньше, чем дата первого в истории кинопоказа.");
        } else if (film.getDuration().isNegative()) {
            throw new InvalidDurationException("Введена отрицательная продолжительность.");
        } else {
            return true;
        }
    }
}
