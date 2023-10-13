package ru.yandex.practicum.filmorate.dao.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("UserDbStorage")
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) throws ObjectAlreadyExistException, ValidationException {

        if (UserValidator.isValid(user)) {
            String sqlQuery = "INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY)\n" +
                    "VALUES (?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getLogin());
                stmt.setString(3, user.getName());
                stmt.setObject(4, user.getBirthday());
                return stmt;
            }, keyHolder);

            return findUserById(keyHolder.getKey().intValue());
        } else {
            return null;
        }
    }

    @Override
    public User put(User user) throws ValidationException {

        if (UserValidator.isValid(user)) {
            String sqlQuery = "UPDATE USERS\n" +
                    "SET EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=?\n" +
                    "WHERE USER_ID=?";

            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());

            return findUserById(user.getId());
        } else {
            return null;
        }
    }

    @Override
    public List<User> findAll() {

        String sqlQuery = "SELECT * \n" +
                "FROM users;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User findUserById(int id) {

        String sqlQuery = "SELECT *\n" +
                "FROM USERS u \n" +
                "WHERE u.USER_ID = ?;";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (filmRows.next()) {

            User user = new User();
            user.setId(filmRows.getInt("USER_ID"));
            user.setEmail(filmRows.getString("EMAIL"));
            user.setLogin(filmRows.getString("LOGIN"));
            user.setName(filmRows.getString("NAME"));
            user.setBirthday(filmRows.getDate("BIRTHDAY"));
            return user;

        } else {
            throw new NotFoundException("Пользователь не найден!");
        }
    }

    @Override
    public void deleteUser(int id) {
        String reviewSql = "DELETE FROM reviews WHERE user_id = ?";
        jdbcTemplate.update(reviewSql, id);
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public void deleteUserById(int id) {
        String sqlQueryToUsers = "DELETE FROM ";
    }

    private User makeUser(ResultSet rs) throws SQLException {

        User user = new User();
        user.setId(rs.getInt("USER_ID"));
        user.setEmail(rs.getString("EMAIL"));
        user.setLogin(rs.getString("LOGIN"));
        user.setName(rs.getString("NAME"));
        user.setBirthday(rs.getDate("BIRTHDAY"));

        return user;
    }
}
