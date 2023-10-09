package ru.yandex.practicum.filmorate.dao.film_director.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film_director.FilmDirectorStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
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

        String sqlQuery = "SELECT fd.film_id,\n" +
                "fd.director_id,\n" +
                "d.name\n" +
                "FROM film_director AS fd\n" +
                "LEFT JOIN directors AS d ON fd.director_id = d.director_id\n" +
                "GROUP BY fd.film_id,\n" +
                "fd.director_id\n" +
                "HAVING fd.film_id = ?;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeDirector(rs), id);
    }

    @Override
    public void updateFilmDirectorRelations(int id, List<Director> newDirector) {

        deleteAllFilmDirectorRelationsById(id);
        createFilmDirectorRelations(id, newDirector);
    }

    @Override
    public void deleteAllFilmDirectorRelationsById(int filmId) {

        String sqlQuery = "DELETE\n" +
                "FROM film_director\n" +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public List<Integer> findFilmIdsOfDirector(int id, String sortBy) {

        String sqlQuery = "SELECT *\n" +
                "FROM FILM_DIRECTOR fd\n" +
                "LEFT JOIN FILM f ON fd.FILM_ID = f.FILM_ID\n" +
                "GROUP BY fd.FILM_ID  \n" +
                "HAVING fd.DIRECTOR_ID = ?\n";

        if (sortBy.equals("year")) {
            sqlQuery = sqlQuery + "ORDER BY f.RELEASE\n" +
                    "DESC;";
        } else if (sortBy.equals("likes")) {
            sqlQuery = sqlQuery + "ORDER BY f.RATE\n" +
                    "DESC;";
        } else {
            throw new NotFoundException("Не найдена категория сортировки!");
        }

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("film_id"), id);
    }

    private Director makeDirector(ResultSet rs) throws SQLException {

        Director director = new Director();
        director.setId(rs.getInt("director_id"));
        director.setName(rs.getString("name"));

        return director;
    }
}
