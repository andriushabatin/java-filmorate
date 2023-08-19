package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;
import ru.yandex.practicum.filmorate.storage.user.UserMapper;

import java.util.List;

@Component
public class FriendshipDbStorage {

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addToFriends(User user, User friend) {
        Friendship friendship = Friendship.builder()
                .user_id(user.getId())
                .friend_id(friend.getId())
                .status(2)
                .build();
        friendshipRepository.save(friendship);
    }


    /*public List<Friendship> getAllFriends(int id) {
        return jdbcTemplate.query("SELECT * FROM friendship", new FriendshipMapper());
    }*/

    public List<User> getAllFriends(int id) {
        return jdbcTemplate.query(
                "SELECT * FROM USER_TABLE WHERE user_id IN (SELECT user_id FROM FRIENDSHIP WHERE friend_id=?)",
                new Object[]{id},
                new UserMapper()
        );
    }
}
