package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;
import ru.yandex.practicum.filmorate.storage.user.UserMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class FriendshipDbStorage {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addToFriends(User user, User friend) throws SQLException {

            if (friendshipExist(friend, user)) {
                throw new ObjectAlreadyExistException("Дружба уже существует!");
            } else if (friendshipExist(user, friend)) {
                //update
                friendshipUpdateStatus(2, user.getId(), friend.getId());
                //insert
                insertNewFriendship(friend.getId(), user.getId(), 2);
            } else {
                //insert
                insertNewFriendship(friend.getId(), user.getId(), 1);
            }
    }

    public List<User> getAllFriends(int id) {

        return jdbcTemplate.query(
                "SELECT * FROM USER_TABLE WHERE user_id IN (SELECT user_id FROM FRIENDSHIP WHERE friend_id=?)",
                new UserMapper(),
                id
        );
    }

    private boolean friendshipExist(User friend, User user) throws SQLException {

        String sql = "SELECT USER_ID FROM FRIENDSHIP WHERE USER_ID=? AND friend_id=?";
        List<Integer> a = jdbcTemplate.queryForList(sql, Integer.class, friend.getId(), user.getId());
        return !a.isEmpty();
    }

    private void friendshipUpdateStatus(int status, int firstId, int SecondId) {

        jdbcTemplate.update(
                "UPDATE friendship SET status=? WHERE user_id=? AND friend_id=?",
                status,
                firstId,
                SecondId
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
