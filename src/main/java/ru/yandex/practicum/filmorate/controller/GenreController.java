package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

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
}


