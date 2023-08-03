package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping({"/films"})
    public Film create(@RequestBody Film film) throws ValidationException, ObjectAlreadyExistException {
        return filmService.create(film);
    }

    @PutMapping({"/films"})
    public Film put(@RequestBody Film film) throws ValidationException {
        return filmService.put(film);
    }

    @GetMapping({"/films"})
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable int id) throws FilmNotFoundException {
        if (filmService.findFilmById(id) == null) {
            throw new FilmNotFoundException();
        } else {
            return filmService.findFilmById(id);
        }
    }

    @PutMapping("films/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) {
        return filmService.getPopularFilms(Integer.parseInt(count));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException (final FilmNotFoundException e) {
        return Map.of("error:", "Фильм не найден!");
    }
}
