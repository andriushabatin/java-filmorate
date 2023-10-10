package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.data.EventType;
import ru.yandex.practicum.filmorate.data.Operation;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
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
    @Qualifier("FilmDbStorage")
    private FilmStorage filmStorage;
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private EventService eventService;

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
        eventService.createEvent(id, EventType.FRIEND, Operation.ADD, friendId);
    }

    public void deleteFromFriends(int id, int friendId) {

        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);
        friendshipService.deleteFromFriends(user, friend);
        eventService.createEvent(id, EventType.FRIEND, Operation.REMOVE, friendId);
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

    public List<Film> getRecommendations(int userId) {
        userStorage.findUserById(userId);
        return filmStorage.getRecommendationsForUser(userId);
    }
}
