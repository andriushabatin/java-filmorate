package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;

@Component
public class FriendshipDbStorage {

    @Autowired
    FriendshipRepository friendshipRepository;

    public void addToFriends(User user, User friend) {
        Friendship friendship = Friendship.builder()
                .user_id(user.getId())
                .friend_id(friend.getId())
                .status(2)
                .build();
        friendshipRepository.save(friendship);
    }
}
