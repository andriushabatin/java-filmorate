package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    public User create(User user) throws ObjectAlreadyExistException, ValidationException {
        return userStorage.create(user);
    }

    public User put(User user) throws ValidationException {
        return userStorage.put(user);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User addToFriends(int id, int friendId) throws ValidationException {
        return userStorage.addToFriends(id, friendId);
    }

    public void deleteFromFriends(int id, int friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        Set<Integer> friends = new HashSet<>();

        friends = user.getFriends();
        friends.remove(friendId);
        user.setFriends(friends);

        friends = friend.getFriends();
        friends.remove(id);
        friend.setFriends(friends);
    }



}
