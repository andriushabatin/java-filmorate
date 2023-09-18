package ru.yandex.practicum.filmorate.dao.film.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {

    public void createFilmGenreRelations(int filmId, List<Genre> genres);
}
