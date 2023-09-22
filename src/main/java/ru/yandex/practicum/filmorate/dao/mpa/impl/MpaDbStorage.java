package ru.yandex.practicum.filmorate.dao.mpa.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {

        String sqlQuery = "SELECT * \n" +
                "FROM RATING;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Mpa getMpaById(int id) {

        String sqlQuery = "SELECT * \n" +
                "FROM RATING r \n" +
                "WHERE r.RATING_ID = ?;";

        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (mpaRows.next()) {
            Mpa mpa = new Mpa();
            mpa.setId(mpaRows.getInt("rating_id"));
            mpa.setName(mpaRows.getString("rating"));
            return mpa;
        } else {
            throw new NotFoundException("Mpa не найден!");
        }
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {

        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("rating_id"));
        mpa.setName(rs.getString("rating"));

        return mpa;
    }
}
