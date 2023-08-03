package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.filmStorage = inMemoryFilmStorage;
    }

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
        Film film = filmStorage.findFilmById(id);
        Set<Integer> likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);
    }
    
}
