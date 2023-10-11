package ru.yandex.practicum.filmorate.dao.like.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.like.LikeStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void likeFilm(Film film, User user) {

        if (!likeExists(film.getId(), user.getId())) {
            insertNewLike(film.getId(), user.getId());
        }
    }

    @Override
    public void deleteLike(Film film, User user) {

        if (likeExists(film.getId(), user.getId())) {
            deleteLike(film.getId(), user.getId());
        }
    }

    @Override
    public List<Integer> findFilmsIdsOfUser(int userId) {

        String sqlQuery = "SELECT *\n" +
                "FROM LIKES l \n" +
                "LEFT JOIN FILM f ON l.FILM_ID = f.FILM_ID  \n" +
                "GROUP BY l.USER_ID, \n" +
                "         l.FILM_ID \n" +
                "HAVING l.USER_ID = ?\n" +
                "ORDER BY f.RATE DESC;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("film_id"), userId);
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
