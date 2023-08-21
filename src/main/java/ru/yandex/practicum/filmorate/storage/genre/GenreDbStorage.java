package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage {

    @Autowired
    private GenreRepository genreRepository;

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(int id) {

        Optional <Genre> genreDb = this.genreRepository.findById(id);
        if (genreDb.isPresent()) {
            return genreDb.get();
        } else {
            throw new NotFoundException("Genre not found with id : " + id);
        }
    }
}
