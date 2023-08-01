package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @Override
    public Film create(Film film) throws ObjectAlreadyExistException, ValidationException {
        if (this.films.containsKey(film.getId())) {
            throw new ObjectAlreadyExistException("Такой фильм уже существует.");
        } else {
            try {
                if (FilmValidator.isValid(film)) {
                    film.setId(getNextId());
                    this.films.put(film.getId(), film);
                    log.debug("Фильм " + film.getName() + " добавлен.");
                }
                return film;
            } catch (ValidationException e) {
                log.error(e.getMessage());
                throw new ValidationException(e.getMessage());
            }
        }
    }

    @Override
    public Film put(Film film) throws ValidationException {
        try {
            if (FilmValidator.isValid(film)) {
                log.debug("Фильм " + films.get(film.getId()).getName() + " обновлен.");
                this.films.put(film.getId(), film);
            }
            return film;
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(this.films.values());
    }

    public int getNextId() {
        return nextId++;
    }
}
