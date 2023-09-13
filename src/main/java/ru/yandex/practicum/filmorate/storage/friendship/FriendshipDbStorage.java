package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;
import ru.yandex.practicum.filmorate.storage.user.UserMapper;

import java.sql.SQLException;
import java.util.List;

@Component
public class FriendshipDbStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addToFriends(User user, User friend) throws SQLException {

            if (friendshipExist(user, friend)) {
                throw new ObjectAlreadyExistException("Дружба уже существует!");
            } else if (friendshipExist(friend, user)) {
                //update
                friendshipUpdateStatus(friend.getId(), user.getId(), 2);
                //insert
                //insertNewFriendship(friend.getId(), user.getId(), 2);
                insertNewFriendship(user.getId(), friend.getId(), 2);
            } else {
                //insert
                //insertNewFriendship(friend.getId(), user.getId(), 1);
                insertNewFriendship(user.getId(), friend.getId(), 1);
            }
    }

    public List<User> getAllFriends(int id) {

        return jdbcTemplate.query(
                "SELECT * FROM USER_TABLE WHERE user_id IN (SELECT friend_id FROM FRIENDSHIP WHERE user_id=?)",
                new UserMapper(),
                id
        );
    }

    public void deleteFromFriends(User user, User friend) throws SQLException {

        if (friendshipExist(user, friend)) {
            if (friendshipExist(friend, user)) {
                deleteFriendship(user, friend);
                friendshipUpdateStatus(1, friend.getId(), user.getId());
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

    private boolean friendshipExist(User user, User friend) throws SQLException {

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

    private void insertNewFriendship(int firstId, int secondId, int status) throws SQLException {
        String sqlQuery = "INSERT INTO FRIENDSHIP(user_id, friend_id, status) " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                firstId,
                secondId,
                status
        );
    }


}
