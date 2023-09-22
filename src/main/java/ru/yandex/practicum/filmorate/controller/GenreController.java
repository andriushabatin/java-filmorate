package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GenreController {

    @Autowired
    GenreService genreService;

    @GetMapping({"/genres"})
    public List<Genre> getGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping({"/genres/{id}"})
    public Genre getGenreById(@PathVariable int id) {
        return genreService.getGenreById(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        return Map.of("error:", e.getMessage());
    }
}


