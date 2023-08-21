package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;

import java.sql.SQLException;
import java.util.List;

@Service
public class FriendshipService {

    @Autowired
    FriendshipDbStorage friendshipStorage;
    public void addToFriends(User user, User friend) throws SQLException {
        friendshipStorage.addToFriends(user, friend);
    }

    public List<User> getAllFriends(int id) {
        return friendshipStorage.getAllFriends(id);
    }

    public void deleteFromFriends(User user, User friend) throws SQLException {
        friendshipStorage.deleteFromFriends(user, friend);

    }
}
