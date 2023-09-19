package ru.yandex.practicum.filmorate.dao.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
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

        //return keyHolder.getKey().intValue();
        return null;

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
        return null;
    }

    @Override
    public List<User> findAll() {
        //return this.userRepository.findAll();
        return null;
    }

    @Override
    public User findUserById(int id) {
        /*Optional <User> userDb = this.userRepository.findById(id);

        if (userDb.isPresent()) {
            return userDb.get();
        } else {
            throw new NotFoundException("User not found with id : " + id);
        }*/

        return null;
    }
}
