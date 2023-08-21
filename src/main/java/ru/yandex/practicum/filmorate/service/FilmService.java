package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    @Autowired
    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;

    @Autowired
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.filmStorage = inMemoryFilmStorage;
        this.userStorage = inMemoryUserStorage;
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
        /*Film film = filmStorage.findFilmById(id);
        User user = userStorage.getUserById(userId);

        Set<Integer> likes = film.getLikes();
        likes.add(user.getId());
        film.setLikes(likes);*/
    }

    public void deleteLike(int id, int userId) throws NotFoundException {
        /*Film film = filmStorage.findFilmById(id);
        Set<Integer> likes = film.getLikes();
        User user = userStorage.getUserById(userId);
        if (likes.contains(user.getId())) {
            likes.remove(user.getId());
        } else {
            throw new NotFoundException();
        }
        film.setLikes(likes);*/;
    }

    public List<Film> getPopularFilms(int count) {
        /*List<Film> sortedFilms = filmStorage.findAll()
                .stream()
                .sorted(((o1, o2) -> (o2.getLikes().size() - o1.getLikes().size())))
                .collect(Collectors.toList());
        System.out.println(sortedFilms);
        try {
            return sortedFilms.subList(0, count);
        } catch (IndexOutOfBoundsException e) {
            return sortedFilms;
        }*/
        return null;
    }
}
