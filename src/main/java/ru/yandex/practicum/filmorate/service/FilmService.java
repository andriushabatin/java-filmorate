package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.data.EventType;
import ru.yandex.practicum.filmorate.data.Operation;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private final EventService eventService;

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
        eventService.createEvent(userId, EventType.LIKE, Operation.ADD, id);
    }

    public void deleteLike(int id, int userId) throws NotFoundException {
        filmStorage.deleteLike(id, userStorage.findUserById(userId));
        eventService.createEvent(userId, EventType.LIKE, Operation.REMOVE, id);
    }

    public List<Film> getMostPopularFilms(Integer count, Integer genreId, Integer year) {
        return filmStorage.getMostPopularFilms(count, genreId, year);
    }

    public List<Film> searchFilmsBySubstring(String query, List<String> by) {
        return filmStorage.searchFilmsBySubstring(query, by);
    }

    public List<Film> findAllFilmsOfDirector(int id, String sortBy) {
        return filmStorage.findAllFilmsByDirector(id, sortBy);
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    }

    public List<Film> findCommonFilms(int userId, int friendId) {
        return filmStorage.findCommonFilms(userId, friendId);
    }
}
