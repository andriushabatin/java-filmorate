package ru.yandex.practicum.filmorate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.filmorate.key.FriendshipPrimaryKey;
import ru.yandex.practicum.filmorate.model.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipPrimaryKey> {
}
