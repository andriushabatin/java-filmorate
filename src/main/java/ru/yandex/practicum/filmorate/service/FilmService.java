package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.HashMap;

@Service
@Slf4j
public class FilmService {
    private final HashMap<Integer, Film> films = new HashMap<>();
    public Film create(Film film) throws ObjectAlreadyExistException, ValidationException {
        if (this.films.containsKey(film.getId())) {
            throw new ObjectAlreadyExistException("Такой фильм уже существует.");
        } else {
            try {
                if (FilmValidator.isValid(film)) {
                    this.films.put(film.getId(), film);
                    log.debug("Фильм " + film.getName() + " добавлен.");
                }
                return film;
            } catch (InvalidNameException | InvalidDurationException | InvalidReleaseDateException |
                     InvalidDescriptionException e) {
                log.error(e.getMessage());
                throw new ValidationException(e.getMessage(), e);
            }
        }
    }
    public Film put(Film film) throws ValidationException {
        try {
            if (FilmValidator.isValid(film)) {
                log.debug("Фильм " + films.get(film.getId()).getName() + " обновлен.");
                this.films.put(film.getId(), film);
            }
            return film;
        } catch (InvalidNameException | InvalidDurationException | InvalidReleaseDateException |
                 InvalidDescriptionException e) {
            throw new ValidationException(e.getMessage(), e);
        }
    }
    public HashMap<Integer, Film> findAll() {
        return this.films;
    }
}
