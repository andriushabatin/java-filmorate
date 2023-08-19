package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

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

    public void deleteAll() {
        userStorage.deleteAll();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void addToFriends(int id, int friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        friendshipService.addToFriends(user, friend);

        /*String key = id + "," + friendId;
        String alterKey = friendId + "," + id;

        HashMap<String, FriendshipStatus> fshipStatusMap = friendshipStorage.getFshipStatusMap();

        if (fshipStatusMap.containsKey(key)) {

            fshipStatusMap.put(key, FriendshipStatus.CONFIRMED);
            friendshipStorage.setFshipStatusMap(fshipStatusMap);
            addToFriendsForBoth(user, friend);

        } else if (fshipStatusMap.containsKey(alterKey)) {

            fshipStatusMap.put(alterKey, FriendshipStatus.CONFIRMED);
            friendshipStorage.setFshipStatusMap(fshipStatusMap);
            addToFriendsForBoth(user, friend);

        } else {

            fshipStatusMap.put(key, FriendshipStatus.NOT_CONFIRMED);
            friendshipStorage.setFshipStatusMap(fshipStatusMap);
        }*/
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
        /*User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        Set<Integer> friends = new HashSet<>();

        friends = user.getFriends();
        friends.remove(friendId);
        user.setFriends(friends);

        friends = friend.getFriends();
        friends.remove(id);
        friend.setFriends(friends);*/
    }

    public List<User> getAllFriends(int id) {

        return friendshipService.getAllFriends(id);

        /*User user = userStorage.getUserById(id);
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());*/
    }

    public List<User> findCommonFriends(int id, int otherId) {
        /*User user = userStorage.getUserById(id);
        User other = userStorage.getUserById(otherId);
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> otherFriends = other.getFriends();

        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());*/
        return null;
    }


}
