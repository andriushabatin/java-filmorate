package ru.yandex.practicum.filmorate.dao.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {

    public void addToFriends(User user, User friend);
    public List<User> getAllFriends(int id);
    public void deleteFromFriends(User user, User friend);
}

