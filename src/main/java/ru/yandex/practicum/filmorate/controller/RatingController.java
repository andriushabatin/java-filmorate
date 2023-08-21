package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RatingController {

    @Autowired
    RatingService ratingService;

    @GetMapping({"/mpa"})
    public List<Rating> getMpa() {
        return ratingService.getMpa();
    }

    @GetMapping({"/mpa/{id}"})
    public Rating getMpaById(@PathVariable int id) {
        return ratingService.getMpaById(id);
    }
}
