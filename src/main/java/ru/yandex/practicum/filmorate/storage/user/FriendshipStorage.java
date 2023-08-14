package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.FriendshipStatus;

import java.util.HashMap;

@Component
public class FriendshipStorage {

    private HashMap<int[], FriendshipStatus> friendshipStatusHashMap = new HashMap<>();

    public HashMap<int[], FriendshipStatus> getFriendshipStatusHashMap() {
        return friendshipStatusHashMap;
    }

    public void setFriendshipStatusHashMap(HashMap<int[], FriendshipStatus> friendshipStatusHashMap) {
        this.friendshipStatusHashMap = friendshipStatusHashMap;
    }
}
