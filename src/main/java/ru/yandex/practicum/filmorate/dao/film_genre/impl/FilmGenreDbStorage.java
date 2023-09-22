package ru.yandex.practicum.filmorate.dao.film_genre.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film_genre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class FilmGenreDbStorage implements FilmGenreStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void createFilmGenreRelations(int filmId, List<Genre> genres) {

        //System.out.println(genres);

        if (Optional.ofNullable(genres).isPresent()) {
            String sqlQuery = "INSERT INTO film_genre(film_id, genre_id) " +
                    "values (?, ?)";

            for (Genre genre : genres) {
                jdbcTemplate.update(sqlQuery,
                        filmId,
                        genre.getId());
            }
        }
    }

    @Override
    public List<Genre> findGenresByFilmId(int id) {

        List<Genre> genres;

        String sqlQuery = "SELECT fg.film_id,\n" +
                "fg.genre_id,\n" +
                "g.genre\n" +
        "FROM film_genre AS fg\n" +
        "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id\n" +
        "GROUP BY fg.film_id,\n" +
                "fg.genre_id\n" +
        "HAVING fg.film_id=?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), id);
    }

    @Override
    public void updateFilmGenreRelations(int filmId, List<Genre> newGenres) {
        deleteAllFilmGenreRelationsById(filmId);
        createFilmGenreRelations(filmId, newGenres);
    }

    @Override
    public void deleteAllFilmGenreRelationsById(int filmId) {

        String sqlQuery = "DELETE\n" +
                "FROM film_genre\n" +
                "WHERE genre_id = ?";

        jdbcTemplate.update(sqlQuery, filmId);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {

        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setGenre(rs.getString("genre"));

        return genre;
    }
}
