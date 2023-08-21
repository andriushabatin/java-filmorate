package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Like.LikeDbStorage;

@Service
public class LikeService {

    @Autowired
    LikeDbStorage likeStorage ;

    public void likeFilm(Film film, User user) {

        likeStorage.likeFilm(film, user);
    }

    public void deleteLike(Film film, User user) {

        likeStorage.deleteLike(film, user);
    }
}
