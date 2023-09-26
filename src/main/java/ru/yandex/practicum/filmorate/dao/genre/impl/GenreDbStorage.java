package ru.yandex.practicum.filmorate.dao.genre.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {

        String sqlQuery = "SELECT * \n" +
                "FROM GENRE;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre getGenreById(int id) {

        String sqlQuery = "SELECT * \n" +
                "FROM GENRE g \n" +
                "WHERE g.GENRE_ID = ?;";

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getInt("genre_id"));
            genre.setName(genreRows.getString("genre"));

            return genre;
        } else {
            throw new NotFoundException("Жанр не найден!");
        }
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {

        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre"));

        return genre;
    }
}
