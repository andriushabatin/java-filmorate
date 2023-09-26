package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    @Qualifier("UserDbStorage")
    private UserStorage userStorage;
    @Autowired
    private FriendshipService friendshipService;

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
        return userStorage.findUserById(id);
    }

    public void addToFriends(int id, int friendId) {

        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);
        friendshipService.addToFriends(user, friend);
    }

    public void deleteFromFriends(int id, int friendId) {

        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);
        friendshipService.deleteFromFriends(user, friend);
    }

    public List<User> getAllFriends(int id) {
        return friendshipService.getAllFriends(id);
    }

    public List<User> findCommonFriends(int id, int otherId) {

        List<User> friends = getAllFriends(id);
        List<User> otherFriends = getAllFriends(otherId);

        return friends.stream()
                .filter(otherFriends::contains)
                .collect(Collectors.toList());
    }
}
