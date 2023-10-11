package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
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

    @Override
    public Film findFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void likeFilm(int id, User user) {
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        return null;
    }

    @Override
    public List<Film> getMostPopularFilmsByYear(Integer count, Integer year) {
        return null;
    }

    @Override
    public List<Film> getMostPopularFilmsByGenre(Integer count, Integer genreId) {
        return null;
    }

    @Override
    public List<Film> getMostPopularFilmsByGenreAndYear(Integer count, Integer genreId, Integer year) {
        return null;
    }

    @Override
    public void deleteLike(int id, User user) {
    }

    @Override
    public List<Film> findAllFilmsByDirector(int id, String sortBy) {
        return null;
    }

    @Override
    public List<Film> findCommonFilms(int userId, int friendId) {
        return null;
    }

    public int getNextId() {
        return nextId++;
    }
}
