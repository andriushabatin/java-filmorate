package ru.yandex.practicum.filmorate.dao.director.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.director.DirectorStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director create(Director director) {

        String sqlQuery = "INSERT INTO DIRECTORS (name)\n" +
                "VALUES (?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);

        return findDirectorById(keyHolder.getKey().intValue());
    }

    @Override
    public Director findDirectorById(int id) {

        String sqlQuery = "SELECT * \n" +
                "FROM DIRECTORS d\n" +
                "WHERE DIRECTOR_ID = ?;";

        SqlRowSet DirectorRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (DirectorRows.next()) {
            Director director = new Director();
            director.setId(DirectorRows.getInt("director_id"));
            director.setName(DirectorRows.getString("name"));
            return director;
        } else {
            throw new NotFoundException("Режиссёр не найден!");
        }
    }

    @Override
    public List<Director> findAll() {

        String sqlQuery = "SELECT * \n" +
                "FROM DIRECTORS;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeDirector(rs));
    }

    @Override
    public Director put(Director director) {

        String sqlQuery = "UPDATE DIRECTORS \n" +
                "SET name = ?\n" +
                "WHERE DIRECTOR_ID  = ?;";

        jdbcTemplate.update(sqlQuery,
                director.getName(),
                director.getId());

        return findDirectorById(director.getId());
    }

    private Director makeDirector(ResultSet rs) throws SQLException {

        Director director = new Director();
        director.setId(rs.getInt("director_id"));
        director.setName(rs.getString("name"));
        return director;
    }
}
