package ru.yandex.practicum.filmorate.dao.genre.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {

        String sqlQuery = "SELECT * \n" +
                "FROM GENRE;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre getGenreById(int id) {
        return null;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {

        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setGenre(rs.getString("genre"));

        return genre;
    }
}
