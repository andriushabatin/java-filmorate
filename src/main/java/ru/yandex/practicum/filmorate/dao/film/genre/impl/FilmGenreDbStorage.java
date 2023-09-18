package ru.yandex.practicum.filmorate.dao.film.genre.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.genre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class FilmGenreDbStorage implements FilmGenreStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void createFilmGenreRelations(int filmId, List<Genre> genres) {

        String sqlQuery = "INSERT INTO film_genre(film_id, genre_id) " +
                "values (?, ?)";

        for (Genre genre : genres) {
            jdbcTemplate.update(sqlQuery,
                    filmId,
                    genre.getId());
        }
    }
}
