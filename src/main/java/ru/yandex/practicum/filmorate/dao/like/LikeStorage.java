package ru.yandex.practicum.filmorate.dao.like;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface LikeStorage {

    public void likeFilm(Film film, User user);
    public void deleteLike(Film film, User user);
}
