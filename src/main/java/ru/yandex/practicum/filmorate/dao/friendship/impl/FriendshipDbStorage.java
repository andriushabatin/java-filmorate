package ru.yandex.practicum.filmorate.dao.friendship.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addToFriends(User user, User friend) {

        if (friendshipExist(user, friend)) {
            throw new ObjectAlreadyExistException("Дружба уже существует!");
        } else if (friendshipExist(friend, user)) {
            friendshipUpdateStatus(friend.getId(), user.getId(), 2);
            insertNewFriendship(user.getId(), friend.getId(), 2);
        } else {
            insertNewFriendship(user.getId(), friend.getId(), 1);
        }
    }

    @Override
    public List<User> getAllFriends(int id) {
        try {
            jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?", new UserMapper(), id);
            return jdbcTemplate.query(
                    "SELECT * FROM USERS WHERE user_id IN (SELECT friend_id FROM FRIENDSHIP WHERE user_id=?)",
                    new UserMapper(),
                    id
            );
        } catch (RuntimeException e) {
            throw new NotFoundException("Пользователя с таким id нет");
        }
    }

    @Override
    public void deleteFromFriends(User user, User friend) {

        if (friendshipExist(user, friend)) {
            if (friendshipExist(friend, user)) {
                deleteFriendship(user, friend);
                friendshipUpdateStatus(friend.getId(), user.getId(), 1);
            } else {
                deleteFriendship(user, friend);
            }
        } else {
            throw new NotFoundException();
        }
    }

    private void deleteFriendship(User user, User friend) {

        String sql = "DELETE FROM FRIENDSHIP WHERE user_id=? AND friend_id=?";
        jdbcTemplate.update(sql,
                user.getId(),
                friend.getId()
        );
    }

    private boolean friendshipExist(User user, User friend) {

        String sql = "SELECT USER_ID FROM FRIENDSHIP WHERE USER_ID=? AND friend_id=?";
        List<Integer> a = jdbcTemplate.queryForList(sql, Integer.class, user.getId(), friend.getId());
        return !a.isEmpty();
    }

    private void friendshipUpdateStatus(int user_id, int friend_id, int status) {

        String sql = "UPDATE friendship SET status=? WHERE user_id=? AND friend_id=?";
        jdbcTemplate.update(sql,
                status,
                user_id,
                friend_id
        );
    }

    private void insertNewFriendship(int firstId, int secondId, int status) {

        String sqlQuery = "INSERT INTO FRIENDSHIP(user_id, friend_id, status) " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                firstId,
                secondId,
                status
        );
    }
}
