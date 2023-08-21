package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingDbStorage ratingStorage;

    public List<Rating> getMpa() {
        return ratingStorage.getMpa();
    }

    public Rating getMpaById(int id) {
        return ratingStorage.getMpaById(id);
    }
}
