package ru.yandex.practicum.filmorate.service;

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

    private static void addToFriendsForBoth(User user, User friend) {
        /*Set<Integer> friends;

        friends = user.getFriends();
        friends.add(friend.getId());
        user.setFriends(friends);

        friends = friend.getFriends();
        friends.add(user.getId());
        friend.setFriends(friends);*/
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

        /*User user = userStorage.getUserById(id);
        User other = userStorage.getUserById(otherId);
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> otherFriends = other.getFriends();

        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        return null;*/
    }


}
