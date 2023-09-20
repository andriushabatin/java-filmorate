package ru.yandex.practicum.filmorate.dao.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) throws ObjectAlreadyExistException, ValidationException {

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
        /*try {
            if (UserValidator.isValid(user)) {
                return userRepository.save(user);
            }
            return user;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }*/
    }

    @Override
    public User put(User user) throws ValidationException {

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
        /*try {
            if (UserValidator.isValid(user)) {
                Optional<User> userDb = this.userRepository.findById(user.getId());
                if (userDb.isPresent()) {
                    User userUpdate = userDb.get();
                    userUpdate.setId(user.getId());
                    userUpdate.setEmail(user.getEmail());
                    userUpdate.setLogin(user.getLogin());
                    userUpdate.setName(user.getName());
                    userUpdate.setBirthday(user.getBirthday());
                    userRepository.save(userUpdate);
                    return userUpdate;
                } else {
                    throw new NotFoundException("User not found with id : " + user.getId());
                }
            }
            return user;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }*/
    }

    @Override
    public List<User> findAll() {

        String sqlQuery = "SELECT * \n" +
                "FROM users;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
        //return this.userRepository.findAll();
    }

    @Override
    public User findUserById(int id) {

        String sqlQuery = "SELECT *\n" +
                "FROM USERS u \n" +
                "WHERE u.USER_ID = ?;";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        filmRows.next();

        User user = new User();
        user.setId(filmRows.getInt("USER_ID"));
        user.setEmail(filmRows.getString("EMAIL"));
        user.setLogin(filmRows.getString("LOGIN"));
        user.setName(filmRows.getString("NAME"));
        user.setBirthday(filmRows.getDate("BIRTHDAY"));

        return user;
        /*Optional <User> userDb = this.userRepository.findById(id);

        if (userDb.isPresent()) {
            return userDb.get();
        } else {
            throw new NotFoundException("User not found with id : " + id);
        }*/
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
