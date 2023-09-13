package ru.yandex.practicum.filmorate.storage.Like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.LikeRepository;

import java.util.List;

@Component
public class LikeDbStorage {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void likeFilm(Film film, User user) {

        if (likeExists(film.getId(), user.getId())) {
            throw new ObjectAlreadyExistException("Лайк уже существует!");
        } else {
            insertNewLike(film.getId(), user.getId());
        }
    }

    public void deleteLike(Film film, User user) {

        if (likeExists(film.getId(), user.getId())) {
            deleteLike(film.getId(), user.getId());
        } else {
            throw new NotFoundException("Лайк не найден!");
        }
    }

    private boolean likeExists(int filmId, int userId) {

        String sql = "SELECT FILM_ID FROM LIKES WHERE FILM_ID=? AND USER_ID=?";
        List<Integer> existingLikes = jdbcTemplate.queryForList(sql, Integer.class, filmId, userId);
        return !existingLikes.isEmpty();
    }

    private void insertNewLike(int filmId, int userId) {

        String sqlQuery = "INSERT INTO LIKES(film_id, user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                filmId,
                userId
        );
    }

    private void deleteLike(int filmId, int userId) {

        String sql = "DELETE FROM LIKES WHERE film_id=? AND user_id=?";
        jdbcTemplate.update(sql,
                filmId,
                userId
        );
    }
}
