package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;

import java.util.*;

@Service
@Slf4j
public class FilmService {

    @Autowired
    @Qualifier("FilmDbStorage")
    private FilmStorage filmStorage;

    @Autowired
    @Qualifier("UserDbStorage")
    private UserStorage userStorage;

    public Film create(Film film) throws ObjectAlreadyExistException, ValidationException {
        return filmStorage.create(film);
    }

    public Film put(Film film) throws ValidationException {
        return filmStorage.put(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findFilmById(int id) {
        return filmStorage.findFilmById(id);
    }

    public void likeFilm(int id, int userId) {
        filmStorage.likeFilm(id, userStorage.findUserById(userId));
    }

    public void deleteLike(int id, int userId) throws NotFoundException {
        filmStorage.deleteLike(id, userStorage.findUserById(userId));
    }

    public List<Film> getMostPopulars(Integer count, Integer genreId, Integer year) {
        if (genreId == null && year == null) {
            return filmStorage.getMostPopulars(count);
        } else if (genreId == null) {
            return filmStorage.getMostPopularsByYear(count, year);
        } else if (year == null) {
            return filmStorage.getMostPopularsByGenre(count, genreId);
        } else {
            return filmStorage.getMostPopularsByGenreAndYear(count, genreId, year);
        }
    }
}
