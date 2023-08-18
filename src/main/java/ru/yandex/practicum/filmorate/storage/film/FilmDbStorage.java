package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;


public class FilmDbStorage implements FilmStorage {

    @Override
    public Film create(Film film) throws ObjectAlreadyExistException, ValidationException {
        return null;
    }

    @Override
    public Film put(Film film) throws ValidationException {
        return null;
    }

    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public Film findFilmById(int id) {
        return null;
    }
}
