package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.like.LikeStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class LikeService {

    @Autowired
    private final LikeStorage likeStorage;

    public LikeService(LikeStorage likeStorage) {
        this.likeStorage = likeStorage;
    }

    public void likeFilm(Film film, User user) {
        likeStorage.likeFilm(film, user);
    }

    public void deleteLike(Film film, User user) {
        likeStorage.deleteLike(film, user);
    }
}
