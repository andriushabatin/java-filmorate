package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.FriendshipStatus;

import java.util.HashMap;

@Component
public class FriendshipStorage {

    private HashMap<String, FriendshipStatus> fshipStatusMap = new HashMap<>();

    public HashMap<String, FriendshipStatus> getFshipStatusMap() {
        return fshipStatusMap;
    }

    public void setFshipStatusMap(HashMap<String, FriendshipStatus> fshipStatusMap) {
        this.fshipStatusMap = fshipStatusMap;
    }
}
