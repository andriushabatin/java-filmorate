package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {

    public Film create(Film film) throws ObjectAlreadyExistException, ValidationException;

    public Film put(Film film) throws ValidationException;

    public List<Film> findAll();

    public Film findFilmById(int id);

    public void likeFilm(int id, User user) throws ValidationException;

    public List<Film> getPopularFilms(int count);

    public void deleteLike(int id, User user);

    List<Film> findAllFilmsByDirector(int id, String sortBy);

    List<Film> findCommonFilms(int userId, int friendId);
}
