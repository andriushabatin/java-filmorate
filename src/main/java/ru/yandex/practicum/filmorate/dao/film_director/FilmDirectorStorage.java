package ru.yandex.practicum.filmorate.dao.film_director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface FilmDirectorStorage {

    public void createFilmDirectorRelations(int filmId, List<Director> directors);

    public List<Director> findDirectorByFilmId(int id);

    public void updateFilmDirectorRelations(int id, List<Director> newDirector);

    public void deleteAllFilmDirectorRelationsById(int filmId);

    List<Integer> findFilmIdsOfDirector(int id, String sortBy);
}
