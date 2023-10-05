package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.director.DirectorStorage;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.model.Director;

@Service
@Slf4j
public class DirectorService {

    @Autowired
    private DirectorStorage directorStorage;

    public Director create(Director director) {
        return directorStorage.create(director);
    }
}
