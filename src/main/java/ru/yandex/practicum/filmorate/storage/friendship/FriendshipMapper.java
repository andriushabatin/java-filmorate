package ru.yandex.practicum.filmorate.storage.friendship;


import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipMapper implements RowMapper<Friendship> {

    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {

            Friendship friendship = new Friendship();

            friendship.setUser_id(rs.getInt("user_id"));
            friendship.setFriend_id(rs.getInt("friend_id"));
            friendship.setStatus(rs.getInt("status"));

            return friendship;
    }
}
