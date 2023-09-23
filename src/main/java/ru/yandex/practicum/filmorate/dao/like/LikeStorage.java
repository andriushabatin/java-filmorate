package ru.yandex.practicum.filmorate.dao.like;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface LikeStorage {

    public void likeFilm(Film film, User user);
    public void deleteLike(Film film, User user);
}
