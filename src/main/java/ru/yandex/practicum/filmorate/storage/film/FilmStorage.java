package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    public Film create(Film film) throws ObjectAlreadyExistException, ValidationException;

    public Film put(Film film) throws ValidationException;

    public List<Film> findAll();
}
