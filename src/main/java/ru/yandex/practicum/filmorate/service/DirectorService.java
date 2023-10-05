package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.director.DirectorStorage;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Service
@Slf4j
public class DirectorService {

    @Autowired
    private DirectorStorage directorStorage;

    public Director create(Director director) {
        return directorStorage.create(director);
    }

    public Director findDirectorById(int id) {
        return directorStorage.findDirectorById(id);
    }

    public List<Director> findAll() {
        return directorStorage.findAll();
    }

    public Director put(Director director) {
        return directorStorage.put(director);
    }
}
