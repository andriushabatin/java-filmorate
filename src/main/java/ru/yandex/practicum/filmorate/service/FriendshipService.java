package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipStorage friendshipStorage;

    public void addToFriends(User user, User friend) {
        friendshipStorage.addToFriends(user, friend);
    }

    public List<User> getAllFriends(int id) {
        return friendshipStorage.getAllFriends(id);
    }

    public void deleteFromFriends(User user, User friend) {
        friendshipStorage.deleteFromFriends(user, friend);
    }

}
