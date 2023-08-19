package ru.yandex.practicum.filmorate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.filmorate.key.FriendshipPrimaryKey;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipPrimaryKey> {
}
