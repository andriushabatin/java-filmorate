package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;

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

    public void deleteLike(int id, int userId) throws NotFoundException {
        Film film = filmStorage.findFilmById(id);
        if (film == null) {
            throw new NotFoundException();
        }
        Set<Integer> likes = film.getLikes();
        if (likes.contains(userId)) {
            likes.remove(userId);
        } else {
            throw new NotFoundException();
        }
        film.setLikes(likes);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> sortedFilms = filmStorage.findAll()
                .stream()
                .sorted(((o1, o2) -> (o2.getLikes().size() - o1.getLikes().size())))
                .collect(Collectors.toList());
        System.out.println(sortedFilms);
        try {
            return sortedFilms.subList(0, count);
        } catch (IndexOutOfBoundsException e) {
            return sortedFilms;
        }
    }
}
