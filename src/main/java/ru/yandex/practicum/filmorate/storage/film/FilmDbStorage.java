package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    @Autowired
    private FilmRepository filmRepository;

    @Override
    public Film create(Film film) throws ObjectAlreadyExistException, ValidationException {

        return filmRepository.save(film);
    }

    @Override
    public Film put(Film film) throws ValidationException {

        Optional<Film> filmDb = this.filmRepository.findById(film.getId());
        if (filmDb.isPresent()) {
            Film filmUpdate = filmDb.get();
            filmUpdate.setId(film.getId());
            filmUpdate.setName(film.getName());
            filmUpdate.setDescription(film.getDescription());
            filmUpdate.setReleaseDate(film.getReleaseDate());
            filmUpdate.setDuration(film.getDuration());
            filmUpdate.setRating(film.getRating());
            filmRepository.save(filmUpdate);
            return filmUpdate;
        } else {
            throw new NotFoundException("Film not found with id : " + film.getId());
        }

        /*Optional<Film> filmDb = this.filmRepository.findById(film.getId());
        if (filmDb.isPresent()) {
            Film filmUpdate = filmDb.get();
            filmUpdate.setId(film.getId());
            filmUpdate.getReleaseDate(film.getReleaseDate());
            userRepository.save(filmUpdate);
            return filmUpdate;
        } else {
            throw new NotFoundException("User not found with id : " + user.getId());
        }*/
    }

    @Override
    public List<Film> findAll() {
        return this.filmRepository.findAll();
    }

    @Override
    public Film findFilmById(int id) {

        Optional <Film> filmDb = this.filmRepository.findById(id);
        if (filmDb.isPresent()) {
            return filmDb.get();
        } else {
            throw new NotFoundException("Film not found with id : " + id);
        }
    }
}
