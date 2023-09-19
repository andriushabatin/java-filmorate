package ru.yandex.practicum.filmorate.dao.film_genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {

    public void createFilmGenreRelations(int filmId, List<Genre> genres);
    public List<Genre> findGenresByFilmId (int id);
    public void updateFilmGenreRelations(int filmId, List<Genre> newGenres);
    public void deleteAllFilmGenreRelationsById(int filmId);
}
