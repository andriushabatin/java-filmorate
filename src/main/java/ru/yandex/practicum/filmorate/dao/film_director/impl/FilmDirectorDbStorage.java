package ru.yandex.practicum.filmorate.dao.film_director.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film_director.FilmDirectorStorage;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmDirectorDbStorage implements FilmDirectorStorage {

    @Override
    public void createFilmDirectorRelations(int filmId, List<Director> directors) {

    }
}
