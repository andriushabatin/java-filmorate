package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

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
}
