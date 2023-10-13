package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException, ObjectAlreadyExistException {
        return filmService.create(film);
    }

    @PutMapping
    public Film put(@RequestBody Film film) throws ValidationException {
        return filmService.put(film);
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable int id) {
        return filmService.findFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopulars(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count,
                                      @RequestParam(value = "genreId", required = false) Integer genreId,
                                      @RequestParam(value = "year", required = false) Integer year) {
        return filmService.getMostPopularFilms(count, genreId, year);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> findAllFilmsOfDirector(@PathVariable("directorId") int id, @RequestParam String sortBy) {
        return filmService.findAllFilmsOfDirector(id, sortBy);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable int id) {
        filmService.deleteFilm(id);
    }

    @GetMapping("/common")
    public List<Film> findCommonFilms(@RequestParam String userId, @RequestParam String friendId) {
        return filmService.findCommonFilms(Integer.parseInt(userId), Integer.parseInt(friendId));
    }

    @GetMapping("/search")
    public List<Film> searchFilmsBySubstring(@RequestParam String query, @RequestParam String by) {
        return filmService.searchFilmsBySubstring(query, by);
    }
}