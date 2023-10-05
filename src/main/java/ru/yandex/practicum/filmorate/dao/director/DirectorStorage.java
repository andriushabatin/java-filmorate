package ru.yandex.practicum.filmorate.dao.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    Director create(Director director);

    Director findDirectorById(int id);

    List<Director> findAll();

    Director put(Director director);
}
