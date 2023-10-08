package ru.yandex.practicum.filmorate.dao.film_director.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film_director.FilmDirectorStorage;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmDirectorDbStorage implements FilmDirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createFilmDirectorRelations(int filmId, List<Director> directors) {

        String sqlQuery = "INSERT INTO film_director(film_id, director_id) " +
                "values (?, ?)";

        for (Director director : directors) {
            jdbcTemplate.update(sqlQuery,
                    filmId,
                    director.getId());
        }
    }

    @Override
    public List<Director> findDirectorByFilmId(int id) {

       return new ArrayList<>();
    }

    private Director makeDirector(ResultSet rs) throws SQLException {

        Director director = new Director();
        director.setId(rs.getInt("director_id"));
        director.setName(rs.getString("name"));

        return director;
    }
}
